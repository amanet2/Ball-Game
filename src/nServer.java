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
    static Queue<String> kickClientIds = new LinkedList<>(); //temporarily holds ids that are being kicked
    static boolean kickConfirmed = false;
    static ArrayList<String> clientIds = new ArrayList<>();
    static HashMap<String, HashMap<String, String>> clientArgsMap = new HashMap<>(); //server too, index by uuids
    //id maps to queue of cmds we want to run on that client
    static HashMap<String, Queue<String>> clientSendCmdQueues = new HashMap<>();
    static String[] mapvoteSelection = new String[]{};
    private static Queue<DatagramPacket> receivedPackets = new LinkedList<>();
    private static nServer instance = null;
    static DatagramSocket serverSocket = null;


    public static nServer instance() {
        if(instance == null)
            instance = new nServer();
        return instance;
    }

    private nServer() {
        netticks = 0;
    }

    public static void addSendCmd(String id, String cmd) {
        if(!clientSendCmdQueues.containsKey(id))
            clientSendCmdQueues.put(id, new LinkedList<>());
        clientSendCmdQueues.get(id).add(cmd);
    }

    public static void addSendCmd(String cmd) {
        for(String id : clientSendCmdQueues.keySet()) {
            addSendCmd(id, cmd);
        }
    }

    public void setMapvoteSelection() {
        nServer.mapvoteSelection = new String[7];
        for(int i = 0; i < 5; i++) {
            int tovote = (int)(Math.random()*eManager.mapsSelection.length);
            nServer.mapvoteSelection[i] = eManager.mapsSelection[tovote];
        }
        nServer.mapvoteSelection[5] = "EXTEND " + (double)sVars.getInt("timelimit")/60000.0
                + " MINUTES on " + eManager.mapsSelection[eManager.mapSelectionIndex];
        nServer.mapvoteSelection[6] = "REMATCH on " + eManager.mapsSelection[eManager.mapSelectionIndex];
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
                nServer.readData(receiveDataString);
                //show the ip address of the client
                InetAddress addr = receivePacket.getAddress();
                int port = receivePacket.getPort();
                //get player id of client
                HashMap<String, String> clientmap = nVars.getMapFromNetString(receiveDataString);
                String clientId = clientmap.get("id");
                nSend.focus_id = clientId;
                //create response
                String sendDataString = createSendDataString();
                byte[] sendData = sendDataString.getBytes();
                DatagramPacket sendPacket =
                        new DatagramPacket(sendData, sendData.length, addr, port);
                serverSocket.send(sendPacket);
                xCon.instance().debug("SERVER SND [" + sendDataString.length() + "]: " + sendDataString);
                if (kickClientIds.size() > 0 && kickClientIds.peek().equals(clientId)) {
                    kickConfirmed = true;
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
                    if (kickClientIds.size() > 0 && kickClientIds.peek().equals(clientId)) {
                        kickConfirmed = true;
                        break;
                    }
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
        cVars.put("sendsafezone", "0");
        return sendDataString.toString();
    }

    public static void removeNetClient(String id) {
        if(nSend.focus_id.equals(id)){
            nSend.focus_id = "";
        }
        clientArgsMap.remove(id);
        cScoreboard.scoresMap.remove(id);
        clientSendCmdQueues.remove(id);
        gPlayer quittingPlayer = gScene.getPlayerById(id);
        eManager.currentMap.scene.playersMap().remove(id);
        String quitterName = quittingPlayer.get("name");
        clientIds.remove(id);
        if((cVars.getInt("gamemode") == cGameMode.CAPTURE_THE_FLAG
                || cVars.getInt("gamemode") == cGameMode.FLAG_MASTER)
                && cVars.isVal("flagmasterid", quittingPlayer.get("id"))) {
            cVars.put("flagmasterid", "");
        }
        xCon.ex(String.format("say %s left the game", quitterName));
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
                    String msg = packArgMap.get("msg");
                    xCon.ex(String.format("say %s", msg));
                    String[] t = msg.split(" ");
                    if(t.length > 1)
                        cScripts.checkMsgSpecialFunction(t[1]);
                }
            }
            if(isnewclient == 1) {
                nServer.clientIds.add(packId);
                if(!packId.contains("bot")) {
                    gPlayer player = new gPlayer(-6000, -6000,150,150,
                            eUtils.getPath("animations/player_red/a03.png"));
                        player.put("name", packName);
                    player.putInt("tag", eManager.currentMap.scene.playersMap().size());
                    player.put("id", packId);
//                        player.putInt("weapon", packWeap);
                    eManager.currentMap.scene.playersMap().put(packId, player);
                }
                addSendCmd(packId, "load "+eManager.currentMap.mapName+sVars.get("mapextension"));
                xCon.ex(String.format("say %s joined the game", packName));
            }
            nServer.clientArgsMap.get(packId).put("stockhp", gScene.getPlayerById(packId).get("stockhp"));
        }
    }
}
