import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class eGameLogicClient extends eGameLogicAdapter {
    private Queue<String> netSendCmds;
    private DatagramSocket clientSocket;
    private nStateMap clientStateMap; //hold net player vars
    private gArgSet receivedArgsServer;
    private boolean cmdReceived;
    public String clientStateSnapshot; //hold snapshot of clientStateMap

    public eGameLogicClient() {
        netSendCmds = new LinkedList<>();
        cmdReceived = false;
        try {
            clientSocket = new DatagramSocket();
            clientSocket.setSoTimeout(500);
        }
        catch (SocketException e) {
            xMain.shellLogic.console.logException(e);
            e.printStackTrace();
        }
        clientStateMap = new nStateMap();
        clientStateSnapshot = "{}";
        receivedArgsServer = new gArgSet();
        receivedArgsServer.putArg(new gArg("time", Long.toString(sSettings.gameTime)) {
            public void onChange() {
                sSettings.clientTimeLeft = Long.parseLong(value);
            }
        });
        receivedArgsServer.putArg(new gArg("cmd", "") {
            public void onUpdate() {
                if(value.length() > 0) {
                    xMain.shellLogic.console.debug("FROM_SERVER: " + value);
                    cmdReceived = true;
                    xMain.shellLogic.console.ex(value);
                }
            }
        });
    }

    private void readData(String netmapstring) {
        ArrayList<String> foundIds = new ArrayList<>();
        Queue<String> toRemove = new LinkedList<>();
        xMain.shellLogic.console.debug(String.format("CLIENT RCV [%d]: %s", netmapstring.length(), netmapstring));
        nStateMap packArgStateMap = new nStateMap(netmapstring);
        for(String idload : packArgStateMap.keys()) {
            nState packArgState = packArgStateMap.get(idload);
            // SERVER time and cmd
            if(idload.equals("server")) {
                for (String k : packArgState.keys()) {
                    receivedArgsServer.put(k, packArgState.get(k));
                }
            }
            else {
                if(!clientStateMap.contains(idload))
                    clientStateMap.put(idload, new nStateBallGameClient());
                for(String k : packArgState.keys()) {
                    clientStateMap.get(idload).put(k, packArgState.get(k));
                }
                foundIds.add(idload);
            }
        }
        // check for IDs that are absent from received snapshot
        for(String k : clientStateMap.keys()) {
            if(!foundIds.contains(k))
                toRemove.add(k);
        }
        while(toRemove.size() > 0) {
            clientStateMap.remove(toRemove.remove());
        }
        clientStateSnapshot = clientStateMap.toString().replace(", ", ",");
    }

    @Override
    public void update() {
        super.update();
        try {
            sendData();
            byte[] clientReceiveData = new byte[sSettings.rcvbytesclient];
            DatagramPacket receivePacket = new DatagramPacket(clientReceiveData, clientReceiveData.length);
            clientSocket.receive(receivePacket);
            readData(new String(receivePacket.getData()).trim());
            sSettings.clientNetRcvTime = System.currentTimeMillis();
            if(sSettings.clientNetRcvTime > sSettings.clientNetSendTime)
                sSettings.clientPing = (int) (sSettings.clientNetRcvTime - sSettings.clientNetSendTime);
        }
        catch (SocketException se) {
            //just to catch the closing
            se.printStackTrace();
            return;
        }
        catch (Exception e) {
            xMain.shellLogic.console.logException(e);
            e.printStackTrace();
        }
        sSettings.tickReportClient = getTickReport();
    }

    public void addNetCmd(String cmd) {
        netSendCmds.add(cmd);
    }

    private void sendData() {
        InetAddress IPAddress;
        try {
            //if we are the server, have local client send data thru localhost always
            if(sSettings.IS_SERVER)
                IPAddress = InetAddress.getByName("127.0.0.1");
            else
                IPAddress = InetAddress.getByName(xMain.shellLogic.console.ex("cl_setvar joinip"));
            String sendDataString = getNetVars().toString().replace(", ", ",");
            byte[] clientSendData = sendDataString.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(clientSendData, clientSendData.length, IPAddress,
                    Integer.parseInt(xMain.shellLogic.console.ex("cl_setvar joinport")));
            clientSocket.send(sendPacket);
            sSettings.clientNetSendTime = System.currentTimeMillis();
            xMain.shellLogic.console.debug("CLIENT SND [" + clientSendData.length + "]:" + sendDataString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HashMap<String, String> getNetVars() {
        HashMap<String, String> keys = new HashMap<>();
        String outgoingCmd = dequeueNetCmd(); //dequeues w/ every call so call once a tick
        keys.put("cmd", outgoingCmd != null ? outgoingCmd : "");
        keys.put("cmdrcv", cmdReceived ? "1" : "0");
        //update id in net args
        keys.put("id", sSettings.uuid);
        keys.put("color", sSettings.clientPlayerColor);
        keys.put("name", sSettings.clientPlayerName);
        gPlayer userPlayer = xMain.shellLogic.getUserPlayer();
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
        if(sSettings.show_mapmaker_ui) {
            keys.put("px", Integer.toString(sSettings.clientPrevX));
            keys.put("py", Integer.toString(sSettings.clientPrevY));
            keys.put("pw", Integer.toString(sSettings.clientPrevW));
            keys.put("ph", Integer.toString(sSettings.clientPrevH));
        }
        cmdReceived = false; //always open up to new commands
        return keys;
    }

    private String dequeueNetCmd() {
        if(netSendCmds.size() > 0) {
            String cmdString = netSendCmds.peek();
            // user's client-side firing
            if(cmdString.startsWith("fireweapon"))
                xMain.shellLogic.console.ex("cl_" + cmdString);
            xMain.shellLogic.console.debug("TO_SERVER: " + cmdString);
            return netSendCmds.remove();
        }
        return null;
    }

    @Override
    public void cleanup() {
        super.cleanup();
        sSettings.IS_CLIENT = false;
        xMain.shellLogic.clientNetThread = null;
        clientSocket.close();
        sSettings.tickReportClient = 0;
    }
}
