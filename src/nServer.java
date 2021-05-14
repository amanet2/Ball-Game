import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.*;

public class nServer extends Thread {
    private int netticks;
    Queue<DatagramPacket> receivedPackets = new LinkedList<>();
    private Queue<String> quitClientIds = new LinkedList<>(); //temporarily holds ids that are quitting
    HashMap<String, Long> banIds = new HashMap<>(); // ids mapped to the time to be allowed back
    ArrayList<String> clientIds = new ArrayList<>(); //insertion-ordered list of client ids
    //manage variables for use in the network game, sync to-and-from the actual map and objects
    HashMap<String, HashMap<String, String>> clientArgsMap = new HashMap<>(); //server too, index by uuids
    //id maps to queue of cmds we want to run on that client
    HashMap<String, Queue<String>> clientNetCmdMap = new HashMap<>();
    //queue for holding local cmds that the server user should run
    private Queue<String> serverLocalCmdQueue = new LinkedList<>();
    private static nServer instance = null;    //singleton-instance
    private DatagramSocket serverSocket = null;    //socket object
    //VERY IMPORTANT LIST. whats allowed to be done by the clients
    private static final ArrayList<String> legalClientCommands = new ArrayList<>(Arrays.asList(
            "fireweapon",
            "deleteplayer",
            "respawnnetplayer",
            "requestdisconnect",
            "exec",
            "putitem",
            "deleteblock",
            "deletecollision",
            "deleteitem"
    ));

    public static nServer instance() {
        if(instance == null)
            instance = new nServer();
        return instance;
    }

    private nServer() {
        netticks = 0;
    }

    public void addQuitClient(String id) {
        quitClientIds.add(id);
    }

