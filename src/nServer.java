import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.*;

public class nServer extends Thread {
    private int netticks;
    private long nettickcounterTimeServer = -1;
    private static final int sendbatchsize = 320;
    private static final int timeout = 10000;
    private final Queue<DatagramPacket> receivedPackets = new LinkedList<>();
    private final Queue<String> quitClientIds = new LinkedList<>(); //temporarily holds ids that are quitting
    HashMap<String, Long> banIds = new HashMap<>(); // ids mapped to the time to be allowed back
    private final ArrayList<String> clientIds = new ArrayList<>(); //insertion-ordered list of client ids
    //manage variables for use in the network game, sync to-and-from the actual map and objects
    HashMap<String, HashMap<String, String>> clientArgsMap = new HashMap<>(); //server too, index by uuids
    HashMap<String, HashMap<String, HashMap<String, String>>> sendArgsMaps = new HashMap<>(); //for deltas
    //id maps to queue of cmds we want to run on that client
    private final HashMap<String, Queue<String>> clientNetCmdMap = new HashMap<>();
    //map of doables for handling cmds from clients
    private final HashMap<String, gDoableCmd> clientCmdDoables = new HashMap<>();
    //map of skip votes
    HashMap<String, String> voteSkipMap = new HashMap<>();
    //queue for holding local cmds that the server user should run
    private final Queue<String> serverLocalCmdQueue = new LinkedList<>();
    private static nServer instance = null;    //singleton-instance
    private DatagramSocket serverSocket = null;    //socket object
    //VERY IMPORTANT LIST. whats allowed to be done by the clients
    private static final ArrayList<String> legalClientCommands = new ArrayList<>(Arrays.asList(
            "deleteblock",
            "deleteitem",
            "deleteplayer",
            "deleteprefab",
            "exec",
            "fireweapon",
            "putblock",
            "putitem",
            "respawnnetplayer",
            "requestdisconnect"
    ));

    public static nServer instance() {
        if(instance == null)
            instance = new nServer();
        return instance;
    }

    private nServer() {
        netticks = 0;
        clientCmdDoables.put("fireweapon",
                new gDoableCmd() {
                    void ex(String id, String cmd) {
                        addExcludingNetCmd(id+",server,",
                                cmd.replaceFirst("fireweapon", "cl_fireweapon"));
                        xCon.ex(cmd);
                    }
                });
        clientCmdDoables.put("requestdisconnect",
                new gDoableCmd() {
                    void ex(String id, String cmd) {
                        quitClientIds.add(id);
                    }
                });
        clientCmdDoables.put("respawnnetplayer",
                new gDoableCmd() {
                    void ex(String id, String cmd) {
                        String[] toks = cmd.split(" ");
                        if(toks.length > 1) {
                            String reqid = toks[1];
                            if(reqid.equals(id)) //client can only respawn themself
                                xCon.ex(cmd);
                        }
                    }
                });

        for(String rcs : new String[]{"putblock", "putitem", "deleteblock", "deleteitem", "deleteprefab"}) {
            clientCmdDoables.put(rcs,
                    new gDoableCmd() {
                        void ex(String id, String cmd) {
                            xCon.ex(cmd);
                            addExcludingNetCmd("server", cmd.replaceFirst(rcs, "cl_" + rcs));
                        }
                    });
        }
        clientCmdDoables.put("deleteplayer",
                new gDoableCmd() {
                    void ex(String id, String cmd) {
                        String[] toks = cmd.split(" ");
                        if(toks.length > 1) {
                            String reqid = toks[1];
                            if(reqid.equals(id)) //client can only remove itself
                                xCon.ex(cmd);
                            addExcludingNetCmd("server",
                                    cmd.replaceFirst("deleteplayer ", "cl_deleteplayer "));
                        }
                    }
                });
    }

    public void addQuitClient(String id) {
        quitClientIds.add(id);
    }

    public void checkForUnhandledQuitters() {
        //other players
        for(String id : clientArgsMap.keySet()) {
            if(!id.equals("server")) {
                //check currentTime vs last recorded checkin time
                long lastrecordedtime = Long.parseLong(clientArgsMap.get(id).get("time"));
                if(gTime.gameTime > lastrecordedtime + timeout) {
                    addQuitClient(id);
                }
            }
        }

        while(quitClientIds.size() > 0) {
            String quitterId = quitClientIds.remove();
            removeNetClient(quitterId);
        }
    }

