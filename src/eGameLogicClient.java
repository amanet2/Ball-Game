import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class eGameLogicClient implements eGameLogic {
    private int ticks;
    private long nextsecondnanos;
    private Queue<String> netSendCmds;
    private DatagramSocket clientSocket;
    private Queue<DatagramPacket> receivedPackets;
    private HashMap<String, String> sendMap;
    public nStateMap clientStateMap; //hold net player vars
    private gArgSet receivedArgsSetServer;
    private ArrayList<String> playerIds;
    private eGameSession parentSession;

    public void setParentSession(eGameSession session) {
        parentSession = session;
    }

    public eGameLogicClient() {
        ticks = 0;
        nextsecondnanos = 0;
        netSendCmds = new LinkedList<>();
        receivedPackets = new LinkedList<>();
        sendMap = new HashMap<>();
        playerIds = new ArrayList<>();
        try {
            clientSocket = new DatagramSocket();
            clientSocket.setSoTimeout(500);
        }
        catch (SocketException e) {
            eLogging.logException(e);
            e.printStackTrace();
        }
        clientStateMap = new nStateMap();
        receivedArgsSetServer = new gArgSet();
        receivedArgsSetServer.putArg(new gArg("time", Long.toString(gTime.gameTime)) {
            public void onChange() {
                cClientLogic.timeleft = Long.parseLong(value);
            }
        });
        receivedArgsSetServer.putArg(new gArg("cmd", "") {
            public void onUpdate() {
                if(value.length() > 0) {
                    xCon.instance().debug("FROM_SERVER: " + value);
                    sendMap.put("cmdrcv", "1");
                    xCon.ex(value);
                }
            }
        });
    }

    @Override
    public void init(){

    }

    @Override
    public void input() {

    }

    public void processPackets() {
        try {
            while(receivedPackets.size() > 1) {
                //this means all older packets are thrown out
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

    public void readData(String receiveDataString) {
        ArrayList<String> foundIds = new ArrayList<>();
        String netmapstring = receiveDataString.trim();
        HashMap<String, HashMap<String, String>> packargmap = getMapFromNetMapString(netmapstring);
        for(String idload : packargmap.keySet()) {
            HashMap<String, String> packArgs = new HashMap<>(packargmap.get(idload));
            // SERVER time and cmd
            if(idload.equals("server")) {
                for (String k : packArgs.keySet()) {
                    receivedArgsSetServer.put(k, packArgs.get(k));
                }
            }
            else {
                if(!clientStateMap.contains(idload))
                    clientStateMap.put(idload, new nStateBallGameClient());
                for(String k : packArgs.keySet()) {
                    clientStateMap.get(idload).put(k, packArgs.get(k));
                }
                if(!idload.equals(uiInterface.uuid)) {
                    foundIds.add(idload);
                    if(!playerIds.contains(idload))
                        playerIds.add(idload);
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

    public HashMap<String, HashMap<String, String>> getMapFromNetMapString(String argload) {
        HashMap<String, HashMap<String, String>> toReturn = new HashMap<>();
        String modstr = argload.substring(1,argload.length()-1);
        String[] idsplit = modstr.split("},");
        for(String s : idsplit) {
            String idtok = s.substring(0,s.indexOf("={"));
            toReturn.put(idtok, new HashMap<>());
            String argstr = s.replace(idtok+"={","");
            if(argstr.length() < 1)
                continue;
            if(argstr.charAt(argstr.length()-1) == '}')
                argstr = argstr.substring(0, argstr.length()-1);
            for(String pair : argstr.split(",")) {
                String[] vals = pair.split("=");
                toReturn.get(idtok).put(vals[0].trim(), vals.length > 1 ? vals[1].trim() : "");
            }
        }
        return toReturn;
    }

    @Override
    public void update() {
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
//            processPackets();
        }
        catch (Exception e) {
            eLogging.logException(e);
            e.printStackTrace();
        }
        ticks++;
        long theTime = System.nanoTime();
        if(nextsecondnanos < theTime) {
            nextsecondnanos = theTime + 1000000000;
            uiInterface.tickReportClient = ticks;
            ticks = 0;
        }
    }

    public void addNetCmd(String cmd) {
        netSendCmds.add(cmd);
    }

    private void sendData() {
        InetAddress IPAddress = null;
        try {
            //if we are the server, have client send data thru localhost always
            if(sSettings.IS_SERVER)
                IPAddress = InetAddress.getByName("127.0.0.1");
            else
                IPAddress = InetAddress.getByName(xCon.ex("cl_setvar joinip"));
            String sendDataString = createSendDataString();
            byte[] clientSendData = sendDataString.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(clientSendData, clientSendData.length, IPAddress,
                    Integer.parseInt(xCon.ex("cl_setvar joinport")));

            clientSocket.send(sendPacket);
            cClientLogic.serverSendTime = System.currentTimeMillis();
            xCon.instance().debug("CLIENT SND [" + clientSendData.length + "]:" + sendDataString);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private HashMap<String, String> getNetVars() {
        HashMap<String, String> keys = new HashMap<>();
        gPlayer userPlayer = cClientLogic.getUserPlayer();
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

    public String dequeueNetCmd() {
        if(netSendCmds.size() > 0) {
            String cmdString = netSendCmds.peek();
            // user's client-side firing
            if(cmdString.contains("fireweapon"))
                xCon.ex(cmdString.replaceFirst("fireweapon", "cl_fireweapon"));
            xCon.instance().debug("TO_SERVER: " + cmdString);
            return netSendCmds.remove();
        }
        return null;
    }

    @Override
    public void render() {

    }

    @Override
    public void disconnect() {
        parentSession.destroy();
    }

    @Override
    public void cleanup() {
        sSettings.IS_CLIENT = false;
        cClientLogic.netClientThread = null;
        clientSocket.close();
    }
}