    public void checkForUnhandledQuitters() {
        //other players
        for(String id : clientArgsMap.keySet()) {
//            System.out.println(id);
            if(!id.equals("server")) {
                //check currentTime vs last recorded checkin time
                long lastrecordedtime = Long.parseLong(clientArgsMap.get(id).get("time"));
                if(System.currentTimeMillis() > lastrecordedtime + sVars.getInt("timeout")) {
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
        System.out.println("ID_"+id+" "+cmd);
        if(id.equalsIgnoreCase("server"))
            serverLocalCmdQueue.add(cmd);
        else
            addNetSendData(clientNetCmdMap, id, cmd);
    }

    public void addNetCmd(String cmd) {
        System.out.println("ALL_CLIENTS: " + cmd);
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

    public HashMap<String, String> getNetVars() {
        HashMap<String, String> keys = new HashMap<>();
        //handle outgoing cmd
        keys.put("cmd", "");
        //handle server outgoing cmds that loopback to the server
        checkLocalCmds();
        //update id in net args
        keys.put("id", "server");
        //send scores
        keys.put("scoremap", gScoreboard.createSortedScoreMapStringServer());
        cVars.put("scoremap", keys.get("scoremap"));
        keys.put("timeleft", cVars.get("timeleft"));
//        keys.put("topscore", gScoreboard.getTopScoreString());
        if(clientArgsMap.containsKey("server")) {
            for(String s : new String[]{"flagmasterid", "virusids"}) {
                if(clientArgsMap.get("server").containsKey(s))
                    keys.put(s, clientArgsMap.get("server").get(s));
            }
        }
        return keys;
    }

    public void processPackets() {
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
                if(banIds.containsKey(clientId) && banIds.get(clientId) < System.currentTimeMillis())
                    banIds.remove(clientId);
                if(banIds.containsKey(clientId)) {
                    addNetCmd(clientId, "echo You are banned for "
                            + (banIds.get(clientId) - System.currentTimeMillis()) + "ms");
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
                }
                receivedPackets.remove();
            }
            HashMap botsMap = cServerLogic.scene.getThingMap("THING_BOTPLAYER");
            if(botsMap.size() > 0 && cVars.getLong("bottime") < uiInterface.gameTime) {
                cVars.putLong("bottime",
                        uiInterface.gameTime + (long)(1000.0/(double)sVars.getInt("ratebots")));
                for(Object id : botsMap.keySet()) {
                    gPlayer p = (gPlayer) botsMap.get(id);
                    nVarsBot.update(p);
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

    private String createSendDataString(HashMap<String, String> netVars, String clientid) {
        StringBuilder sendDataString;
        if(clientid.length() > 0 && clientNetCmdMap.containsKey(clientid)
                && clientNetCmdMap.get(clientid).size() > 0 && clientArgsMap.containsKey(clientid)) {
            //act as if bot has instantly received outgoing cmds (bots dont have a "client" to exec things on)
            if(!clientArgsMap.get(clientid).containsKey("cmdrcv")) {
                if(clientid.contains("bot"))
                    clientArgsMap.get(clientid).put("cmdrcv", "1");
                netVars.put("cmd", clientNetCmdMap.get(clientid).peek());
            }
        }
        sendDataString = new StringBuilder(netVars.toString()); //using sendmap doesnt work
        for(int i = 0; i < clientIds.size(); i++) {
            String idload2 = clientIds.get(i);
            if(clientArgsMap.containsKey(idload2)) {
                HashMap<String, String> workingmap = new HashMap<>(clientArgsMap.get(idload2));
                workingmap.remove("time"); //unnecessary args for sending, but necessary to retain server-side
                workingmap.remove("respawntime"); //unnecessary args for sending, but necessary to retain server-side
                sendDataString.append(String.format("@%s", workingmap.toString()));
            }
        }
        return sendDataString.toString().replace(", ", ","); //replace to save 1 byte per field
    }

    void removeNetClient(String id) {
        String quitterName = clientArgsMap.get(id).get("name");
        if(clientArgsMap.containsKey("server") && clientArgsMap.get("server").containsKey("flagmasterid")
                && clientArgsMap.get("server").get("flagmasterid").equals(id)) {
            clientArgsMap.get("server").put("flagmasterid", "");
            gPlayer player = cServerLogic.getPlayerById(id);
            addNetCmd(String.format("putitem ITEM_FLAG %d %d",
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
            serverSocket = new DatagramSocket(sVars.getInt("joinport"));
            while (sSettings.IS_SERVER) {
                try {
                    netticks++;
                    if (uiInterface.nettickcounterTime < uiInterface.gameTime) {
                        uiInterface.netReport = netticks;
                        netticks = 0;
                        uiInterface.nettickcounterTime = uiInterface.gameTime + 1000;
                    }
                    byte[] receiveData = new byte[sVars.getInt("rcvbytesserver")];
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    serverSocket.receive(receivePacket);
                    receivedPackets.add(receivePacket);
                    long networkTime = System.currentTimeMillis()
                            + (long) (1000.0 / (double) sVars.getInt("rateserver"));
                    processPackets();
                    checkOutgoingCmdMap();
                    checkForUnhandledQuitters();
                    sleep(Math.max(0, networkTime - uiInterface.gameTime));
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
        String[] toks = receiveDataString.trim().split("@");
        if(toks[0].length() > 0) {
            String argload = toks[0];
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
                if(!clientArgsMap.get(packId).containsKey(k)
                        || !clientArgsMap.get(packId).get(k).equals(packArgMap.get(k))) {
                    clientArgsMap.get(packId).put(k, packArgMap.get(k));
                }
            }
            //record time we last updated client args
            clientArgsMap.get(packId).put("time", Long.toString(System.currentTimeMillis()));
            //parse and process the args from client packet
            if(clientIds.contains(packId)) {
                //update ping
//                scoresMap.get(packId).put("ping", (int) Math.abs(System.currentTimeMillis() - oldTimestamp));
                //handle name change to notify
                if(packName != null && oldName != null && oldName.length() > 0 && !oldName.equals(packName))
                    addNetCmd(String.format("echo %s changed name to %s", oldName, packName));
                gPlayer packPlayer = cServerLogic.getPlayerById(packId);
                if(packPlayer != null) {
                    if (clientArgsMap.get(packId).containsKey("vels")) {
                        String[] veltoks = clientArgsMap.get(packId).get("vels").split("-");
                        packPlayer.put("vel0", veltoks[0]);
                        packPlayer.put("vel1", veltoks[1]);
                        packPlayer.put("vel2", veltoks[2]);
                        packPlayer.put("vel3", veltoks[3]);
                    }
                    if (sVars.isOne("smoothing")) {
                        packPlayer.put("coordx", clientArgsMap.get(packId).get("x"));
                        packPlayer.put("coordy", clientArgsMap.get(packId).get("y"));
                    }
                    if(clientArgsMap.get(packId).containsKey("fv")) {
                        packPlayer.putDouble("fv", Double.parseDouble(clientArgsMap.get(packId).get("fv")));
                    }
                    //store player object's health in outgoing network arg map
                    clientArgsMap.get(packId).put("stockhp", cServerLogic.getPlayerById(packId).get("stockhp"));
                }
                if(packArgMap.get("msg") != null && packArgMap.get("msg").length() > 0) {
                    handleClientMessage(packArgMap.get("msg"));
                }
                if(packArgMap.get("cmd") != null && packArgMap.get("cmd").length() > 0) {
                    handleClientCommand(packId, packArgMap.get("cmd"));
                }
            }
        }
    }

    private void handleNewClientJoin(String packId, String packName) {
        System.out.println("NEW CLIENT: "+packId);
        clientIds.add(packId);
        clientNetCmdMap.put(packId, new LinkedList<>());
        sendMap(packId);
        addNetCmd(packId, "cv_maploaded 1");
        for(String clientId : clientIds) {
            gThing player = cServerLogic.scene.getPlayerById(clientId);
            if(player != null)
                addNetCmd(packId, String.format("spawnplayerclient %s %s %s",
                    clientId, player.get("coordx"), player.get("coordy")));
        }
        if(!sSettings.show_mapmaker_ui)
            xCon.ex(String.format("respawnnetplayer %s", packId));
        addExcludingNetCmd("server", String.format("echo %s joined the game", packName));
    }

    public void sendMap(String packId) {
        //these three are always here
        ArrayList<String> maplines = new ArrayList<>();
        maplines.add(String.format("cv_maploaded 0;cv_gamemode %s\n", cVars.get("gamemode")));
        HashMap<String, gThing> blockMap = cServerLogic.scene.getThingMap("THING_BLOCK");
        for(String id : blockMap.keySet()) {
            gBlock block = (gBlock) blockMap.get(id);
            String[] args = new String[]{
                    block.get("type"),
                    block.get("coordx"),
                    block.get("coordy"),
                    block.get("dimw"),
                    block.get("dimh"),
                    block.get("toph"),
                    block.get("wallh"),
                    block.get("color"),
                    block.get("colorwall"),
                    block.get("frontwall"),
                    block.get("backtop")
            };
            String prefabString = "";
            if(block.contains("prefabid")) {
                prefabString = "cv_prefabid " + block.get("prefabid") +";";
//                maplines.add(prefabString);
            }
            StringBuilder blockString = new StringBuilder("putblock");
            for(String arg : args) {
                if(arg != null) {
                    blockString.append(" ").append(arg);
                }
            }
//            maplines.add(blockString.toString());
            maplines.add(prefabString + blockString.toString());
        }
        HashMap<String, gThing> collisionMap = cServerLogic.scene.getThingMap("THING_COLLISION");
        for(String id : collisionMap.keySet()) {
            gCollision collision = (gCollision) collisionMap.get(id);
            StringBuilder xString = new StringBuilder();
            StringBuilder yString = new StringBuilder();
            for(int i = 0; i < collision.xarr.length; i++) {
                int coordx = collision.xarr[i];
                xString.append(coordx).append(".");
            }
            xString = new StringBuilder(xString.substring(0, xString.lastIndexOf(".")));
            for(int i = 0; i < collision.yarr.length; i++) {
                int coordy = collision.yarr[i];
                yString.append(coordy).append(".");
            }
            yString = new StringBuilder(yString.substring(0, yString.lastIndexOf(".")));
            String[] args = new String[]{
                    xString.toString(),
                    yString.toString(),
                    Integer.toString(collision.npoints)
            };
            String prefabString = "";
            if(collision.contains("prefabid")) {
                prefabString = "cv_prefabid " + collision.get("prefabid");
                maplines.add(prefabString);
            }
            StringBuilder str = new StringBuilder("putcollision");
            for(String arg : args) {
                if(arg != null) {
                    str.append(" ").append(arg);
                }
            }
            maplines.add(str.toString());
        }
        HashMap<String, gThing> itemMap = cServerLogic.scene.getThingMap("THING_ITEM");
        for(String id : itemMap.keySet()) {
            gItem item = (gItem) itemMap.get(id);
            String[] args = new String[]{
                    item.get("type"),
                    item.get("coordx"),
                    item.get("coordy")
            };
            StringBuilder str = new StringBuilder("putitem");
            for(String arg : args) {
                if(arg != null) {
                    str.append(" ").append(arg);
                }
            }
            maplines.add(str.toString());
        }
        HashMap<String, gThing> flareMap = cServerLogic.scene.getThingMap("THING_FLARE");
        for(String id : flareMap.keySet()) {
            gFlare flare = (gFlare) flareMap.get(id);
            String[] args = new String[]{
                    flare.get("coordx"),
                    flare.get("coordy"),
                    flare.get("dimw"),
                    flare.get("dimh"),
                    flare.get("r1"),
                    flare.get("g1"),
                    flare.get("b1"),
                    flare.get("a1"),
                    flare.get("r2"),
                    flare.get("g2"),
                    flare.get("b2"),
                    flare.get("a2")
            };
            StringBuilder str = new StringBuilder("putflare");
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
        for(String line : maplines) {
            sendStringBuilder.append(line).append(";");
            linectr++;
            if(linectr%cVars.getInt("serversendmapbatchsize") == 0
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
        checkMessageForVoteToSkip(testmsg);
    }

    private void handleClientCommand(String id, String cmd) {
        String ccmd = cmd.split(" ")[0];
        System.out.println("FROM_CLIENT_" + id + ": " + cmd);
        if(legalClientCommands.contains(ccmd)) {
            if(ccmd.contains("fireweapon")) { //handle special case for weapons
                addExcludingNetCmd(id+",server,",
                        cmd.replaceFirst("fireweapon", "fireweaponclient"));
                xCon.ex(cmd);
            }
            else if(ccmd.contains("requestdisconnect")) {
                quitClientIds.add(id);
            }
            else if(ccmd.contains("deleteplayer") || ccmd.contains("respawnnetplayer")) {
                String[] toks = cmd.split(" ");
                if(toks.length > 1) {
                    String reqid = toks[1];
                    if(reqid.equals(id)) { //client can only remove itself
                        addExcludingNetCmd(id, cmd);
                    }
                }
            }
            else if(cmd.contains("exec prefabs/")) {
                int prefabid = cServerLogic.scene.getHighestPrefabId() + 1;
                addExcludingNetCmd(uiInterface.uuid, String.format("cv_prefabid %d;%s", prefabid, cmd));
            }
            else if(ccmd.contains("putitem")) {
                int itemid = cServerLogic.scene.getHighestItemId() + 1;
                addExcludingNetCmd(uiInterface.uuid, String.format("cv_itemid %d;%s", itemid, cmd));
            }
            else
                addNetCmd(cmd);
        }
        else {
            System.out.println("ILLEGAL COMMAND FROM CLIENT: " + cmd);
        }
    }

    void checkMessageForSpecialSound(String testmsg) {
        for(String s : eManager.winClipSelection) {
            String[] ttoks = s.split("\\.");
            if(testmsg.equalsIgnoreCase(ttoks[0])) {
                String soundString = "playsound sounds/win/" + s;
                addExcludingNetCmd("server", soundString);
                break;
            }
        }
    }

    void checkMessageForVoteToSkip(String testmsg) {
        //handle the vote-to-skip function
        testmsg = testmsg.strip();
        if(testmsg.equalsIgnoreCase("skip")) {
            cVars.addIntVal("voteskipctr", 1);
            if(cVars.getInt("voteskipctr") >= cVars.getInt("voteskiplimit")) {
                cVars.putLong("intermissiontime",
                        System.currentTimeMillis() + sVars.getInt("intermissiontime"));
                for(String s : new String[]{
                        "playsound sounds/win/"+eManager.winClipSelection[
                                (int) (Math.random() * eManager.winClipSelection.length)],
                        String.format("echo [VOTE_SKIP] VOTE TARGET REACHED (%s)", cVars.get("voteskiplimit")),
                        "echo [VOTE_SKIP] CHANGING MAP IN 10s..."}) {
                    addExcludingNetCmd("server", s);
                }
            }
            else {
                String s = String.format("echo [VOTE_SKIP] SAY 'skip' TO END ROUND. (%s/%s)",
                        cVars.get("voteskipctr"), cVars.get("voteskiplimit"));
                addExcludingNetCmd("server", s);
            }
        }
    }
}
