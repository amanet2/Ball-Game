import java.net.*;
import java.util.*;

public class nClient extends Thread {
    private int netticks;
    Queue<DatagramPacket> receivedPackets = new LinkedList<>();
    HashMap<String, HashMap<String, String>> serverArgsMap = new HashMap<>();
    ArrayList<String> serverIds = new ArrayList<>(); //insertion-ordered list of client ids
    HashMap<String, String> sendMap = null;
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
        while(sSettings.IS_CLIENT) {
            try {
                netticks += 1;
                if(uiInterface.nettickcounterTime < uiInterface.gameTime) {
                    uiInterface.netReport = netticks;
                    netticks = 0;
                    uiInterface.nettickcounterTime = uiInterface.gameTime + 1000;
                }
                if(receivedPackets.size() < 1) {
                    InetAddress IPAddress = InetAddress.getByName(sVars.get("joinip"));
                    String sendDataString = createSendDataString();
                    byte[] clientSendData = sendDataString.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(clientSendData, clientSendData.length, IPAddress,
                            sVars.getInt("joinport"));
                    if(clientSocket == null || clientSocket.isClosed()) {
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
                        + (long)(1000.0/(double)sVars.getInt("rateclient"));
                sleep(Math.max(0, networkTime-uiInterface.gameTime));
                retries = 0;
            }
            catch(Exception e) {
                eUtils.echoException(e);
                retries++;
                e.printStackTrace();
                if(retries > sVars.getInt("netrcvretries")) {
                    xCon.ex("disconnect");
                    xCon.ex("echo Lost connection to server");
                }
            }
        }
        interrupt();
    }

    private HashMap<String, String> getNetVars() {
        HashMap<String, String> keys = new HashMap<>();
        gPlayer userPlayer = cClientLogic.getUserPlayer();
        //handle outgoing msg
        String outgoingMsg = dequeueNetMsg(); //dequeues w/ every call so call once a tick
        keys.put("msg", outgoingMsg != null ? outgoingMsg : "");
        //handle outgoing cmd
        keys.put("cmd", "");
        String outgoingCmd = dequeueNetCmd(); //dequeues w/ every call so call once a tick
        keys.put("cmd", outgoingCmd != null ? outgoingCmd : "");
        //update id in net args
        keys.put("id", uiInterface.uuid);
        //userplayer vars like coords and dirs and weapon
        if(userPlayer != null) {
            keys.put("color", sVars.get("playercolor"));
            keys.put("hat", sVars.get("playerhat"));
            keys.put("x", userPlayer.get("coordx"));
            keys.put("y", userPlayer.get("coordy"));
            keys.put("fv", userPlayer.get("fv"));
            keys.put("dirs", String.format("%s%s%s%s", userPlayer.get("mov0"), userPlayer.get("mov1"),
                    userPlayer.get("mov2"), userPlayer.get("mov3")));
            keys.put("vels", String.format("%s-%s-%s-%s", userPlayer.get("vel0"), userPlayer.get("vel1"),
                    userPlayer.get("vel2"), userPlayer.get("vel3")));
            keys.put("weapon", userPlayer.get("weapon"));
        }
        //name for spectator and gameplay
        keys.put("name", sVars.get("playername"));
        return keys;
    }

    private String createSendDataString() {
        StringBuilder sendDataString;
        HashMap<String, String> netVars = getNetVars();
        if(sendMap != null) {
            for(String k : netVars.keySet()) {
                if(k.equals("id") || !sendMap.containsKey(k) || !sendMap.get(k).equals(netVars.get(k)))
                    sendMap.put(k, netVars.get(k));
                else
                    sendMap.remove(k);
            }
        }
        else
            sendMap = new HashMap<>(netVars);

        sendDataString = new StringBuilder(sendMap.toString());
        //handle removing variables after the fact
        sendMap.remove("netcmdrcv");
        return sendDataString.toString();
    }

    private void processCmd(String cmdload) {
        sendMap.put("netcmdrcv","");
        xCon.ex(cmdload);
    }

    public void readData(String receiveDataString) {
        String[] toks = receiveDataString.trim().split("@");
        ArrayList<String> foundIds = new ArrayList<>();
        for(int i = 0; i < toks.length; i++) {
            String argload = toks[i];
            HashMap<String, String> packArgs = nVars.getMapFromNetString(argload);
            String idload = packArgs.get("id");
            if(!serverArgsMap.containsKey(idload))
                serverArgsMap.put(idload, packArgs);
            for(String k : packArgs.keySet()) {
                if(!serverArgsMap.get(idload).containsKey(k)
                        || !serverArgsMap.get(idload).get(k).equals(packArgs.get(k))) {
                    serverArgsMap.get(idload).put(k, packArgs.get(k));
                }
            }
            //detect a win message from the server and cancel all movements
            if(idload.equals("server")) {
                //scorelimit
                //check for end of game
                if(packArgs.get("win").length() > 0) {
                    cVars.put("winnerid", packArgs.get("win"));
                }
                else if(cVars.get("winnerid").length() > 0){
                    cVars.put("winnerid", "");
                }
                //important
                cVars.put("scorelimit", packArgs.get("scorelimit"));
                cVars.put("timeleft", packArgs.get("timeleft"));
                //check cmd from server only
                String cmdload = packArgs.get("cmd") != null ? packArgs.get("cmd") : "";
                if(cmdload.length() > 0) {
                    System.out.println("FROM_SERVER: " + cmdload);
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
                        break;
                    //here we avoid client and server fighting over the player coords when hosting and playing
                    if(!sSettings.IS_SERVER && eManager.getPlayerById(idload) != null) {
                        if (sVars.isOne("smoothing")) {
                            eManager.getPlayerById(idload).put("coordx", serverArgsMap.get(idload).get("x"));
                            eManager.getPlayerById(idload).put("coordy", serverArgsMap.get(idload).get("y"));
                        }
                        String[] veltoks = serverArgsMap.get(idload).get("vels").split("-");
                        for (int vel = 0; vel < veltoks.length; vel++) {
                            eManager.getPlayerById(idload).put("vel" + vel, veltoks[vel]);
                        }
                    }
                    if(!packArgs.containsKey("spawnprotected")) {
                        serverArgsMap.get(idload).remove("spawnprotected");
                    }
                }
                else {
                    serverIds.add(idload);
                    foundIds.add(idload);
                    gPlayer player = new gPlayer(-6000, -6000,
                            eUtils.getPath("animations/player_red/a03.png"));
                    player.put("id", idload);
                    eManager.currentMap.scene.getThingMap("THING_PLAYER").put(idload, player);
                }
            }
            //handle our own player to get things like stockhp from server
            if(idload.equals(uiInterface.uuid)) {
                gPlayer userPlayer = cClientLogic.getUserPlayer();
                if(userPlayer != null)
                    userPlayer.put("stockhp", packArgs.get("stockhp"));
            }
            if(idload.equals("server")) {
                //this is where we update scores on client
                cVars.put("scoremap", packArgs.get("scoremap"));
                String[] stoks = packArgs.get("scoremap").split(":");
                for (int j = 0; j < stoks.length; j++) {
                    String[] sstoks = stoks[j].split("-");
                    String scoreid = sstoks[0];
                    if(scoreid.length() > 0) {
                        if (!gScoreboard.scoresMap.containsKey(scoreid)) {
                            gScoreboard.addId(scoreid);
                        }
                        HashMap<String, Integer> scoresMapIdMap = gScoreboard.scoresMap.get(scoreid);
                        if (scoresMapIdMap != null) {
                            scoresMapIdMap.put("wins", Integer.parseInt(sstoks[1]));
                            scoresMapIdMap.put("score", Integer.parseInt(sstoks[2]));
                            scoresMapIdMap.put("kills", Integer.parseInt(sstoks[3]));
                            scoresMapIdMap.put("ping", Integer.parseInt(sstoks[4]));
                        }
                    }
                }
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
            eManager.currentMap.scene.getThingMap("THING_PLAYER").remove(tr);
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
            //here we avoid server double shooting, and also handle the user's client-side firing (like in halo 5)
            if(!sSettings.IS_SERVER && cmdString.contains("fireweapon")) //handle special firing case
                xCon.ex(cmdString);
            System.out.println("TO_SERVER: " + cmdString);
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
        addNetCmd("requestdisconnect");
        sSettings.IS_CLIENT = false;
        clientSocket.close();
        serverArgsMap = new HashMap<>();
        serverIds = new ArrayList<>();
        xCon.ex("load");
        if (uiInterface.inplay)
            xCon.ex("pause");
    }
}