    void addExcludingNetCmd(String excludedids, String cmd) {
        //excludedids is comma-separated string of ids
        if(!excludedids.contains("server"))
            xCon.ex(cmd);
        for(String id : clientNetCmdMap.keySet()) {
            if(!excludedids.contains(id)) {
                addNetCmd(id, cmd);
            }
        }
    }

    public void addNetCmd(String id, String cmd) {
//        System.out.println("TO_"+id+" "+cmd);
        if(id.equalsIgnoreCase("server"))
            serverLocalCmdQueue.add(cmd);
        else
            addNetSendData(clientNetCmdMap, id, cmd);
    }

    public void addNetCmd(String cmd) {
//        System.out.println("TO_ALL: " + cmd);
        xCon.ex(cmd);
        addNetSendData(clientNetCmdMap, cmd);
    }

    private void addNetSendData(HashMap<String, Queue<String>> sendMap, String id, String data) {
        if(!sendMap.containsKey(id))
            sendMap.put(id, new LinkedList<>());
        sendMap.get(id).add(data);
    }

    private void addNetSendData(HashMap<String, Queue<String>> sendMap, String data) {
        for(String id: sendMap.keySet()) {
            addNetSendData(sendMap, id, data);
        }
    }

    public void checkLocalCmds() {
        if(serverLocalCmdQueue.size() > 0) {
            xCon.ex(serverLocalCmdQueue.remove());
        }
    }

    public void checkOutgoingCmdMap() {
        //check clients
        for(String id : clientNetCmdMap.keySet()) {
            if(clientArgsMap.get(id).containsKey("cmdrcv") && clientNetCmdMap.get(id).size() > 0) {
                clientNetCmdMap.get(id).remove();
                clientArgsMap.get(id).remove("cmdrcv");
            }
        }
    }

    public void processPackets(long gameTimeMillis) {
        try {
            HashMap<String, String> netVars = getNetVars();
            if(receivedPackets.size() > 0) {
                DatagramPacket receivePacket = receivedPackets.peek();
                String receiveDataString = new String(receivePacket.getData());
                xCon.instance().debug("SERVER RCV [" + receiveDataString.trim().length() + "]: "
                        + receiveDataString.trim());
                //get the ip address of the client
                InetAddress addr = receivePacket.getAddress();
                int port = receivePacket.getPort();
                //read data of packet
                readData(receiveDataString);
                //get player id of client
                HashMap<String, String> clientmap = nVars.getMapFromNetString(receiveDataString);
                String clientId = clientmap.get("id");
                //relieve bans
                if(banIds.containsKey(clientId) && banIds.get(clientId) < gTime.gameTime)
                    banIds.remove(clientId);
                if(banIds.containsKey(clientId)) {
                    addNetCmd(clientId, "echo You are banned for "
                            + (banIds.get(clientId) - gTime.gameTime) + "ms");
                    addNetCmd(clientId, "disconnect");
                }
                if(clientId != null) {
                    //create response
                    String sendDataString = createSendDataString(netVars, clientId);
                    byte[] sendData = sendDataString.getBytes();
                    DatagramPacket sendPacket =
                            new DatagramPacket(sendData, sendData.length, addr, port);
                    serverSocket.send(sendPacket);
                    xCon.instance().debug("SERVER_SEND_" + clientId + " [" + sendDataString.length() + "]: " + sendDataString);
                    if(sendDataString.length() > sSettings.max_packet_size)
                        System.out.println("*WARNING* PACKET LENGTH EXCEED 508 BYTES: "
                                + "SERVER_SEND_" + clientId + " [" + sendDataString.length() + "]: " + sendDataString);
                }
                receivedPackets.remove();
            }
            HashMap botsMap = cServerLogic.scene.getThingMap("THING_BOTPLAYER");
            if(botsMap.size() > 0 && cBotsLogic.bottime < gameTimeMillis) {
                cBotsLogic.bottime = gameTimeMillis + (long)(1000.0/(double)sSettings.ratebots);
                for(Object id : botsMap.keySet()) {
                    gPlayer p = (gPlayer) botsMap.get(id);
                    nVarsBot.update(p, gameTimeMillis);
                    String receiveDataString = nVarsBot.dumpArgsForId(p.get("id"));
                    xCon.instance().debug("SERVER RCV [" + receiveDataString.trim().length() + "]: "
                            + receiveDataString.trim());
                    readData(receiveDataString);
                    //get player id of client
                    HashMap<String, String> clientmap = nVars.getMapFromNetString(receiveDataString);
                    String clientId = clientmap.get("id");
                    //act as if responding
                    createSendDataString(netVars, clientId);
                }
            }
        }
        catch (Exception e) {
            eUtils.echoException(e);
            e.printStackTrace();
        }
    }

