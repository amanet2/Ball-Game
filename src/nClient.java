import java.net.*;
import java.util.*;

public class nClient extends Thread implements fNetBase {
    private int netticks;
    private int hasDisconnected = 0;
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
        while(true) {
            try {
                netticks += 1;
                if(uiInterface.nettickcounterTime < uiInterface.gameTime) {
                    uiInterface.netReport = netticks;
                    netticks = 0;
                    uiInterface.nettickcounterTime = uiInterface.gameTime + 1000;
                }
                if(receivedPackets.size() < 1 && isDisconnected()) {
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
                    HashMap<String, String> clientmap = nVars.getMapFromNetString(sendDataString);
                    if(clientmap.keySet().contains("quit") || clientmap.keySet().contains("disconnect")) {
                        setDisconnected(1);
                    }
                    else {
                        byte[] clientReceiveData = new byte[sVars.getInt("rcvbytesclient")];
                        DatagramPacket receivePacket = new DatagramPacket(clientReceiveData, clientReceiveData.length);
                        clientSocket.receive(receivePacket);
                        receivedPackets.add(receivePacket);
                    }
                }
                uiInterface.networkTime = System.currentTimeMillis()
                        + (long)(1000.0/(double)sVars.getInt("rateclient"));
                sleep(Math.max(0, uiInterface.networkTime-uiInterface.gameTime));
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
    }

    private String createSendDataString() {
        StringBuilder sendDataString;
        nVars.update();
        if(nSend.sendMap != null) {
            for(String k : nVars.keySet()) {
                if(k.equals("id") || !nSend.sendMap.containsKey(k) || !nSend.sendMap.get(k).equals(nVars.get(k)))
                    nSend.sendMap.put(k, nVars.get(k));
                else
                    nSend.sendMap.remove(k);
            }
        }
        else
            nSend.sendMap = nVars.copy();

        sendDataString = new StringBuilder(nSend.sendMap.toString());
        //handle removing variables after the fact
        nSend.sendMap.remove("netcmdrcv");
        cVars.put("quitconfirmed", cVars.get("quitting"));
        cVars.put("disconnectconfirmed", cVars.get("disconnecting"));
        if(cGameLogic.userPlayer() != null && cGameLogic.userPlayer().contains("spawnprotectiontime"))
            nSend.sendMap.put("spawnprotected","");
        else
            nSend.sendMap.remove("spawnprotected");
        return sendDataString.toString();
    }

    private void readData(String receiveDataString) {
        String[] toks = receiveDataString.trim().split("@");
        int ctr = 0;
        ArrayList<String> foundIds = new ArrayList<>();
        for(int i = 0; i < toks.length; i++) {
            String argload = toks[i];
            HashMap<String, String> packArgs = nVars.getMapFromNetString(argload);
            HashMap<String, HashMap<String, Integer>> scoresMap = cScoreboard.scoresMap;
            String idload = packArgs.get("id");
//            String nameload = packArgs.get("name") != null ? packArgs.get("name")
//                    : nServer.instance().clientArgsMap.containsKey(idload) ? nServer.instance().clientArgsMap.get(idload).get("name")
//                    : "player";
            String actionload = packArgs.get("act") != null ? packArgs.get("act") : "";
            if(!nServer.instance().clientArgsMap.containsKey(idload))
                nServer.instance().clientArgsMap.put(idload, packArgs);
            if(!scoresMap.containsKey(idload)) {
                cScoreboard.addId(idload);
            }
            for(String k : packArgs.keySet()) {
                if(!nServer.instance().clientArgsMap.get(idload).containsKey(k)
                        || !nServer.instance().clientArgsMap.get(idload).get(k).equals(packArgs.get(k))) {
                    nServer.instance().clientArgsMap.get(idload).put(k, packArgs.get(k));
                }
            }
            //detect a win message from the server and cancel all movements
            if(idload.equals("server")) {
//                //check if we're somehow on the wrong map
//                if(!packArgs.get("map").contains(eManager.currentMap.mapName)) {
//                    xCon.ex(String.format("load %s", packArgs.get("map") + sVars.get("mapextension")));
//                    xCon.ex("respawn");
//                }
                //check for end of game
                if(packArgs.get("win").length() > 0) {
                    cVars.put("winnerid", packArgs.get("win"));
                }
                else if(cVars.get("winnerid").length() > 0){
                    cVars.put("winnerid", "");
                }
                //important
                cPowerups.processPowerupStringClient(packArgs.get("powerups"));
                cVars.put("gamemode", packArgs.get("mode"));
                cVars.put("gameteam", packArgs.get("teams"));
                cVars.put("scorelimit", packArgs.get("scorelimit"));
                cVars.put("gravity", packArgs.get("gravity"));
                cVars.put("gametick", packArgs.get("tick"));
                cVars.put("timeleft", packArgs.get("timeleft"));
                cVars.put("spawnprotectionmaxtime", packArgs.get("spmaxtime"));
                //check cmd from server only
                String cmdload = packArgs.get("cmd") != null ? packArgs.get("cmd") : "";
                if(cmdload.length() > 0) {
                    System.out.println("FROM_SERVER: " + cmdload);
                    cClient.processCmd(cmdload);
                }
                //ugly if else for gamemodes
//                if(packArgs.containsKey("flagmasterid")) {
//                    cVars.put("flagmasterid", packArgs.get("flagmasterid"));
//                }
//                if(packArgs.get("state").contains("safezone")) {
//                    HashMap scorepointsMap = eManager.currentMap.scene.getThingMap("PROP_SCOREPOINT");
//                    String[] args = packArgs.get("state").split("-");
//                    for(Object id : scorepointsMap.keySet()) {
//                        gProp pr = (gProp) scorepointsMap.get(id);
//                        if(pr.isVal("tag", args[1]))
//                            pr.put("int0", "1");
//                        else
//                            pr.put("int0", "0");
//                    }
//                    cVars.put("safezonetime", packArgs.get("state").split("-")[2]);
//                }
//                if(packArgs.get("state").contains("waypoints")) {
//                    HashMap scorepointsMap = eManager.currentMap.scene.getThingMap("PROP_SCOREPOINT");
//                    String[] args = packArgs.get("state").split("-");
//                    for(Object id : scorepointsMap.keySet()) {
//                        gProp pr = (gProp) scorepointsMap.get(id);
//                        pr.put("int0", "0");
//                        if(pr.isVal("tag", args[1]))
//                            pr.put("int0", "1");
//                    }
//                }
//                if(packArgs.get("state").contains("kingofflags")) {
//                    //read kingofflags for client
//                    String flagidstr = packArgs.get("state").replace("kingofflags","");
//                    String[] kingids = flagidstr.split(":");
//                    for(int c = 0; c < kingids.length;c++) {
//                        String[] kofidpair = kingids[c].split("-");
//                        HashMap<String, gThing> thingMap =
//                                eManager.currentMap.scene.getThingMap("PROP_FLAGRED");
//                        for(String id : thingMap.keySet()) {
//                            if(thingMap.get(id).isVal("tag", kofidpair[0])
//                                    && !thingMap.get(id).isVal("str0", kofidpair[1])) {
//                                thingMap.get(id).put("str0", kofidpair[1]);
//                            }
//                        }
//                    }
//                }
                //end ugly if else
            }
            else if(!idload.equals(uiInterface.uuid)) {
                if(nServer.instance().clientIds.contains(idload)) {
                    ctr ++;
                    foundIds.add(idload);
//                    String[] requiredFields = new String[]{"name", "x", "y", "vels"};
                    String[] requiredFields = new String[]{"x", "y", "vels"};
                    boolean skip = false;
                    for(String rf : requiredFields) {
                        if(!nServer.instance().clientArgsMap.get(idload).containsKey(rf)) {
                            skip = true;
                            break;
                        }
                    }
                    if(skip)
                        break;
//                    String clientname = nServer.instance().clientArgsMap.get(idload).get("name");
//                    if(!clientname.equals(nameload))
//                        gScene.getPlayerById(idload).put("name", nameload);
                    if(sVars.isOne("smoothing")) {
                        gScene.getPlayerById(idload).put("coordx", nServer.instance().clientArgsMap.get(idload).get("x"));
                        gScene.getPlayerById(idload).put("coordy", nServer.instance().clientArgsMap.get(idload).get("y"));
                    }
                    String[] veltoks = nServer.instance().clientArgsMap.get(idload).get("vels").split("-");
                    for(int vel = 0; vel < veltoks.length; vel++) {
                        gScene.getPlayerById(idload).put("vel"+vel, veltoks[vel]);
                    }
                    if(!packArgs.containsKey("spawnprotected")
                            && nServer.instance().clientArgsMap.get(idload).containsKey("spawnprotected")) {
                        nServer.instance().clientArgsMap.get(idload).remove("spawnprotected");
                    }
                    cClient.processActionLoadClient(actionload);
                }
                else {
                    nServer.instance().clientIds.add(idload);
                    ctr++;
                    gPlayer player = new gPlayer(-6000, -6000,150,150,
                            eUtils.getPath("animations/player_red/a03.png"));
                    player.put("id", idload);
                    player.putInt("tag", eManager.currentMap.scene.playersMap().size());
//                    player.put("name", nameload);
                    eManager.currentMap.scene.playersMap().put(idload, player);
                }
            }
            //handle our own player to get things like stockhp from server
            if(idload.equals(uiInterface.uuid)) {
                gPlayer userPlayer = cGameLogic.userPlayer();
                userPlayer.put("stockhp", packArgs.get("stockhp"));
            }
//            if(idload.equals("server")) {
//                //this is where we update scores on client
//                cVars.put("scoremap", packArgs.get("scoremap"));
//                String[] stoks = packArgs.get("scoremap").split(":");
//                for (int j = 0; j < stoks.length; j++) {
//                    String scoreid = stoks[j].split("-")[0];
//                    if(!scoresMap.containsKey(scoreid)) {
//                        cScoreboard.addId(scoreid);
//                    }
//                    HashMap<String, Integer> scoresMapIdMap = scoresMap.get(scoreid);
//                    if(scoresMapIdMap != null) {
//                        scoresMapIdMap.put("wins", Integer.parseInt(stoks[j].split("-")[1]));
//                        scoresMapIdMap.put("score", Integer.parseInt(stoks[j].split("-")[2]));
//                        scoresMapIdMap.put("kills", Integer.parseInt(stoks[j].split("-")[3]));
//                        scoresMapIdMap.put("ping", Integer.parseInt(stoks[j].split("-")[4]));
//                    }
//                }
//            }
        }
//        if(ctr < nServer.instance().clientIds.size()) {
//            String tr = "";
//            for(String s : nServer.instance().clientIds) {
//                if(!foundIds.contains(s)) {
//                    tr = s;
//                }
//            }
//            if(tr.length() > 0) {
//                nServer.instance().clientArgsMap.remove(tr);
//                cScoreboard.scoresMap.remove(tr);
////                nServer.instance().clientNetCmdMap.remove(tr);
//                nServer.instance().clientIds.remove(tr);
//                eManager.currentMap.scene.playersMap().remove(tr);
//            }
//        }
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
            if(cmdString.contains("fireweapon")) //handle special firing case
                xCon.ex(cmdString);
            return netSendCmds.remove();
        }
        return null;
    }

    void setDisconnected(int v) { //needs to be here
        hasDisconnected = v;
    }

    boolean isDisconnected() {
        return hasDisconnected == 0;
    }

    public void disconnect() {
        cVars.put("disconnecting", "0");
        clientSocket.close();
        if(isAlive())
            interrupt();
        sSettings.net_client = false;
        sSettings.NET_MODE = sSettings.NET_OFFLINE;
        nServer.instance().clientArgsMap = new HashMap<>();
        nServer.instance().clientIds = new ArrayList<>();
        xCon.ex("load " + sVars.get("defaultmap"));
        if (uiInterface.inplay)
            xCon.ex("pause");
    }
}
