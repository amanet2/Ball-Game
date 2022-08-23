import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.*;

public class nServer extends Thread {
    private static final int sendbatchsize = 320;
    private static final int timeout = 10000;
    private final Queue<DatagramPacket> receivedPackets = new LinkedList<>();
    private final Queue<String> quitClientIds = new LinkedList<>(); //temporarily holds ids that are quitting
    HashMap<String, Long> banIds = new HashMap<>(); // ids mapped to the time to be allowed back
    //NEW --
    //--
    nStateMap masterStateMap; //will be the source of truth for game state including passed messages and comms
    HashMap<String, Queue<String>> clientNetCmdMap = new HashMap<>(); //id maps to queue of cmds to be sent
    private final HashMap<String, String> clientCheckinMap; //track the timestamp of last received packet of a client
    //**
    // preserve the masterstate (as a string) at last client checkin.
    // use this to compute deltas when sending state to clients
    // *//
    private final HashMap<String, String> clientStateSnapshots;
    // OLD --
    //--
    //manage variables for use in the network game, sync to-and-from the actual map and objects
    HashMap<String, HashMap<String, String>> clientArgsMap = new HashMap<>(); //server too, index by uuids
    //
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
            "setthing",
            "exec",
            "fireweapon",
            "putblock",
            "putitem",
            "requestdisconnect",
            "setnstate"
    ));

    public static nServer instance() {
        if(instance == null)
            instance = new nServer();
        return instance;
    }

    private nServer() {
        masterStateMap = new nStateMap();
        clientCheckinMap = new HashMap<>();
        clientStateSnapshots = new HashMap<>();
        clientCmdDoables.put("fireweapon",
                new gDoableCmd() {
                    void ex(String id, String cmd) {
                        addExcludingNetCmd(id+",server,",
                                cmd.replaceFirst("fireweapon", "cl_fireweapon"));
                        xCon.ex(cmd);
                    }
                });
        clientCmdDoables.put("setnstate",
                new gDoableCmd() {
                    void ex(String id, String cmd) {
                        xCon.ex(cmd);
                    }
                });
        clientCmdDoables.put("requestdisconnect",
                new gDoableCmd() {
                    void ex(String id, String cmd) {
                        quitClientIds.add(id);
                    }
                });

        for(String rcs : new String[]{"putblock", "putitem", "deleteblock", "deleteitem", "deleteprefab", "setthing"}) {
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

    public void checkForUnhandledQuitters() {
        //other players
        for(String id : clientCheckinMap.keySet()) {
            long pt = Long.parseLong(clientCheckinMap.get(id));
            if(gTime.gameTime > pt + timeout)
                quitClientIds.add(id);
        }

        while(quitClientIds.size() > 0) {
            removeNetClient(quitClientIds.remove());
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
                readData(receiveDataString); //and respond too
                //get player id of client
                HashMap<String, String> clientmap = nVars.getMapFromNetString(receiveDataString);
                String clientId = clientmap.get("id");
                if(clientId != null) {
                    //create response
                    String sendDataString = createSendDataString(netVars, clientId);
                    byte[] sendData = sendDataString.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, addr, port);
                    serverSocket.send(sendPacket);
                    xCon.instance().debug("SERVER_SEND_" + clientId + " [" + sendDataString.length() + "]: " + sendDataString);
                    if(sendDataString.length() > sSettings.max_packet_size)
                        System.out.println("*WARNING* PACKET LENGTH EXCEED " + sSettings.max_packet_size + " BYTES: "
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

    private String createSendDataString(HashMap<String, String> netVars, String clientid) {
        HashMap<String, HashMap<String, String>> sendDataMap = new HashMap<>();
        if(clientNetCmdMap.containsKey(clientid) && clientNetCmdMap.get(clientid).size() > 0)
            netVars.put("cmd", clientNetCmdMap.get(clientid).peek());
        sendDataMap.put("server", new HashMap<>(netVars)); //add server map first
        //NEW --
        //--
        //fetch old snapshot for client
        nStateMap deltaStateMap = new nStateMap(clientStateSnapshots.get(clientid)).getDelta(masterStateMap);
        //add server vars to the sending map
        deltaStateMap.put("server", new nState());
        for(String k : netVars.keySet()) {
            deltaStateMap.get("server").put(k, netVars.get(k));
        }
        if(!clientid.equals(uiInterface.uuid))
            System.out.println(deltaStateMap.toString().replace(", ", ","));
        return deltaStateMap.toString().replace(", ", ",");
    }

    void removeNetClient(String id) {
        //NEW
        //--
        String qn = masterStateMap.get(id).get("name");
        String qc = masterStateMap.get(id).get("color");
        clientCheckinMap.remove(id);
        masterStateMap.remove(id);
        clientNetCmdMap.remove(id);
        clientStateSnapshots.remove(id);
        gScoreboard.scoresMap.remove(id);
        cServerLogic.scene.getThingMap("THING_PLAYER").remove(id);
        addExcludingNetCmd("server", String.format("echo %s#%s left the game", qn, qc));
        //OLD
        //--
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
//        clientArgsMap.remove(id);
    }

    public void run() {
        try {
            serverSocket = new DatagramSocket(cServerLogic.listenPort);
            while (sSettings.IS_SERVER) {
                try {
                    long gameTime = gTime.gameTime;
                    byte[] receiveData = new byte[sSettings.rcvbytesserver];
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    serverSocket.receive(receivePacket);
                    receivedPackets.add(receivePacket);
                    long networkTime = gameTime + (long) (1000.0 / (double) sSettings.rateserver);
                    processPackets(gameTime);
//                    checkIfClientAckedCommand();
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
        return masterStateMap.contains(id);
    }

    public void handleJoin(String id) {
        masterStateMap.put(id, new nStateBallGame());
        clientNetCmdMap.put(id, new LinkedList<>());
        gScoreboard.addId(id);
        sendMap(id);
        if(!sSettings.show_mapmaker_ui) //spawn in after finished loading
            xCon.ex("exec scripts/respawnnetplayer " + id);
        for(String clientId : masterStateMap.keys()) {
            gThing player = cServerLogic.scene.getPlayerById(clientId);
            if(clientId.equals(id) || player == null)
                continue;
            addNetCmd(id, String.format("cl_spawnplayer %s %s %s", clientId,
                    player.get("coordx"), player.get("coordy")));
        }
        addExcludingNetCmd("server", String.format("echo %s#%s joined the game",
                masterStateMap.get(id).get("name"), masterStateMap.get(id).get("color")));
    }

    public void checkBanStatus(String stateId) {
        if(banIds.containsKey(stateId)) {
            if(banIds.get(stateId) < gTime.gameTime)
                banIds.remove(stateId);
            else {
                addNetCmd(stateId, "echo You are banned for " + (banIds.get(stateId) - gTime.gameTime) + "ms");
                addNetCmd(stateId, "disconnect");
            }
        }
    }

    public void readData(String receiveDataString) {
        String toks = receiveDataString.trim();
        if(toks.length() > 0) {
            // ----
            //------ NEW STATES
            //load received string into state object
            nState receivedState = new nState(receiveDataString.trim());
            String stateId = receivedState.get("id");
            //relieve bans
            checkBanStatus(stateId);
            //check if masterState contains
            if(!masterStateMap.contains(stateId))
                handleJoin(stateId);
            //record checkin time for client
            clientCheckinMap.put(stateId, Long.toString(gTime.gameTime));
            //record the master state at last communication time
            clientStateSnapshots.put(stateId, masterStateMap.toString());
            //compare received state to what we have kept in master. this will load the diff into master state
            nState deltaState = receivedState.getDelta(masterStateMap.get(stateId));
            //load the keys from delta into our state map
            for(String k : deltaState.keys()) {
                masterStateMap.get(stateId).put(k, deltaState.get(k));
            }
            //update players
            gPlayer pl = cServerLogic.getPlayerById(stateId);
            if(pl != null) {
                if (sSettings.smoothing) {
                    pl.put("coordx", masterStateMap.get(stateId).get("x"));
                    pl.put("coordy", masterStateMap.get(stateId).get("y"));
                }
                //store player object's health in outgoing network arg map
                masterStateMap.get(stateId).put("hp", cServerLogic.getPlayerById(stateId).get("stockhp"));
            }
//            //record the master state at last communication time
//            clientStateSnapshots.put(stateId, masterStateMap.toString());
//            System.out.println(masterStateMap);
//            System.out.println(clientState.keys());
            // ----- OLD BELOW
            // ----
            HashMap<String, String> packArgMap = nVars.getMapFromNetString(toks);
            String packId = packArgMap.get("id");
            String packName = packArgMap.get("name");
            if(packId == null)
                return;
//            if(!clientArgsMap.containsKey(packId)) {
//                clientArgsMap.put(packId, packArgMap);
//                handleNewClientJoin(packId, packName);
//            }
            //only want to update keys that have changes
//            for(String k : packArgMap.keySet()) {
//                clientArgsMap.get(packId).put(k, packArgMap.get(k));
//            }
            //parse and process the args from client packet
            if(hasClient(packId)) {
//                gPlayer packPlayer = cServerLogic.getPlayerById(packId);
//                if(packPlayer != null) {
//                    if (sSettings.smoothing) {
//                        packPlayer.put("coordx", masterStateMap.get(packId).get("x"));
//                        packPlayer.put("coordy", masterStateMap.get(packId).get("y"));
//                    }
//                    //store player object's health in outgoing network arg map
//                    masterStateMap.get(packId).put("hp", cServerLogic.getPlayerById(packId).get("stockhp"));
//                }
                //store player's wins and scores
                masterStateMap.get(packId).put("score",  String.format("%d:%d",
                        gScoreboard.scoresMap.get(packId).get("wins"),
                        gScoreboard.scoresMap.get(packId).get("score")));
//                if(packArgMap.get("px") != null)
//                    clientArgsMap.get(packId).put("px", packArgMap.get("px"));
//                if(packArgMap.get("py") != null)
//                    clientArgsMap.get(packId).put("py", packArgMap.get("py"));
//                if(packArgMap.get("pw") != null)
//                    clientArgsMap.get(packId).put("pw", packArgMap.get("pw"));
//                if(packArgMap.get("ph") != null)
//                    clientArgsMap.get(packId).put("ph", packArgMap.get("ph"));
            }
        }
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

    void handleClientCommand(String id, String cmd) {
        String ccmd = cmd.split(" ")[0];
//        System.out.println("FROM_" + id + ": " + cmd);
        if(legalClientCommands.contains(ccmd)) {
            if(ccmd.equals("exec"))
                System.out.println("CLIENT REQ EXEC: " + cmd);
            if(clientCmdDoables.containsKey(ccmd))
                clientCmdDoables.get(ccmd).ex(id, cmd);
            else if(cmd.startsWith("exec prefabs/")) {
                xCon.ex(cmd);
                addExcludingNetCmd("server", String.format("%s",
                        cmd.replace("exec ", "cl_exec ")));
            }
            else if(cmd.startsWith("exec scripts/respawnnetplayer")) {
                xCon.ex(cmd);
            }
            else
                addNetCmd(id, "echo NO HANDLER FOUND FOR CMD: " + cmd);
        }
        else
            addNetCmd(id, "echo ILLEGAL CMD REQUEST: " + cmd);
    }

    void checkMessageForSpecialSound(String testmsg) {
        for(String s : eManager.winSoundFileSelection) {
            String[] ttoks = s.split("\\.");
            if(testmsg.equalsIgnoreCase(ttoks[0])) {
                String soundString = "playsound sounds/win/" + s;
                addExcludingNetCmd("server", soundString);
                break;
            }
        }
    }

    public void checkClientMessageForVoteSkip(String id, String testmsg) {
        //handle the vote-to-skip function
        testmsg = testmsg.strip();
        if(testmsg.equalsIgnoreCase("skip")) {
            if(!voteSkipMap.containsKey(id)) {
                voteSkipMap.put(id,"1");
                if(voteSkipMap.keySet().size() >= cServerLogic.voteskiplimit) {
                    for(String s : new String[]{
                            "playsound sounds/win/"+eManager.winSoundFileSelection[
                                    (int) (Math.random() * eManager.winSoundFileSelection.length)],
                            String.format("echo [VOTE_SKIP] VOTE TARGET REACHED (%s)", cServerLogic.voteskiplimit),
                            "echo changing map..."}) {
                        addExcludingNetCmd("server", s);
                    }
                    cServerLogic.timedEvents.put(Long.toString(gTime.gameTime + cServerLogic.intermissionDelay),
                        new gTimeEvent() {
                            //change map after game over
                            public void doCommand() {
                                xCon.ex("changemaprandom");
                            }
                        }
                    );
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

    public void sendMapToClients() {
        for(String id : masterStateMap.keys()) {
            sendMap(id);
            xCon.ex("exec scripts/respawnnetplayer " + id);
        }
    }
}
