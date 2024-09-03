import java.io.IOException;
import java.net.*;
import java.util.*;

public class eGameLogicClient extends eGameLogicAdapter {
    private final Queue<String> netSendCmds;
    private DatagramSocket clientSocket;
    private final nStateMap clientStateMap; //hold net player vars
    private final gArgSet receivedArgsServer;
    private boolean cmdReceived;
    public String clientStateSnapshot; //hold snapshot of clientStateMap
    private int failure_count = 0;

    public eGameLogicClient() {
        super();
        netSendCmds = new LinkedList<>();
        cmdReceived = false;
        try {
            clientSocket = new DatagramSocket();
            clientSocket.setSoTimeout(1000);
        }
        catch (SocketException e) {
            xMain.shellLogic.console.logException(e);
        }
        clientStateMap = new nStateMap();
        clientStateSnapshot = "{}";
        receivedArgsServer = new gArgSet();
        receivedArgsServer.putArg(new gArg("time", Long.toString(sSettings.gameTime)) {
            @Override
            public void onChange() {
                sSettings.clientTimeLeft = Long.parseLong(value);
            }
        });
        receivedArgsServer.putArg(new gArg("cmd", "") {
            @Override
            public void onUpdate() {
                if(!value.isEmpty()) {
                    xMain.shellLogic.console.debug("FROM_SERVER: " + value);
                    cmdReceived = true;
                    String[] batchCommands = value.split(";");
                    for(String comm : batchCommands) {
                        xMain.shellLogic.console.ex(comm);
                    }
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
                    //TODO: special code here to manage rate of client position set by server
                }
                if(idload.startsWith("bot")) {
                    gPlayer botThing = xMain.shellLogic.getPlayerById(idload);
                    if(botThing != null) {
                        String[] ccds = packArgState.get("coords").split(":");
                        botThing.coords = new int[]{Integer.parseInt(ccds[0]), Integer.parseInt(ccds[1])};
                        botThing.fv = Double.parseDouble(packArgState.get("fv"));
                        botThing.checkSpriteFlip();
                    }
                }
                foundIds.add(idload);
            }
        }
        // check for IDs that are absent from received snapshot
        for(String k : clientStateMap.keys()) {
            if(!foundIds.contains(k))
                toRemove.add(k);
        }
        while(!toRemove.isEmpty()) {
            clientStateMap.remove(toRemove.remove());
        }
        clientStateSnapshot = clientStateMap.toString().replace(", ", ",");
    }

    @Override
    public void update() {
        super.update();
        long gameTimeMillis = System.currentTimeMillis();
        try {
            sendData();
            byte[] clientReceiveData = new byte[sSettings.rcvbytesclient];
            DatagramPacket receivePacket = new DatagramPacket(clientReceiveData, clientReceiveData.length);
            clientSocket.receive(receivePacket);  // this fails when joining unreachable server
            readData(new String(receivePacket.getData()).trim());
            sSettings.clientNetRcvTime = gameTimeMillis;
            if(sSettings.clientNetRcvTime > sSettings.clientNetSendTime)
                sSettings.clientPing = (int) (sSettings.clientNetRcvTime - sSettings.clientNetSendTime);
        }
        catch (SocketException se) {
            //just to catch the closing
            xMain.shellLogic.console.logException(se);
            return;
        }
        catch (SocketTimeoutException ste) {
            if(failure_count++ >= 10) {
                xMain.shellLogic.console.ex("disconnect");
                xMain.shellLogic.console.ex("cl_echo disconnected due to connection issues");
                failure_count = 0;
            }
            xMain.shellLogic.console.logException(ste);
        }
        catch (Exception e) {
            xMain.shellLogic.console.logException(e);
        }
        sSettings.tickReportClient = tickReport;
        System.out.println("client_update_"+gameTimeMillis);
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
            xMain.shellLogic.console.logException(e);
        }
    }

    private HashMap<String, String> getNetVars() {
        HashMap<String, String> keys = new HashMap<>();
        String outgoingCmd = dequeueNetCmd(); //dequeues w/ every call so call once a tick
        keys.put("cmd", outgoingCmd != null ? outgoingCmd.replace(",", "COMMA") : "");
        keys.put("cmdrcv", cmdReceived ? "1" : "0");
        //update id in net args
        keys.put("id", sSettings.uuid);
        keys.put("color", sSettings.clientPlayerColor);
        keys.put("name", sSettings.clientPlayerName.replace(",", "COMMA"));
        gPlayer userPlayer = xMain.shellLogic.getUserPlayer();
        //userplayer vars like coords and dirs and weapon
        if(userPlayer != null) {
            keys.put("fv", Double.toString(userPlayer.fv).substring(0, Math.min(Double.toString(userPlayer.fv).length(), 4)));
            keys.put("mov0", Integer.toString(userPlayer.mov0));
            keys.put("mov1", Integer.toString(userPlayer.mov1));
            keys.put("mov2", Integer.toString(userPlayer.mov2));
            keys.put("mov3", Integer.toString(userPlayer.mov3));
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
        if(!netSendCmds.isEmpty()) {
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