    public HashMap<String, String> getNetVars() {
        HashMap<String, String> keys = new HashMap<>();
        //handle outgoing cmd
        keys.put("cmd", "");
        //handle server outgoing cmds that loopback to the server
        checkLocalCmds();
        //send scores
        keys.put("time", Long.toString(cServerLogic.timeleft));
        if(clientArgsMap.containsKey("server")) {
            for(String s : new String[]{"flagmasterid", "virusids"}) {
                if(clientArgsMap.get("server").containsKey(s))
                    keys.put(s, clientArgsMap.get("server").get(s));
            }
        }
        return keys;
    }

    public HashMap<String, String> getClientNetVars(String clientId) {
        HashMap<String, String> keys = new HashMap<>();
        return keys;
    }

    private String createSendDataString(HashMap<String, String> netVars, String clientid) {
        HashMap<String, HashMap<String, String>> sendDataMap = new HashMap<>();
        if(clientid.length() > 0 && clientNetCmdMap.containsKey(clientid)
                && clientNetCmdMap.get(clientid).size() > 0 && clientArgsMap.containsKey(clientid)) {
            //act as if bot has instantly received outgoing cmds (bots dont have a "client" to exec things on)
            if(!clientArgsMap.get(clientid).containsKey("cmdrcv")) {
                if(clientid.contains("bot"))
                    clientArgsMap.get(clientid).put("cmdrcv", "1");
                netVars.put("cmd", clientNetCmdMap.get(clientid).peek());
            }
        }
        sendDataMap.put("server", new HashMap<>(netVars)); //add server map first
        boolean sendfull = false;
        if(!sendArgsMaps.containsKey(clientid)) {
            sendfull = true;
            sendArgsMaps.put(clientid, new HashMap<>());
        }
        for (String idload2 : clientIds) {
            if (!sendArgsMaps.get(clientid).containsKey(idload2)) {
                sendfull = true;
                sendArgsMaps.get(clientid).put(idload2, new HashMap<>());
            }
            HashMap<String, String> workingMap = new HashMap<>(clientArgsMap.get(idload2));
            if (!sendfull) {
                //calc delta
                for (String k : clientArgsMap.get(idload2).keySet()) {
                    if (clientArgsMap.get(idload2).containsKey(k)
                            && clientArgsMap.get(idload2).get(k).equals(sendArgsMaps.get(clientid).get(idload2).get(k))) {
                        workingMap.remove(k);
                    }
                }
            }
            workingMap.remove("time"); //unnecessary args for sending, but necessary to retain server-side
            workingMap.remove("respawntime"); //unnecessary args for sending, but necessary to retain server-side
            workingMap.remove("id"); //unnecessary args for sending, but necessary to retain server-side
            sendDataMap.put(idload2, new HashMap<>(workingMap));
            sendArgsMaps.get(clientid).put(idload2, new HashMap<>(clientArgsMap.get(idload2)));
            sendArgsMaps.get(clientid).get(idload2).remove("cmdrcv");
        }
        return sendDataMap.toString().replace(", ", ","); //replace to save 1 byte per field
    }

    void removeNetClient(String id) {
        String quitterName = clientArgsMap.get(id).get("name");
        if(clientArgsMap.containsKey("server") && clientArgsMap.get("server").containsKey("flagmasterid")
                && clientArgsMap.get("server").get("flagmasterid").equals(id)) {
            clientArgsMap.get("server").put("flagmasterid", "");
            gPlayer player = cServerLogic.getPlayerById(id);
            int itemId = 0;
            for(String iid : cServerLogic.scene.getThingMap("THING_ITEM").keySet()) {;
                if(itemId < Integer.parseInt(iid))
                    itemId = Integer.parseInt(iid);
            }
            itemId++; //want to be the _next_ id
            addNetCmd(String.format("putitem ITEM_FLAG %d %d %d", itemId,
                    player.getInt("coordx"), player.getInt("coordy")));
        }
        clientArgsMap.remove(id);
        gScoreboard.scoresMap.remove(id);
        clientNetCmdMap.remove(id);
        cServerLogic.scene.getThingMap("THING_PLAYER").remove(id);
        clientIds.remove(id);
        //tell remaining players
        addExcludingNetCmd("server", String.format("echo %s left the game", quitterName));
    }

