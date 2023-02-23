import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class nClient extends Thread {
    private int ticks = 0;
    private long nextSecondNanos = 0;
    private static final int retrylimit = 10;
    long netTime = -1;
    private static final int timeout = 500;
    Queue<DatagramPacket> receivedPackets = new LinkedList<>();
    HashMap<String, String> serverArgsMap = new HashMap<>(); //hold server vars
    ArrayList<String> playerIds = new ArrayList<>(); //insertion-ordered list of client ids
    HashMap<String, String> sendMap = new HashMap<>();
    private final ArrayList<String> protectedArgs = new ArrayList<>(Arrays.asList("id", "cmdrcv", "cmd"));
    private final Queue<String> netSendMsgs = new LinkedList<>();
    private final Queue<String> netSendCmds = new LinkedList<>();
    private static nClient instance = null;
    private DatagramSocket clientSocket = null;
    nStateMap clientStateMap; //hold net player vars

    public static nClient instance() {
        if(instance == null)
            instance = new nClient();
        return instance;
    }

    public void reset() {
        clientStateMap = new nStateMap();
        netSendMsgs.clear();
        netSendCmds.clear();
        netTime = -1;
        receivedPackets.clear();
        serverArgsMap.clear();
        serverArgsMap.put("time", "180000");
        playerIds.clear();
        sendMap.clear();
    }

    public void run() {
        try {
            refreshSock();
            while (sSettings.IS_CLIENT) {
                try {
                    sendData();
                    byte[] clientReceiveData = new byte[sSettings.rcvbytesclient];
                    DatagramPacket receivePacket = new DatagramPacket(clientReceiveData,
                            clientReceiveData.length);
                    clientSocket.receive(receivePacket);
                    receivedPackets.add(receivePacket);
                    cClientLogic.serverRcvTime = System.currentTimeMillis();
                    if(cClientLogic.serverRcvTime > cClientLogic.serverSendTime)
                        cClientLogic.ping = (int) (cClientLogic.serverRcvTime - cClientLogic.serverSendTime);
                    ticks++;
                    long theTime = System.nanoTime();
                    if(nextSecondNanos < theTime) {
                        nextSecondNanos = theTime + 1000000000;
                        uiInterface.netReportClient = ticks;
                        ticks = 0;
                    }
                    // client rate limit
                    int tickRate = sSettings.rateclient;
                    long nextFrameTime = (theTime + (1000000000/tickRate));
                    while (nextFrameTime > System.nanoTime()) {
                        try {
                            Thread.sleep(1);
                        }
                        catch (InterruptedException ignored) {
                        }
                    }
                }
                catch (Exception e) {
                    eLogging.logException(e);
                    e.printStackTrace();
                }
            }
            interrupt();
        }
        catch (Exception ee) {
            eLogging.logException(ee);
            ee.printStackTrace();
        }
    }

    private nClient() {
        clientStateMap = new nStateMap();
    }

    void addSendMsg(String msg) {
        netSendMsgs.add(msg);
    }

    public void addNetCmd(String cmd) {
        netSendCmds.add(cmd);
    }

    public void processPackets() {
        try {
            while(receivedPackets.size() > 1) {
                //this means all other packets are thrown out, bad in long run
                receivedPackets.remove();
            }
            if(receivedPackets.size() > 0) {
                DatagramPacket receivePacket = receivedPackets.peek();
                if(receivePacket != null && receivePacket.getData() != null) {
                    String receiveDataString = new String(receivePacket.getData());
                    xCon.instance().debug(String.format("CLIENT RCV [%d]: %s",
                            receiveDataString.trim().length(), receiveDataString.trim()));
                    readData(receiveDataString);
                }
            }
        }
        catch (Exception e) {
            eLogging.logException(e);
            e.printStackTrace();
        }
        receivedPackets.clear();
    }

    public void sendData() {
        InetAddress IPAddress = null;
        try {
            //TODO: if we are the server, have client send data thru localhost always
            if(sSettings.IS_SERVER)
                IPAddress = InetAddress.getByName("127.0.0.1");
            else
                IPAddress = InetAddress.getByName(xCon.ex("cl_setvar joinip"));
            String sendDataString = createSendDataString();
            byte[] clientSendData = sendDataString.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(clientSendData, clientSendData.length, IPAddress,
                    Integer.parseInt(xCon.ex("cl_setvar joinport")));
//            if (clientSocket == null || clientSocket.isClosed()) {
//                clientSocket = new DatagramSocket();
//                clientSocket.setSoTimeout(timeout);
//            }

            clientSocket.send(sendPacket);
            cClientLogic.serverSendTime = System.currentTimeMillis();
            xCon.instance().debug("CLIENT SND [" + clientSendData.length + "]:" + sendDataString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refreshSock() {
        try {
            clientSocket = new DatagramSocket();
            clientSocket.setSoTimeout(timeout);
        }
        catch (SocketException e) {
            eLogging.logException(e);
            e.printStackTrace();
        }
    }

    private HashMap<String, String> getNetVars() {
        HashMap<String, String> keys = new HashMap<>();
        gPlayer userPlayer = cClientLogic.getUserPlayer();
        //handle outgoing msg
        String outgoingMsg = dequeueNetMsg(); //dequeues w/ every call so call once a tick
        keys.put("msg", outgoingMsg != null ? outgoingMsg : "");
        //handle outgoing cmd
        String outgoingCmd = dequeueNetCmd(); //dequeues w/ every call so call once a tick
        keys.put("cmd", outgoingCmd != null ? outgoingCmd : "");
        //update id in net args
        keys.put("id", uiInterface.uuid);
        keys.put("color", cClientLogic.playerColor);
        //userplayer vars like coords and dirs and weapon
        if(userPlayer != null) {
            keys.put("fv", userPlayer.get("fv").substring(0, Math.min(userPlayer.get("fv").length(), 4)));
            keys.put("mov0", userPlayer.get("mov0"));
            keys.put("mov1", userPlayer.get("mov1"));
            keys.put("mov2", userPlayer.get("mov2"));
            keys.put("mov3", userPlayer.get("mov3"));
        }
        else {
            keys.put("fv", "0");
            keys.put("mov0", "0");
            keys.put("mov1", "0");
            keys.put("mov2", "0");
            keys.put("mov3", "0");
        }
        //name for spectator and gameplay
        keys.put("name", cClientLogic.playerName);
        if(sSettings.show_mapmaker_ui) {
            keys.put("px", Integer.toString(cClientLogic.prevX));
            keys.put("py", Integer.toString(cClientLogic.prevY));
            keys.put("pw", Integer.toString(cClientLogic.prevW));
            keys.put("ph", Integer.toString(cClientLogic.prevH));
        }
        return keys;
    }

    private String createSendDataString() {
        StringBuilder sendDataString;
        HashMap<String, String> netVars = getNetVars();
        //this BS has to be decoded
        for(String k : netVars.keySet()) {
            sendMap.put(k, netVars.get(k));
        }
        HashMap<String, String> workingMap = new HashMap<>(sendMap);
        //send delta of serverargs
//        if(clientStateMap.contains(uiInterface.uuid)) {
//            for (String k : clientStateMap.get(uiInterface.uuid).keys()) {
//                if (protectedArgs.contains(k) ||
//                        (sendMap.containsKey(k) && !sendMap.get(k).equals(clientStateMap.get(uiInterface.uuid).get(k))))
//                    continue;
//                workingMap.remove(k);
//            }
//        }
        sendDataString = new StringBuilder(workingMap.toString());
        //handle removing variables after the fact
        sendMap.remove("cmd");
        if(sendMap.containsKey("cmdrcv") && sendMap.get("cmdrcv").equals("1")) // turn off our own rcv when we see we sent it last time
            sendMap.put("cmdrcv", "0");
        return sendDataString.toString().replace(", ", ",");
    }

    private void processCmd(String cmdload) {
        sendMap.put("cmdrcv", "1");
        xCon.ex(cmdload);
    }

    private void handleReadDataServer(HashMap<String, String> packArgs) {
        for (String k : packArgs.keySet()) {
            serverArgsMap.put(k, packArgs.get(k));
        }
        //check cmd from server only
        String cmdload = packArgs.get("cmd") != null ? packArgs.get("cmd") : "";
        if(cmdload.length() > 0) {
            xCon.instance().debug("FROM_SERVER: " + cmdload);
            processCmd(cmdload);
        }
    }

    public void readData(String receiveDataString) {
        ArrayList<String> foundIds = new ArrayList<>();
        String netmapstring = receiveDataString.trim();
        HashMap<String, HashMap<String, String>> packargmap = nVars.getMapFromNetMapString(netmapstring);
        for(String idload : packargmap.keySet()) {
            HashMap<String, String> packArgs = new HashMap<>(packargmap.get(idload));
            //NEW --
            //--
            if(!idload.equalsIgnoreCase("server")) {
                if(!clientStateMap.contains(idload))
                    clientStateMap.put(idload, new nStateBallGameClient());
                for(String k : packArgs.keySet()) {
                    clientStateMap.get(idload).put(k, packArgs.get(k));
                }
            }
            //OLD --
            //--
            if(idload.equals("server"))
                handleReadDataServer(packArgs);
            else if(!idload.equals(uiInterface.uuid)) {
                if(playerIds.contains(idload))
                    foundIds.add(idload);
                else {
                    playerIds.add(idload);
                    foundIds.add(idload);
                }
            }
            if(idload.equals(uiInterface.uuid)) { // handle our own player to get things like stockhp from server
                gPlayer userPlayer = cClientLogic.getUserPlayer();
                if(userPlayer != null && packArgs.containsKey("hp"))
                    userPlayer.put("stockhp", packArgs.get("hp"));
            }
        }
        //check for ids that have been taken out of the server argmap
        String tr = "";
        for(String s : playerIds) {
            if(!foundIds.contains(s)) {
                tr = s;
            }
        }
        if(tr.length() > 0) {
            clientStateMap.remove(tr);
            gScoreboard.scoresMap.remove(tr);
            playerIds.remove(tr);
            cClientLogic.scene.getThingMap("THING_PLAYER").remove(tr);
        }
    }

    public String dequeueNetMsg() {
        if(netSendMsgs.size() > 0) {
            return netSendMsgs.remove();
        }
        return null;
    }

    public String dequeueNetCmd() {
        if(netSendCmds.size() > 0) {
            String cmdString = netSendCmds.peek();
//            //user's client-side firing (like in halo 5)
            if(cmdString.contains("fireweapon")) //handle special firing case
                xCon.ex(cmdString.replaceFirst("fireweapon", "cl_fireweapon"));
            xCon.instance().debug("TO_SERVER: " + cmdString);
            return netSendCmds.remove();
        }
        return null;
    }

    public void disconnect() {
        if(sSettings.IS_CLIENT) {
            sSettings.IS_CLIENT = false;
            clientSocket.close();
            serverArgsMap = new HashMap<>();
            clientStateMap = new nStateMap();
            playerIds = new ArrayList<>();
        }
    }
}
