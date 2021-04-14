import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.*;

public class nServer extends Thread implements fNetBase {
    private int netticks;
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
            "fireweapon"
    ));
    boolean isPlaying = false;

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
        cGameLogic.checkDisconnectStatus();
        //other players
        for(String id : clientArgsMap.keySet()) {
//            System.out.println(id);
            if(!id.equals(uiInterface.uuid) && !id.equals("server")) {
                //check currentTime vs last recorded checkin time
                long lastrecordedtime = Long.parseLong(nServer.instance().clientArgsMap.get(id).get("time"));
                if(System.currentTimeMillis() > lastrecordedtime + sVars.getInt("timeout")) {
                    nServer.instance().addQuitClient(id);
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
            if(clientArgsMap.get(id).containsKey("netcmdrcv") && clientNetCmdMap.get(id).size() > 0) {
                clientNetCmdMap.get(id).remove();
                clientArgsMap.get(id).remove("netcmdrcv");
            }
        }
    }

    void clearBots() {
        if(eManager.currentMap != null) {
            HashMap botsMap = eManager.currentMap.scene.getThingMap("THING_BOTPLAYER");
            if(sSettings.net_server && botsMap.size() > 0) {
                for(Object id : botsMap.keySet()) {
                    quitClientIds.add((String) id);
                }
            }
        }
    }

    void addBots() {
        if(sSettings.net_server) {
            int i = 0;
            while (i < sVars.getInt("botcount")) {
                xCon.ex("addbot");
                i++;
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
        //name for spectator and gameplay
        keys.put("name", sVars.get("playername"));
        //key whose presence depends on value of cvar like quitting, disconnecting
        keys.remove("quit");
        keys.remove("disconnect");
        if(cVars.isOne("quitting"))
            keys.put("quit", "");
        if(cVars.isOne("disconnecting"))
            keys.put("disconnect", "");
        //send scores
        keys.put("scoremap", cScoreboard.createSortedScoreMapStringServer());
        cVars.put("scoremap", keys.get("scoremap"));
        keys.put("scorelimit", sVars.get("scorelimit"));
        keys.put("timeleft", cVars.get("timeleft"));
        keys.put("topscore", cScoreboard.getTopScoreString());
        keys.put("state", cServer.getGameStateServer());
        keys.put("win", cVars.get("winnerid"));
        return keys;
    }

    public void processPackets() {
        try {
//            nVars.update();
            HashMap<String, String> netVars = getNetVars();
            clientArgsMap.put("server", netVars);
            if(isPlaying) {
                HashMap<String, String> keys = new HashMap<>();
                keys.put("id", uiInterface.uuid);
                //userplayer vars like coords and dirs and weapon
                keys.put("color", sVars.get("playercolor"));
                keys.put("hat", sVars.get("playerhat"));
                //name for spectator and gameplay
                keys.put("name", sVars.get("playername"));

                gPlayer userPlayer = cGameLogic.userPlayer();
                if(userPlayer != null) {
                    keys.put("x", userPlayer.get("coordx"));
                    keys.put("y", userPlayer.get("coordy"));
                    keys.put("fv", userPlayer.get("fv"));
                    keys.put("dirs", String.format("%s%s%s%s", userPlayer.get("mov0"), userPlayer.get("mov1"),
                            userPlayer.get("mov2"), userPlayer.get("mov3")));
                    keys.put("vels", String.format("%s-%s-%s-%s", userPlayer.get("vel0"), userPlayer.get("vel1"),
                            userPlayer.get("vel2"), userPlayer.get("vel3")));
                    keys.put("weapon", userPlayer.get("weapon"));
                    keys.put("stockhp", userPlayer.get("stockhp"));
                }
                if(clientArgsMap.containsKey(uiInterface.uuid)
                        && clientArgsMap.get(uiInterface.uuid).containsKey("respawntime")) {
                    keys.put("respawntime", clientArgsMap.get(uiInterface.uuid).get("respawntime"));
                }
                clientArgsMap.put(uiInterface.uuid, keys);
            }
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
            HashMap botsMap = eManager.currentMap.scene.getThingMap("THING_BOTPLAYER");
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
            if(!clientArgsMap.get(clientid).containsKey("netcmdrcv")) {
                if(clientid.contains("bot"))
                    clientArgsMap.get(clientid).put("netcmdrcv", "1");
                netVars.put("cmd", clientNetCmdMap.get(clientid).peek());
            }
        }
        sendDataString = new StringBuilder(netVars.toString()); //using sendmap doesnt work
        if(isPlaying) {
            HashMap<String, String> workingmap = new HashMap<>(clientArgsMap.get(uiInterface.uuid));
            workingmap.remove("time"); //unnecessary args for sending, but necessary to retain server-side
            workingmap.remove("respawntime"); //unnecessary args for sending, but necessary to retain server-side
            sendDataString.append(String.format("@%s", workingmap.toString()));
        }
        for(int i = 0; i < clientIds.size(); i++) {
            String idload2 = clientIds.get(i);
            if(clientArgsMap.containsKey(idload2)) {
                HashMap<String, String> workingmap = new HashMap<>(clientArgsMap.get(idload2));
                workingmap.remove("time"); //unnecessary args for sending, but necessary to retain server-side
                workingmap.remove("respawntime"); //unnecessary args for sending, but necessary to retain server-side
                sendDataString.append(String.format("@%s", workingmap.toString()));
            }
        }
        return sendDataString.toString();
    }

    void removeNetClient(String id) {
        String quitterName = nServer.instance().clientArgsMap.get(id).get("name");
        if(cVars.isVal("flagmasterid", id)) {
            cVars.put("flagmasterid", "");
            gPlayer player = gScene.getPlayerById(id);
            nServer.instance().addNetCmd(String.format("putitem ITEM_FLAG %d %d",
                    player.getInt("coordx"), player.getInt("coordy")));
        }
        clientArgsMap.remove(id);
        cScoreboard.scoresMap.remove(id);
        clientNetCmdMap.remove(id);
        eManager.currentMap.scene.playersMap().remove(id);
        clientIds.remove(id);
        //tell remaining players
        String quitString = String.format("echo %s left the game", quitterName);
        addNetCmd(quitString);
    }

    public void run() {
        try {
//            uiInterface.uuid = "server";
            serverSocket = new DatagramSocket(sVars.getInt("joinport"));
//            serverSocket.setSoTimeout(sVars.getInt("timeout"));
            while (true) {
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
                    uiInterface.networkTime = System.currentTimeMillis()
                            + (long) (1000.0 / (double) sVars.getInt("rateserver"));
                    sleep(Math.max(0, uiInterface.networkTime - uiInterface.gameTime));
                } catch (Exception e) {
                    eUtils.echoException(e);
                    e.printStackTrace();
                }
            }
        }
        catch (IOException ee) {
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
            HashMap<String, HashMap<String, Integer>> scoresMap = cScoreboard.scoresMap;
            String packId = packArgMap.get("id");
            String packName = packArgMap.get("name");
            if(packId == null)
                return;
            //insert new ids into the greater maps
            if(!scoresMap.containsKey(packId))
                cScoreboard.addId(packId);
            if(!clientArgsMap.containsKey(packId)) {
                clientArgsMap.put(packId, packArgMap);
                handleNewClientJoin(packId, packName);
            }
            //fetch old packet
            HashMap<String, String> oldArgMap = clientArgsMap.get(packId);
            String oldName = "";
            long oldTimestamp = 0;
            if(oldArgMap != null) {
                oldName = oldArgMap.get("name");
                oldTimestamp = oldArgMap.containsKey("time") ?
                        Long.parseLong(oldArgMap.get("time")) : System.currentTimeMillis();
            }
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
                scoresMap.get(packId).put("ping", (int) Math.abs(System.currentTimeMillis() - oldTimestamp));
                //handle name change to notify
                if(packName != null && oldName != null && oldName.length() > 0 && !oldName.equals(packName))
                    addNetCmd(String.format("echo %s changed name to %s", oldName, packName));
                if(System.currentTimeMillis() > oldTimestamp + sVars.getInt("timeout")) {
                    quitClientIds.add(packId);
                }
                gPlayer packPlayer = gScene.getPlayerById(packId);
                if(packPlayer != null) {
//                    if(packPlayer.getInt("weapon") != packWeap)
//                        xCon.ex("giveweapon " + packId + " " + packWeap);
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
                    clientArgsMap.get(packId).put("stockhp", gScene.getPlayerById(packId).get("stockhp"));
                }
                if(packArgMap.containsKey("quit") || packArgMap.containsKey("disconnect")) {
                    quitClientIds.add(packId);
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

    void changeMap(String mapPath) {
        System.out.println("CHANGING MAP: " + mapPath);
        clearBots();
        oDisplay.instance().clearAndRefresh();
        cVars.put("botbehavior", "");
        if(!mapPath.contains(sVars.get("datapath")))
            mapPath = eUtils.getPath(mapPath);
        gMap.load(mapPath);
        oDisplay.instance().createPanels();
        addExcludingNetCmd("server", "cv_maploaded 0;load ");
        eManager.currentMap.scene.clearPlayers();
        for(String id : clientIds) {
            createServersidePlayerAndSendMap(id, clientArgsMap.get(id).get("name"));
            if(gScene.getPlayerById(id) != null)
                addNetCmd(id, "cv_maploaded 1;respawn");
            else
                addNetCmd(id, "createuserplayer;cv_maploaded 1;respawn");
        }
    }

    private void handleNewClientJoin(String packId, String packName) {
        System.out.println("NEW CLIENT: "+packId);
        clientIds.add(packId);
        clientNetCmdMap.put(packId, new LinkedList<>());
        createServersidePlayerAndSendMap(packId, packName);
        addNetCmd(packId, "cv_maploaded 1;respawn");
        addNetCmd(String.format("echo %s joined the game", packName));
    }

    public void createServersidePlayer(String packId, String packName) {
        gPlayer player = new gPlayer(-6000, -6000,150,150,
                eUtils.getPath("animations/player_red/a03.png"));
        player.put("name", packName);
        player.putInt("tag", eManager.currentMap.scene.playersMap().size());
        player.put("id", packId);
        player.put("stockhp", cVars.get("maxstockhp"));
//          player.putInt("weapon", packWeap);
        eManager.currentMap.scene.playersMap().put(packId, player);
    }

    private void createServersidePlayerAndSendMap(String packId, String packName) {
        if(!packId.contains("bot")) {
            gPlayer player = new gPlayer(-6000, -6000,150,150,
                    eUtils.getPath("animations/player_red/a03.png"));
            player.put("name", packName);
            player.putInt("tag", eManager.currentMap.scene.playersMap().size());
            player.put("id", packId);
            player.put("stockhp", cVars.get("maxstockhp"));
//          player.putInt("weapon", packWeap);
            eManager.currentMap.scene.playersMap().put(packId, player);
        }
        StringBuilder sendStringBuilder = new StringBuilder();
        int linectr = 0;
        for(String line : eManager.currentMap.mapLines) {
            sendStringBuilder.append(line.replace("cmd", "")).append(";");
            linectr++;
            if(linectr%cVars.getInt("serversendmapbatchsize") == 0
                    || linectr == eManager.currentMap.mapLines.size()) {
                String sendString = sendStringBuilder.toString();
                addNetCmd(packId, sendString.substring(0, sendString.lastIndexOf(';')));
                sendStringBuilder = new StringBuilder();
            }
        }
    }

    private void handleClientMessage(String msg) {
        addNetCmd("echo " + msg);
        //handle special sounds
        String testmsg = msg.substring(msg.indexOf(':')+2);
        checkMessageForSpecialSound(testmsg);
        checkMessageForVoteToSkip(testmsg);
    }

    private void handleClientCommand(String id, String cmd) {
        String ccmd = cmd.split(" ")[0];
        if(legalClientCommands.contains(ccmd)) {
            if(ccmd.contains("fireweapon")) { //handle special case for weapons
                addExcludingNetCmd(id, cmd);
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
                addNetCmd(soundString);
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
                cVars.put("timeleft", "0");
                for(String s : new String[]{
                        String.format("echo [VOTE_SKIP] VOTE TARGET REACHED (%s)", cVars.get("voteskiplimit")),
                        "echo [VOTE_SKIP] CHANGING MAP..."}) {
                    addNetCmd(s);
                }
            }
            else {
                String sendmsg = String.format("echo [VOTE_SKIP] SAY 'skip' TO END ROUND. (%s/%s)",
                        cVars.get("voteskipctr"), cVars.get("voteskiplimit"));
                addNetCmd(sendmsg);
            }
        }
    }

    public void disconnect() {
        if(eManager.currentMap.scene.getThingMap("THING_PLAYER").size() < 2) {
            sSettings.net_server = false;
            sSettings.NET_MODE = sSettings.NET_OFFLINE;
            if(isAlive())
                interrupt();
//                serverSocket.close();
            xCon.ex("load " + sVars.get("defaultmap"));
            if (uiInterface.inplay)
                xCon.ex("pause");
            cVars.put("disconnecting", "0");
        }
    }
}