    public void run() {
        try {
            serverSocket = new DatagramSocket(cServerLogic.listenPort);
            while (sSettings.IS_SERVER) {
                try {
                    netticks++;
                    long gameTime = gTime.gameTime;
                    if (nettickcounterTimeServer < gameTime) {
                        uiInterface.netReportServer = netticks;
                        netticks = 0;
                        nettickcounterTimeServer = gameTime + 1000;
                    }
                    byte[] receiveData = new byte[sSettings.rcvbytesserver];
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    serverSocket.receive(receivePacket);
                    receivedPackets.add(receivePacket);
                    long networkTime = gameTime + (long) (1000.0 / (double) sSettings.rateserver);
                    processPackets(gameTime);
                    checkOutgoingCmdMap();
                    checkForUnhandledQuitters();
                    cServerLogic.gameLoop(gameTime);
                    sleep(Math.max(0, networkTime - gameTime));
                }
                catch (Exception e) {
                    eUtils.echoException(e);
                    e.printStackTrace();
                }
            }
            interrupt();
        }
        catch (Exception ee) {
            eUtils.echoException(ee);
            ee.printStackTrace();
        }
    }

    boolean hasClient(String id) {
        return clientIds.contains(id);
    }

    boolean containsArgsForId(String id, String[] fields) {
        if(!clientArgsMap.containsKey(id))
            return false;
        HashMap<String, String> cargs = clientArgsMap.get(id);
        for(String rf : fields) {
            if(!cargs.containsKey(rf))
                return false;
        }
        return true;
    }

    public void readData(String receiveDataString) {
        String toks = receiveDataString.trim();
        if(toks.length() > 0) {
            String argload = toks;
            HashMap<String, String> packArgMap = nVars.getMapFromNetString(argload);
            HashMap<String, HashMap<String, Integer>> scoresMap = gScoreboard.scoresMap;
            String packId = packArgMap.get("id");
            String packName = packArgMap.get("name");
            if(packId == null)
                return;
            //insert new ids into the greater maps
            if(!scoresMap.containsKey(packId))
                gScoreboard.addId(packId);
            if(!clientArgsMap.containsKey(packId)) {
                clientArgsMap.put(packId, packArgMap);
                handleNewClientJoin(packId, packName);
            }
            //fetch old packet
            HashMap<String, String> oldArgMap = clientArgsMap.get(packId);
            String oldName = "";
            if(oldArgMap != null)
                oldName = oldArgMap.get("name");
            //only want to update keys that have changes
            for(String k : packArgMap.keySet()) {
                clientArgsMap.get(packId).put(k, packArgMap.get(k));
            }
            //record time we last updated client args
            clientArgsMap.get(packId).put("time", Long.toString(gTime.gameTime));
            //parse and process the args from client packet
            if(hasClient(packId)) {
                //update ping
//                scoresMap.get(packId).put("ping", (int) Math.abs(gTime.gameTime - oldTimestamp));
                //handle name change to notify
                if(packName != null && oldName != null && oldName.length() > 0 && !oldName.equals(packName)) {
                    if(clientArgsMap.get(packId).get("color") != null) {
                        oldName += ("#"+clientArgsMap.get(packId).get("color"));
                        packName += ("#"+clientArgsMap.get(packId).get("color"));
                    }
                    addExcludingNetCmd("server",
                            String.format("echo %s changed name to %s", oldName, packName));
                }
                gPlayer packPlayer = cServerLogic.getPlayerById(packId);
                if(packPlayer != null) {
                    if (clientArgsMap.get(packId).containsKey("vels")) {
                        String[] veltoks = clientArgsMap.get(packId).get("vels").split("-");
                        packPlayer.put("vel0", veltoks[0]);
                        packPlayer.put("vel1", veltoks[1]);
                        packPlayer.put("vel2", veltoks[2]);
                        packPlayer.put("vel3", veltoks[3]);
                    }
                    if (sSettings.smoothing) {
                        packPlayer.put("coordx", clientArgsMap.get(packId).get("x"));
                        packPlayer.put("coordy", clientArgsMap.get(packId).get("y"));
                    }
                    if(clientArgsMap.get(packId).containsKey("fv")) {
                        packPlayer.putDouble("fv", Double.parseDouble(clientArgsMap.get(packId).get("fv")));
                    }
                    //store player object's health in outgoing network arg map
                    clientArgsMap.get(packId).put("hp", cServerLogic.getPlayerById(packId).get("stockhp"));
                }
                //store player's wins and scores
                clientArgsMap.get(packId).put("score",  String.format("%d:%d",
                        gScoreboard.scoresMap.get(packId).get("wins"),
                        gScoreboard.scoresMap.get(packId).get("score")));
                if(packArgMap.get("msg") != null && packArgMap.get("msg").length() > 0) {
                    handleClientMessage(packArgMap.get("msg"));
                    checkClientMessageForVoteSkip(packId,
                            packArgMap.get("msg").substring(packArgMap.get("msg").indexOf(':')+2));
                }
                if(packArgMap.get("cmd") != null && packArgMap.get("cmd").length() > 0) {
                    handleClientCommand(packId, packArgMap.get("cmd"));
                }
                if(packArgMap.get("px") != null)
                    clientArgsMap.get(packId).put("px", packArgMap.get("px"));
                if(packArgMap.get("py") != null)
                    clientArgsMap.get(packId).put("py", packArgMap.get("py"));
                if(packArgMap.get("pw") != null)
                    clientArgsMap.get(packId).put("pw", packArgMap.get("pw"));
                if(packArgMap.get("ph") != null)
                    clientArgsMap.get(packId).put("ph", packArgMap.get("ph"));
            }
        }
    }

