import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class nServer extends Thread {
    private int netticks;
    static ArrayList<String> newClientIds = new ArrayList<>(); //temporarily holds ids that needs full args
    static Queue<String> quitClientIds = new LinkedList<>(); //temporarily holds ids that are quitting
    static Queue<String> kickClientIds = new LinkedList<>(); //temporarily holds ids that are being kicked
    static boolean kickConfirmed = false;
    static ArrayList<String> clientIds = new ArrayList<>();
    static HashMap<String, HashMap<String, String>> clientArgsMap = new HashMap<>(); //server too, index by uuids
    static HashMap<String, HashMap<String, Integer>> scoresMap = new HashMap<>(); //server too, index by uuids
    static String[] mapvoteSelection = new String[]{};
    private static Queue<DatagramPacket> receivedPackets = new LinkedList<>();
    private static nServer instance = null;

    public static nServer instance() {
        if(instance == null)
            instance = new nServer();
        return instance;
    }

    private nServer() {
        netticks = 0;
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
        if(sSettings.net_server && eManager.currentMap.scene.botplayers().size() > 0) {
            for(gPlayer p : eManager.currentMap.scene.botplayers()) {
                nServer.quitClientIds.add(p.get("id"));
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

    public static void incrementScoreFieldById(String id, String field) {
        if(!nServer.scoresMap.containsKey(id))
            nServer.scoresMap.put(id, new HashMap<>());
        if(!nServer.scoresMap.get(id).containsKey(field))
            nServer.scoresMap.get(id).put(field, 0);
        int nscore = nServer.scoresMap.get(id).get(field) + 1;
        nServer.scoresMap.get(id).put(field, nscore);
    }

    public static void processPackets() {
        try {
            if(receivedPackets.size() > 0) {
                DatagramPacket receivePacket = receivedPackets.peek();
                String receiveDataString = new String(receivePacket.getData());
                xCon.instance().debug("SERVER RCV [" + receiveDataString.trim().length() + "]: "
                        + receiveDataString.trim());
                nReceive.processReceiveDataString(receiveDataString);
                //show the ip address of the client
                InetAddress addr = receivePacket.getAddress();
                int port = receivePacket.getPort();
                //                String k = String.format("%s:%d", addr.toString(), port);
                //get player id of client
                HashMap<String, String> clientmap = nVars.getMapFromNetString(receiveDataString);
                String clientId = clientmap.get("id");
                nSend.focus_id = clientId;
                //create response
                String sendDataString = nSend.createSendDataString();
                byte[] sendData = sendDataString.getBytes();
                DatagramPacket sendPacket =
                        new DatagramPacket(sendData, sendData.length, addr, port);
                uiInterface.serverSocket.send(sendPacket);
                xCon.instance().debug("SERVER SND [" + sendDataString.length() + "]: " + sendDataString);
                if (kickClientIds.size() > 0 && kickClientIds.peek().equals(clientId)) {
                    kickConfirmed = true;
                }
                receivedPackets.remove();
            }
            if(eManager.currentMap.scene.botplayers().size() > 0 && sVars.getLong("bottime") < uiInterface.gameTime) {
                sVars.putLong("bottime",
                        uiInterface.gameTime + (long)(1000.0/(double)sVars.getInt("ratebots")));
                for(gPlayer p : eManager.currentMap.scene.botplayers()) {
                    nVarsBot.update(p);
                    String botStateStr = nVarsBot.dumpArgsForId(p.get("id"));
                    String receiveDataString = botStateStr;
                    xCon.instance().debug("SERVER RCV [" + receiveDataString.trim().length() + "]: "
                            + receiveDataString.trim());
                    nReceive.processReceiveDataString(receiveDataString);
                    //String k = String.format("%s:%d", addr.toString(), port);
                    //get player id of client
                    HashMap<String, String> clientmap = nVars.getMapFromNetString(receiveDataString);
                    String clientId = clientmap.get("id");
                    nSend.focus_id = clientId;
                    //act as if responding
                    nSend.createSendDataString();
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

    public static void removeNetClient(String id) {
        if(nSend.focus_id.equals(id)){
            nSend.focus_id = "";
        }
        clientArgsMap.remove(id);
        scoresMap.remove(id);
        gPlayer quittingPlayer = cGameLogic.getPlayerById(id);
        eManager.currentMap.scene.players().remove(quittingPlayer);
        String quitterName = quittingPlayer.get("name");
        clientIds.remove(id);
        if((cVars.getInt("gamemode") == cGameMode.CAPTURE_THE_FLAG
                || cVars.getInt("gamemode") == cGameMode.FLAG_MASTER)
                && cVars.isVal("flagmasterid", quittingPlayer.get("id"))) {
            cVars.put("flagmasterid", "");
        }
        if(cVars.getInt("gamemode") == cGameMode.VIRUS_SINGLE
                && cVars.isVal("virussingleid", quittingPlayer.get("id"))) {
            cVars.put("virussingleid", "");
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
                if(uiInterface.serverSocket == null || uiInterface.serverSocket.isClosed()) {
                    uiInterface.uuid = "server";
                    uiInterface.serverSocket = new DatagramSocket(sVars.getInt("joinport"));
                    uiInterface.serverSocket.setSoTimeout(sVars.getInt("timeout"));
                }
                uiInterface.serverSocket.receive(receivePacket);
                receivedPackets.add(receivePacket);
                uiInterface.networkTime = uiInterface.gameTime + (long)(1000.0/(double)sVars.getInt("rateserver"));
//                if(nServer.clientsConnected < 1)
//                    uiInterface.networkTime = uiInterface.gameTime + (long)(1000.0/(double)sVars.getInt("ratebots"));

                if(sVars.getInt("rateserver") > 1000)
                    sleep(0, (int)(uiInterface.networkTime-uiInterface.gameTime));
                else
                    sleep(uiInterface.networkTime-uiInterface.gameTime);
            }
            catch (Exception e) {
                eUtils.echoException(e);
                e.printStackTrace();
            }
        }
    }
}
