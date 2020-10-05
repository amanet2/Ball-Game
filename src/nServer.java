import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class nServer extends Thread {
    private int netticks;
    static int[] matchWins = new int[]{0};
    static int[] scores = new int[]{0};
    static int[] matchKills = new int[]{0};
    static int[] matchPings = new int[]{0};
    static int clientsConnected = 0;
    static ArrayList<String> newClientIds = new ArrayList<>(); //temporarily holds ids that needs full args
    static Queue<String> quitClientIds = new LinkedList<>(); //temporarily holds ids that are quitting
    static Queue<String> kickClientIds = new LinkedList<>(); //temporarily holds ids that are being kicked
    static boolean kickConfirmed = false;
    static ArrayList<String> clientIds = new ArrayList<>();
    static ArrayList<String> clientNames = new ArrayList<>();
    static HashMap<String, HashMap<String, String>> clientArgsMap = new HashMap<>(); //server too, index by uuids
    static String[] mapvoteSelection = new String[]{};
    static Queue<DatagramPacket> receivedPackets = new LinkedList<>();
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

    public static void givePoint(int i) {
        if(cVars.isOne("gameteam")) {
            String color = cGameLogic.getPlayerByIndex(i).get("color");
            for(int j = 0; j < scores.length; j++) {
                if(color.equals(cGameLogic.getPlayerByIndex(j).get("color"))) {
                    scores[j]++;
                }
            }
        }
        else if(scores.length > i){
            scores[i]++;
        }
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
                HashMap<String, String> clientmap = cScripts.getMapFromNetString(receiveDataString);
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
            if(eManager.currentMap.scene.botplayers().size() > 0) {
                for(gPlayer p : eManager.currentMap.scene.botplayers()) {
                    nVarsBot.update(p);
                    String botStateStr = nVarsBot.dumpArgsForId(p.get("id"));
                    String receiveDataString = botStateStr;
                    xCon.instance().debug("SERVER RCV [" + receiveDataString.trim().length() + "]: "
                            + receiveDataString.trim());
                    nReceive.processReceiveDataString(receiveDataString);
                    //                String k = String.format("%s:%d", addr.toString(), port);
                    //get player id of client
                    HashMap<String, String> clientmap = cScripts.getMapFromNetString(receiveDataString);
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
            e.printStackTrace();
        }
    }

    public void run() {
        while(true) {
            try {
                netticks += 1;
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
                if(sVars.getInt("rateserver") > 1000)
                    sleep(0, (int)(uiInterface.networkTime-uiInterface.gameTime));
                else
                    sleep(uiInterface.networkTime-uiInterface.gameTime);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