    private void handleNewClientJoin(String packId, String packName) {
//        System.out.println("NEW_CLIENT: "+packId);
        clientIds.add(packId);
        clientNetCmdMap.put(packId, new LinkedList<>());
        sendArgsMaps.put(packId, new HashMap<>());
        sendMap(packId);
        addNetCmd(packId, "cv_maploaded 1");
        if(!sSettings.show_mapmaker_ui) //spawn in after finished loading
            addNetCmd(packId,"cl_sendcmd respawnnetplayer " + packId);
        for(String clientId : clientIds) {
            gThing player = cServerLogic.scene.getPlayerById(clientId);
            if(player != null)
                addNetCmd(packId, String.format("cl_spawnplayer %s %s %s",
                    clientId, player.get("coordx"), player.get("coordy")));
        }
        addExcludingNetCmd("server", String.format("echo %s joined the game", packName
        + (clientArgsMap.get(packId).get("color") != null ? "#"+clientArgsMap.get(packId).get("color") : "")));
    }

    public void sendMap(String packId) {
        //these three are always here
        ArrayList<String> maplines = new ArrayList<>();
        maplines.add(String.format("cv_velocityplayer %d;cv_maploaded 0;cv_gamemode %d\n",
                cServerLogic.velocityplayerbase, cClientLogic.gamemode));
        HashMap<String, gThing> blockMap = cServerLogic.scene.getThingMap("THING_BLOCK");
        for(String id : blockMap.keySet()) {
            gBlock block = (gBlock) blockMap.get(id);
            String[] args = new String[]{
                    block.get("type"),
                    block.get("id"),
                    block.get("prefabid"),
                    block.get("coordx"),
                    block.get("coordy"),
                    block.get("dimw"),
                    block.get("dimh"),
                    block.get("toph"),
                    block.get("wallh")
            };
            StringBuilder blockString = new StringBuilder("cl_putblock");
            for(String arg : args) {
                if(arg != null) {
                    blockString.append(" ").append(arg);
                }
            }
            maplines.add(blockString.toString());
        }
        HashMap<String, gThing> itemMap = cServerLogic.scene.getThingMap("THING_ITEM");
        for(String id : itemMap.keySet()) {
            gItem item = (gItem) itemMap.get(id);
            String[] args = new String[]{
                    item.get("type"),
                    item.get("id"),
                    item.get("coordx"),
                    item.get("coordy")
            };
            StringBuilder str = new StringBuilder("cl_putitem");
            for(String arg : args) {
                if(arg != null) {
                    str.append(" ").append(arg);
                }
            }
            maplines.add(str.toString());
        }
        maplines.add("cv_maploaded 1");
        //iterate through the maplines and send in batches
        StringBuilder sendStringBuilder = new StringBuilder();
        int linectr = 0;
        for(int i = 0; i < maplines.size(); i++) {
            String line = maplines.get(i);
            String next = "";
            if(maplines.size() > i+1)
                next = maplines.get(i+1);
            sendStringBuilder.append(line).append(";");
            linectr++;
            if(sendStringBuilder.length() + next.length() >= sendbatchsize
            || linectr == maplines.size()) {
                String sendString = sendStringBuilder.toString();
                addNetCmd(packId, sendString.substring(0, sendString.lastIndexOf(';')));
                sendStringBuilder = new StringBuilder();
            }
        }
    }

