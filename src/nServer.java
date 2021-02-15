import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class nServer extends Thread {
    private int netticks;
    static Queue<String> quitClientIds = new LinkedList<>(); //temporarily holds ids that are quitting
    static HashMap<String, Long> banClientIds = new HashMap<>(); // ids mapped to the time to be allowed back
    static ArrayList<String> clientIds = new ArrayList<>(); //insertion-ordered list of client ids
    //manage variables for use in the network game, sync to-and-from the actual map and objects
    static HashMap<String, HashMap<String, String>> clientArgsMap = new HashMap<>(); //server too, index by uuids
    //id maps to queue of cmds we want to run on that client
    static HashMap<String, Queue<String>> clientSendCmdQueues = new HashMap<>();
    //id maps to queue of msgs we want to send to clients
    static HashMap<String, Queue<String>> clientSendMsgQueues = new HashMap<>();
    private static Queue<DatagramPacket> receivedPackets = new LinkedList<>();
    private static nServer instance = null;
    private static DatagramSocket serverSocket = null;


    public static nServer instance() {
        if(instance == null)
            instance = new nServer();
        return instance;
    }

    private nServer() {
        netticks = 0;
    }

    public static void addSendMsg(String id, String msg) {
        addNetSendData(clientSendMsgQueues, id, msg);
    }

    public static void addSendMsg(String msg) {
        addNetSendData(clientSendMsgQueues, msg);
    }

    public static void addSendCmd(String id, String cmd) {
        addNetSendData(clientSendCmdQueues, id, cmd);
    }

    public static void addSendCmd(String cmd) {
        addNetSendData(clientSendCmdQueues, cmd);
    }

    private static void addNetSendData(HashMap<String, Queue<String>> sendMap, String id, String data) {
        if(!sendMap.containsKey(id))
            sendMap.put(id, new LinkedList<>());
        sendMap.get(id).add(data);
    }

    private static void addNetSendData(HashMap<String, Queue<String>> sendMap, String data) {
        for(String id: sendMap.keySet()) {
            addNetSendData(sendMap, id, data);
        }
    }

    public static void clearBots() {
        if(eManager.currentMap != null) {
            HashMap botsMap = eManager.currentMap.scene.getThingMap("THING_BOTPLAYER");
            if(sSettings.net_server && botsMap.size() > 0) {
                for(Object id : botsMap.keySet()) {
                    nServer.quitClientIds.add((String) id);
                }
            }
        }
    }

    public static void addBots() {
        if(sSettings.net_server) {
            int i = 0;
            while (i < sVars.getInt("botcount")) {
                xCon.ex("addbot");
                i++;
            }
        }
    }

    public static void processPackets() {
        try {
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
                if(banClientIds.containsKey(clientId) && banClientIds.get(clientId) < System.currentTimeMillis())
                    banClientIds.remove(clientId);
                if(clientId != null && !banClientIds.containsKey(clientId)) {
                    nSend.focus_id = clientId;
                    //create response
                    String sendDataString = createSendDataString();
                    byte[] sendData = sendDataString.getBytes();
                    DatagramPacket sendPacket =
                            new DatagramPacket(sendData, sendData.length, addr, port);
                    serverSocket.send(sendPacket);
                    xCon.instance().debug("SERVER SND [" + sendDataString.length() + "]: " + sendDataString);
                }
                receivedPackets.remove();
            }
            HashMap botsMap = eManager.currentMap.scene.getThingMap("THING_BOTPLAYER");
            if(botsMap.size() > 0 && sVars.getLong("bottime") < uiInterface.gameTime) {
                sVars.putLong("bottime",
                        uiInterface.gameTime + (long)(1000.0/(double)sVars.getInt("ratebots")));
                for(Object id : botsMap.keySet()) {
                    gPlayer p = (gPlayer) botsMap.get(id);
                    nVarsBot.update(p);
                    String botStateStr = nVarsBot.dumpArgsForId(p.get("id"));
                    String receiveDataString = botStateStr;
                    xCon.instance().debug("SERVER RCV [" + receiveDataString.trim().length() + "]: "
                            + receiveDataString.trim());
                    nServer.readData(receiveDataString);
                    //get player id of client
                    HashMap<String, String> clientmap = nVars.getMapFromNetString(receiveDataString);
                    String clientId = clientmap.get("id");
                    nSend.focus_id = clientId;
                    //act as if responding
                    createSendDataString();
                }
            }
        }
        catch (Exception e) {
            eUtils.echoException(e);
            e.printStackTrace();
        }
    }

    public static String createSendDataString() {
        StringBuilder sendDataString;
        nVars.update();
        if(nSend.sendMap != null) {
            for(String k : nVars.keySet()) {
                if(nSend.constantsList.contains(k) || k.equals("id") || !nSend.sendMap.containsKey(k)
                        || !nSend.sendMap.get(k).equals(nVars.get(k)))
                    nSend.sendMap.put(k, nVars.get(k));
                else
                    nSend.sendMap.remove(k);
            }
        }
        else
            nSend.sendMap = nVars.copy();

        nServer.clientArgsMap.put(uiInterface.uuid, nVars.copy());
        sendDataString = new StringBuilder(nVars.dump());
        for(int i = 0; i < nServer.clientIds.size(); i++) {
            String idload2 = nServer.clientIds.get(i);
            if(nServer.clientArgsMap.get(idload2) != null)
                sendDataString.append(String.format("@%s", nServer.clientArgsMap.get(idload2).toString()));
        }
        if(cGameLogic.userPlayer() != null && cGameLogic.userPlayer().contains("spawnprotectiontime"))
            nSend.sendMap.put("spawnprotected","");
        else
            nSend.sendMap.remove("spawnprotected");
        cVars.put("exploded", "1");
        return sendDataString.toString();
    }

    public static void removeNetClient(String id) {
        if(nSend.focus_id.equals(id)){
            nSend.focus_id = "";
        }
        clientArgsMap.remove(id);
        cScoreboard.scoresMap.remove(id);
        clientSendCmdQueues.remove(id);
        clientSendMsgQueues.remove(id);
        gPlayer quittingPlayer = gScene.getPlayerById(id);
        eManager.currentMap.scene.playersMap().remove(id);
        String quitterName = quittingPlayer.get("name");
        clientIds.remove(id);
        if((cVars.getInt("gamemode") == cGameMode.CAPTURE_THE_FLAG
                || cVars.getInt("gamemode") == cGameMode.FLAG_MASTER)
                && cVars.isVal("flagmasterid", quittingPlayer.get("id"))) {
            cVars.put("flagmasterid", "");
        }
        String quitString = String.format("echo %s left the game", quitterName);
        xCon.ex(quitString);
        addSendCmd(quitString);
    }

    public void run() {
        while(true) {
            try {
                netticks++;
                if(uiInterface.nettickcounterTime < uiInterface.gameTime) {
                    uiInterface.netReport = netticks;
                    netticks = 0;
                    uiInterface.nettickcounterTime = uiInterface.gameTime + 1000;
                }
                byte[] receiveData = new byte[sVars.getInt("rcvbytesserver")];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                if(serverSocket == null || serverSocket.isClosed()) {
                    uiInterface.uuid = "server";
                    serverSocket = new DatagramSocket(sVars.getInt("joinport"));
                    serverSocket.setSoTimeout(sVars.getInt("timeout"));
                }
                serverSocket.receive(receivePacket);
                receivedPackets.add(receivePacket);
                uiInterface.networkTime = System.currentTimeMillis()
                        + (long)(1000.0/(double)sVars.getInt("rateserver"));
                sleep(Math.max(0, uiInterface.networkTime-uiInterface.gameTime));
            }
            catch (Exception e) {
                eUtils.echoException(e);
                e.printStackTrace();
            }
        }
    }

    public static void readData(String receiveDataString) {
        String[] toks = receiveDataString.trim().split("@");
        if(toks[0].length() > 0) {
            int isnewclient = 1;
            String argload = toks[0];
            //create score and packet maps
            HashMap<String, String> packArgMap = nVars.getMapFromNetString(argload);
            HashMap<String, HashMap<String, Integer>> scoresMap = cScoreboard.scoresMap;
            //get id from packet
            String packId = packArgMap.get("id");
            //dont proceed if id is null it means packet might be bad
            if(packId == null || banClientIds.containsKey(packId))
                return;
            //insert new ids into the greater maps
            if(!nServer.clientArgsMap.containsKey(packId))
                nServer.clientArgsMap.put(packId, packArgMap);
            if(!scoresMap.containsKey(packId))
                cScoreboard.addId(packId);
            //get name
            String packName = packArgMap.get("name") != null ? packArgMap.get("name")
                    : nServer.clientArgsMap.get(packId).get("name");
            String packActions = packArgMap.get("act") != null ? packArgMap.get("act") : "";
//                int packWeap = packArgMap.get("weapon") != null ? Integer.parseInt(packArgMap.get("weapon")) : 0;
            //fetch old packet
            HashMap<String, String> oldArgMap = nServer.clientArgsMap.get(packId);
//                String oldName = "";
            long oldTimestamp = 0;
            if(oldArgMap != null) {
//                    oldName = oldArgMap.get("name");
                oldTimestamp = oldArgMap.containsKey("time") ?
                        Long.parseLong(oldArgMap.get("time")) : System.currentTimeMillis();
            }
            for(String k : packArgMap.keySet()) {
                if(!nServer.clientArgsMap.get(packId).containsKey(k)
                        || !nServer.clientArgsMap.get(packId).get(k).equals(packArgMap.get(k))) {
                    nServer.clientArgsMap.get(packId).put(k, packArgMap.get(k));
                }
            }
            //record time we last updated client args
            nServer.clientArgsMap.get(packId).put("time", Long.toString(System.currentTimeMillis()));
            //parse and process the args from client packet
            if(nServer.clientIds.contains(packId)) {
                isnewclient = 0;
                scoresMap.get(packId).put("ping", (int) Math.abs(System.currentTimeMillis() - oldTimestamp));
//                    if(oldName.length() > 0 && !oldName.equals(packName))
//                        xCon.ex(String.format("say %s changed name to %s", oldName, packName));
                if(System.currentTimeMillis() > oldTimestamp + sVars.getInt("timeout")) {
                    nServer.quitClientIds.add(packId);
                }
                gPlayer packPlayer = gScene.getPlayerById(packId);
                if(packPlayer != null) {
                    if (nServer.clientArgsMap.get(packId).containsKey("vels")) {
                        String[] veltoks = nServer.clientArgsMap.get(packId).get("vels").split("-");
                        packPlayer.put("vel0", veltoks[0]);
                        packPlayer.put("vel1", veltoks[1]);
                        packPlayer.put("vel2", veltoks[2]);
                        packPlayer.put("vel3", veltoks[3]);
                    }
                    if (sVars.isOne("smoothing")) {
                        packPlayer.put("coordx", nServer.clientArgsMap.get(packId).get("x"));
                        packPlayer.put("coordy", nServer.clientArgsMap.get(packId).get("y"));
                    }
                }
                if(!packArgMap.containsKey("spawnprotected")
                        && nServer.clientArgsMap.get(packId).containsKey("spawnprotected")) {
                    nServer.clientArgsMap.get(packId).remove("spawnprotected");
                }
                cServer.processActionLoadServer(packActions, packName, packId);
                if(packArgMap.containsKey("quit") || packArgMap.containsKey("disconnect")) {
                    nServer.quitClientIds.add(packId);
                }
                if(packArgMap.get("msg") != null && packArgMap.get("msg").length() > 0) {
                    handleClientMessage(packArgMap.get("msg"));
                }
            }
            if(isnewclient == 1) {
                handleNewClientJoin(packId, packName);
            }
            //store player object's health in outgoing network arg map
            nServer.clientArgsMap.get(packId).put("stockhp", gScene.getPlayerById(packId).get("stockhp"));
        }
    }

    public static void handleNewClientJoin(String packId, String packName) {
        nServer.clientIds.add(packId);
        clientSendCmdQueues.put(packId, new LinkedList<>());
        clientSendMsgQueues.put(packId, new LinkedList<>());
        if(!packId.contains("bot")) {
            gPlayer player = new gPlayer(-6000, -6000,150,150,
                    eUtils.getPath("animations/player_red/a03.png"));
            player.put("name", packName);
            player.putInt("tag", eManager.currentMap.scene.playersMap().size());
            player.put("id", packId);
            player.put("stockhp", cVars.get("maxstockhp"));
//                        player.putInt("weapon", packWeap);
            eManager.currentMap.scene.playersMap().put(packId, player);
        }
        addSendCmd(packId, "load "+eManager.currentMap.mapName+sVars.get("mapextension"));
        String joinString = String.format("echo %s joined the game", packName);
        xCon.ex(joinString);
        addSendCmd(joinString);
    }

    public static void handleClientMessage(String msg) {
        xCon.ex("echo " + msg);
        nServer.addSendCmd("echo " + msg);
        //handle special sounds
        String testmsg = msg.substring(msg.indexOf(':')+2);
        checkMessageForSpecialSound(testmsg);
//        checkMessageForVoteToSkip(testmsg);

//        //handle the vote-to-skip function
//        if(msg.strip().length() > 0 && "skip".contains(msg.toLowerCase().strip())) {
//            cVars.addIntVal("voteskipctr", 1);
//            if(!(cVars.getInt("voteskipctr") < cVars.getInt("voteskiplimit"))) {
//                cVars.put("timeleft", "0");
//                xCon.ex(String.format("say [VOTE_SKIP] VOTE TARGET REACHED (%s)", cVars.get("voteskiplimit")));
//                xCon.ex("say [VOTE_SKIP] CHANGING MAP...");
//            }
//            else {
//                xCon.ex("say " + String.format("[VOTE_SKIP] SAY 'skip' TO END ROUND. (%s/%s)",
//                        cVars.get("voteskipctr"), cVars.get("voteskiplimit")));
//            }
//        }
    }

    public static void checkMessageForSpecialSound(String testmsg) {
        for(String s : eManager.winClipSelection) {
            String[] ttoks = s.split("\\.");
            if(testmsg.equalsIgnoreCase(ttoks[0])) {
                String soundString = "playsound sounds/win/" + s;
                xCon.ex(soundString);
                addSendCmd(soundString);
                break;
            }
        }
    }

    public static void checkMessageForVoteToSkip(String testmsg) {
        //handle the vote-to-skip function
        testmsg = testmsg.strip();
        System.out.println(testmsg);
        if(testmsg.equalsIgnoreCase("skip")) {
            cVars.addIntVal("voteskipctr", 1);
            if(!(cVars.getInt("voteskipctr") < cVars.getInt("voteskiplimit"))) {
                cVars.put("timeleft", "0");
                addSendMsg(String.format("[VOTE_SKIP] VOTE TARGET REACHED (%s)", cVars.get("voteskiplimit")));
                addSendMsg("[VOTE_SKIP] CHANGING MAP...");
            }
            else
                addSendMsg(String.format("[VOTE_SKIP] SAY 'skip' TO END ROUND. (%s/%s)",
                        cVars.get("voteskipctr"), cVars.get("voteskiplimit")));
        }
    }
}
