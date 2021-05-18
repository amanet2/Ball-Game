import java.net.*;
import java.util.*;

public class nClient extends Thread {
    private int netticks;
    Queue<DatagramPacket> receivedPackets = new LinkedList<>();
    HashMap<String, HashMap<String, String>> serverArgsMap = new HashMap<>();
    ArrayList<String> serverIds = new ArrayList<>(); //insertion-ordered list of client ids
    HashMap<String, String> sendMap = new HashMap<>();
    private ArrayList<String> protectedArgs = new ArrayList<>(Arrays.asList("id", "cmdrcv", "cmd"));
    private Queue<String> netSendMsgs = new LinkedList<>();
    private Queue<String> netSendCmds = new LinkedList<>();
    private static nClient instance = null;
    private DatagramSocket clientSocket = null;

    public static nClient instance() {
        if(instance == null)
            instance = new nClient();
        return instance;
    }

    private nClient() {
        netticks = 0;
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
                String receiveDataString = new String(receivePacket.getData());
                xCon.instance().debug(String.format("CLIENT RCV [%d]: %s",
                        receiveDataString.trim().length(), receiveDataString.trim()));
                readData(receiveDataString);
                receivedPackets.remove();
            }
        }
        catch (Exception e) {
            eUtils.echoException(e);
            e.printStackTrace();
        }
    }

    public void run() {
        int retries = 0;
        try {
            while(sSettings.IS_CLIENT) {
                try {
                    netticks += 1;
                    if (uiInterface.nettickcounterTime < uiInterface.gameTime) {
                        uiInterface.netReport = netticks;
                        netticks = 0;
                        uiInterface.nettickcounterTime = uiInterface.gameTime + 1000;
                    }
                    if (receivedPackets.size() < 1) {
                        InetAddress IPAddress = InetAddress.getByName(sVars.get("joinip"));
                        String sendDataString = createSendDataString();
                        byte[] clientSendData = sendDataString.getBytes();
                        DatagramPacket sendPacket = new DatagramPacket(clientSendData, clientSendData.length, IPAddress,
                                sVars.getInt("joinport"));
                        if (clientSocket == null || clientSocket.isClosed()) {
                            clientSocket = new DatagramSocket();
                            clientSocket.setSoTimeout(sVars.getInt("timeout"));
                        }
                        clientSocket.send(sendPacket);
                        xCon.instance().debug("CLIENT SND [" + clientSendData.length + "]:" + sendDataString);
                        byte[] clientReceiveData = new byte[sVars.getInt("rcvbytesclient")];
                        DatagramPacket receivePacket = new DatagramPacket(clientReceiveData, clientReceiveData.length);
                        clientSocket.receive(receivePacket);
                        receivedPackets.add(receivePacket);
                    }
                    processPackets();
                    long networkTime = System.currentTimeMillis()
                            + (long) (1000.0 / (double) sVars.getInt("rateclient"));
                    sleep(Math.max(0, networkTime - uiInterface.gameTime));
                    retries = 0;
                }
                catch (Exception ee) {
                    eUtils.echoException(ee);
                    ee.printStackTrace();
                    retries++;
                    if(retries > sVars.getInt("netrcvretries")) {
                        xCon.ex("disconnect");
                        xCon.ex("echo Lost connection to server");
                    }
                }
            }
            interrupt();
        }
        catch(Exception e) {
            eUtils.echoException(e);
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
        //userplayer vars like coords and dirs and weapon
        if(userPlayer != null) {
            keys.put("color", sVars.get("playercolor"));
            keys.put("x", userPlayer.get("coordx"));
            keys.put("y", userPlayer.get("coordy"));
            keys.put("fv", userPlayer.get("fv").substring(0, Math.min(userPlayer.get("fv").length(), 4)));
            keys.put("vels", String.format("%s-%s-%s-%s", userPlayer.get("vel0"), userPlayer.get("vel1"),
                    userPlayer.get("vel2"), userPlayer.get("vel3")));
        }
        //name for spectator and gameplay
        keys.put("name", sVars.get("playername"));
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
        if(serverArgsMap.containsKey(uiInterface.uuid)) {
            for (String k : serverArgsMap.get(uiInterface.uuid).keySet()) {
                if (!protectedArgs.contains(k) && serverArgsMap.get(uiInterface.uuid).containsKey(k)
                        && serverArgsMap.get(uiInterface.uuid).get(k).equals(sendMap.get(k))) {
                    workingMap.remove(k);
                }
            }
        }
        sendDataString = new StringBuilder(workingMap.toString());
        //handle removing variables after the fact
        sendMap.remove("cmd");
        return sendDataString.toString().replace(", ", ",");
    }

    private void processCmd(String cmdload) {
        sendMap.put("cmdrcv","");
        xCon.ex(cmdload);
    }

    public void readData(String receiveDataString) {
        String[] argsets = receiveDataString.trim().split("@");
        ArrayList<String> foundIds = new ArrayList<>();
        for(int i = 0; i < argsets.length; i++) {
            String argload = argsets[i];
            HashMap<String, String> packArgs = nVars.getMapFromNetString(argload);
            String idload = packArgs.get("id");
            if(!serverArgsMap.containsKey(idload))
                serverArgsMap.put(idload, packArgs);
            else {
                for (String k : packArgs.keySet()) {
                    serverArgsMap.get(idload).put(k, packArgs.get(k));
                }
            }
            if(idload.equals("server")) {
                cVars.put("timeleft", packArgs.get("time"));
                //check flag and virus
                for(String s : new String[]{"flagmasterid", "virusids"}) {
                    if(!packArgs.containsKey(s))
                        serverArgsMap.get("server").remove(s);
                }
                //check cmd from server only
                String cmdload = packArgs.get("cmd") != null ? packArgs.get("cmd") : "";
                if(cmdload.length() > 0) {
//                    System.out.println("FROM_SERVER: " + cmdload);
                    processCmd(cmdload);
                }
            }
            else if(!idload.equals(uiInterface.uuid)) {
                if(serverIds.contains(idload)) {
                    foundIds.add(idload);
                    String[] requiredFields = new String[]{"x", "y", "vels"};
                    boolean skip = false;
                    for(String rf : requiredFields) {
                        if(!serverArgsMap.get(idload).containsKey(rf)) {
                            skip = true;
                            break;
                        }
                    }
                    if(skip)
                        continue;
                    if(cClientLogic.getPlayerById(idload) != null) {
                        if (sVars.isOne("smoothing")) {
                            cClientLogic.getPlayerById(idload).put("coordx", serverArgsMap.get(idload).get("x"));
                            cClientLogic.getPlayerById(idload).put("coordy", serverArgsMap.get(idload).get("y"));
                        }
                        String[] veltoks = serverArgsMap.get(idload).get("vels").split("-");
                        for (int vel = 0; vel < veltoks.length; vel++) {
                            cClientLogic.getPlayerById(idload).put("vel" + vel, veltoks[vel]);
                        }
                    }
                }
                else {
                    serverIds.add(idload);
                    foundIds.add(idload);
                }
            }
            if(idload.equals(uiInterface.uuid)){ // handle our own player to get things like stockhp from server
                gPlayer userPlayer = cClientLogic.getUserPlayer();
                if(userPlayer != null && packArgs.containsKey("hp"))
                    userPlayer.put("stockhp", packArgs.get("hp"));
                if(packArgs.containsKey("cmdrcv"))
                    sendMap.remove("cmdrcv");
            }
        }
        //check for ids that have been taken out of the server argmap
        String tr = "";
        for(String s : serverIds) {
            if(!foundIds.contains(s)) {
                tr = s;
            }
        }
        if(tr.length() > 0) {
            serverArgsMap.remove(tr);
            gScoreboard.scoresMap.remove(tr);
            serverIds.remove(tr);
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
//            System.out.println("TO_SERVER: " + cmdString);
            return netSendCmds.remove();
        }
        return null;
    }

    boolean containsArgsForId(String id, String[] fields) {
        if(!serverArgsMap.containsKey(id))
            return false;
        HashMap<String, String> cargs = serverArgsMap.get(id);
        for(String rf : fields) {
            if(!cargs.containsKey(rf))
                return false;
        }
        return true;
    }

    public void disconnect() {
        sSettings.IS_CLIENT = false;
        clientSocket.close();
        serverArgsMap = new HashMap<>();
        serverIds = new ArrayList<>();
    }
}