    private void handleClientMessage(String msg) {
        addExcludingNetCmd("server", "echo " + msg);
        //handle special sounds
        String testmsg = msg.substring(msg.indexOf(':')+2);
        checkMessageForSpecialSound(testmsg);
    }

    private void handleClientCommand(String id, String cmd) {
        String ccmd = cmd.split(" ")[0];
//        System.out.println("FROM_" + id + ": " + cmd);
        if(legalClientCommands.contains(ccmd)) {
            if(ccmd.equals("exec")) {
                System.out.println("CLIENT REQ EXEC: " + cmd);
            }
            if(clientCmdDoables.containsKey(ccmd))
                clientCmdDoables.get(ccmd).ex(id, cmd);
            else if(cmd.startsWith("exec prefabs/")) {
                xCon.ex(cmd);
                addExcludingNetCmd("server", String.format("%s",
                        cmd.replace("exec ", "cl_exec ")));
            }
            else
                addNetCmd(id, "echo NO HANDLER FOUND FOR CMD: " + cmd);
        }
        else
            addNetCmd(id, "echo ILLEGAL CMD REQUEST: " + cmd);
    }

    private void checkMessageForSpecialSound(String testmsg) {
        for(String s : eManager.winSoundFileSelection) {
            String[] ttoks = s.split("\\.");
            if(testmsg.equalsIgnoreCase(ttoks[0])) {
                String soundString = "playsound sounds/win/" + s;
                addExcludingNetCmd("server", soundString);
                break;
            }
        }
    }

    private void checkClientMessageForVoteSkip(String id, String testmsg) {
        //handle the vote-to-skip function
        testmsg = testmsg.strip();
        if(testmsg.equalsIgnoreCase("skip")) {
            if(!voteSkipMap.containsKey(id)) {
                voteSkipMap.put(id,"1");
                if(voteSkipMap.keySet().size() >= cServerLogic.voteskiplimit) {
                    cServerLogic.intermissiontime = gTime.gameTime + cServerLogic.intermissionDelay;
                    for(String s : new String[]{
                            "playsound sounds/win/"+eManager.winSoundFileSelection[
                                    (int) (Math.random() * eManager.winSoundFileSelection.length)],
                            String.format("echo [VOTE_SKIP] VOTE TARGET REACHED (%s)", cServerLogic.voteskiplimit),
                            "echo changing map..."}) {
                        addExcludingNetCmd("server", s);
                    }
                }
                else {
                    String s = String.format("echo [VOTE_SKIP] SAY 'skip' TO END ROUND. (%s/%s)",
                            voteSkipMap.keySet().size(), cServerLogic.voteskiplimit);
                    addExcludingNetCmd("server", s);
                }
            }
            else
                addNetCmd(id, "echo [VOTE_SKIP] YOU HAVE ALREADY VOTED TO SKIP");
        }
    }

    public boolean hasClients() {
        return clientCount() > 0;
    }

    public int clientCount() {
        return clientIds.size();
    }

    public String getRandomClientId() {
        int randomClientIndex = (int) (Math.random() * clientCount());
        return clientIds.get(randomClientIndex);
    }

    public void sendMapToClients() {
        for(String id : clientIds) {
            sendMap(id);
            if(!sSettings.show_mapmaker_ui) //spawn in after finished loading
                addNetCmd(id,"cl_sendcmd respawnnetplayer " + id);
        }
    }

    public void addClient(String id) {
        clientIds.add(id);
    }
}
