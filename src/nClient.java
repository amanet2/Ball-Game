import java.net.*;
import java.util.*;

public class nClient extends Thread {
    private int netticks;
    static int hasDisconnected;
    static int msgreceived;
    static int sfxreceived;
    static int cmdreceived;
    static Queue<DatagramPacket> receivedPackets = new LinkedList<>();
    static Queue<String> netSendMsgs = new LinkedList<>();
    private static nClient instance = null;
    static DatagramSocket clientSocket = null;

    public static nClient instance() {
        if(instance == null)
            instance = new nClient();
        return instance;
    }

    private nClient() {
        netticks = 0;
        msgreceived = 0;
        sfxreceived = 0;
        cmdreceived = 0;
    }

    public static void addSendMsg(String msg) {
        netSendMsgs.add(msg);
    }

    public static void processPackets() {
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
                if(receivedPackets.size() < 1 && hasDisconnected != 1) {
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
                        hasDisconnected = 1;
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
                if(retries > sVars.getInt("netrcvretries"))
                {
                    xCon.ex("disconnect");
                    xCon.ex("echo Lost connection to server");
                }
            }
        }
    }

    public static String createSendDataString() {
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

        if(nClient.msgreceived != 0) {
            nSend.sendMap.put("netmsgrcv","");
            nClient.msgreceived = 0;
        }
        else
            nSend.sendMap.remove("netmsgrcv");

        if(nClient.sfxreceived != 0) {
            nSend.sendMap.put("netsfxrcv","");
            nClient.sfxreceived = 0;
        }
        else
            nSend.sendMap.remove("netsfxrcv");

        if(nClient.cmdreceived != 0) {
            nSend.sendMap.put("netcmdrcv","");
            nClient.cmdreceived = 0;
        }
        else
            nSend.sendMap.remove("netcmdrcv");

        sendDataString = new StringBuilder(nSend.sendMap.toString());
        cVars.put("quitconfirmed", cVars.get("quitting"));
        cVars.put("disconnectconfirmed", cVars.get("disconnecting"));
        if(cGameLogic.userPlayer() != null && cGameLogic.userPlayer().contains("spawnprotectiontime"))
            nSend.sendMap.put("spawnprotected","");
        else
            nSend.sendMap.remove("spawnprotected");
        cVars.put("exploded", "1");
        cVars.put("sendsafezone", "0");
        return sendDataString.toString();
    }

    public static void readData(String receiveDataString) {
        String[] toks = receiveDataString.trim().split("@");
        int ctr = 0;
        ArrayList<String> foundIds = new ArrayList<>();
        for(int i = 0; i < toks.length; i++) {
            String argload = toks[i];
            HashMap<String, String> packArgs = nVars.getMapFromNetString(argload);
            HashMap<String, HashMap<String, Integer>> scoresMap = cScoreboard.scoresMap;
            String idload = packArgs.get("id");
            String nameload = packArgs.get("name") != null ? packArgs.get("name")
                    : nServer.clientArgsMap.containsKey(idload) ? nServer.clientArgsMap.get(idload).get("name")
                    : "player";
            String actionload = packArgs.get("act") != null ? packArgs.get("act") : "";
            if(!nServer.clientArgsMap.containsKey(idload))
                nServer.clientArgsMap.put(idload, packArgs);
            if(!scoresMap.containsKey(idload)) {
                cScoreboard.addId(idload);
            }
            for(String k : packArgs.keySet()) {
                if(!nServer.clientArgsMap.get(idload).containsKey(k)
                        || nServer.clientArgsMap.get(idload).get(k) != packArgs.get(k)) {
                    nServer.clientArgsMap.get(idload).put(k, packArgs.get(k));
                }
            }
            //detect a win message from the server and cancel all movements
            if(idload.equals("server")) {
                if(packArgs.get("win").length() > 0) {
                    cVars.put("winnerid", packArgs.get("win"));
                    gPlayer userPlayer = cGameLogic.userPlayer();
                    for(int d = 0; d < 4; d++) {
                        if(!(d == 1 && cVars.getInt("mapview") == gMap.MAP_SIDEVIEW)) {
                            //disable the movements for sidescroller maps
                            userPlayer.put("vel"+d, "0");
                            userPlayer.put("mov"+d, "0");
                        }
                    }
                }
                else if(cVars.get("winnerid").length() > 0){
                    cVars.put("winnerid", "");
                }
                //ugly if else for gamemodes
                if(packArgs.containsKey("flagmasterid")) {
                    cVars.put("flagmasterid", packArgs.get("flagmasterid"));
                }
                if(packArgs.get("state").contains("safezone")) {
                    HashMap scorepointsMap = eManager.currentMap.scene.getThingMap("PROP_SCOREPOINT");
                    String[] args = packArgs.get("state").split("-");
                    for(Object id : scorepointsMap.keySet()) {
                        gProp pr = (gProp) scorepointsMap.get(id);
                        if(pr.isVal("tag", args[1]))
                            pr.put("int0", "1");
                        else
                            pr.put("int0", "0");
                    }
                    cVars.put("safezonetime", packArgs.get("state").split("-")[2]);
                }
                if(packArgs.get("state").contains("waypoints")) {
                    HashMap scorepointsMap = eManager.currentMap.scene.getThingMap("PROP_SCOREPOINT");
                    String[] args = packArgs.get("state").split("-");
                    for(Object id : scorepointsMap.keySet()) {
                        gProp pr = (gProp) scorepointsMap.get(id);
                        if(pr.isVal("tag", args[1]))
                            pr.put("int0", "1");
                        else
                            pr.put("int0", "0");
                    }
                }
                if(packArgs.get("state").contains("kingofflags")) {
                    //read kingofflags for client
                    String flagidstr = packArgs.get("state").replace("kingofflags","");
                    String[] kingids = flagidstr.split(":");
                    for(int c = 0; c < kingids.length;c++) {
                        String[] kofidpair = kingids[c].split("-");
                        HashMap<String, gThing> thingMap =
                                eManager.currentMap.scene.getThingMap("PROP_FLAGRED");
                        for(String id : thingMap.keySet()) {
                            if(thingMap.get(id).isVal("tag", kofidpair[0])
                                    && !thingMap.get(id).isVal("str0", kofidpair[1])) {
                                thingMap.get(id).put("str0", kofidpair[1]);
                            }
                        }
                    }
                }
                //end ugly if else
                cPowerups.processPowerupStringClient(packArgs.get("powerups"));
                cVars.put("gamemode", packArgs.get("mode"));
                cVars.put("gameteam", packArgs.get("teams"));
                cVars.put("gamespawnarmed", packArgs.get("armed"));
                cVars.put("scorelimit", packArgs.get("scorelimit"));
                cVars.put("gravity", packArgs.get("gravity"));
                cVars.put("allowweaponreload", packArgs.get("allowweaponreload"));
                cVars.put("gametick", packArgs.get("tick"));
                cVars.put("timeleft", packArgs.get("timeleft"));
                cVars.put("spawnprotectionmaxtime", packArgs.get("spmaxtime"));
                //check cmd from server only
                String cmdload = packArgs.get("cmd") != null ? packArgs.get("cmd") : "";
                if(cmdload.length() > 0) {
                    cClient.processCmd(cmdload);
                    System.out.println(cmdload);
                    if(cmdload.contains(":")) {
                        checkSpecialSound(cmdload);
                    }
                }
            }
            if(!idload.equals(uiInterface.uuid)) {
                int isnewclient = 1;
                if(nServer.clientIds.contains(idload)) {
                    ctr ++;
                    foundIds.add(idload);
                    String[] requiredFields = new String[]{"name", "x", "y", "vels"};
                    boolean skip = false;
                    for(String rf : requiredFields) {
                        if(!nServer.clientArgsMap.get(idload).containsKey(rf)) {
                            skip = true;
                            break;
                        }
                    }
                    if(skip)
                        break;
                    String clientname = nServer.clientArgsMap.get(idload).get("name");
                    if(!clientname.equals(nameload))
                        gScene.getPlayerById(idload).put("name", nameload);
                    if(sVars.isOne("smoothing")) {
                        gScene.getPlayerById(idload).put("coordx", nServer.clientArgsMap.get(idload).get("x"));
                        gScene.getPlayerById(idload).put("coordy", nServer.clientArgsMap.get(idload).get("y"));
                    }
                    String[] veltoks = nServer.clientArgsMap.get(idload).get("vels").split("-");
                    for(int vel = 0; vel < veltoks.length; vel++) {
                        gScene.getPlayerById(idload).put("vel"+vel, veltoks[vel]);
                    }
                    isnewclient = 0;
                    if(packArgs.containsKey("kick") && packArgs.get("kick").equals(uiInterface.uuid)) {
                        xCon.ex("disconnect");
                        xCon.ex("echo you have been kicked by the server");
                    }
                    if(!packArgs.containsKey("spawnprotected")
                            && nServer.clientArgsMap.get(idload).containsKey("spawnprotected")) {
                        nServer.clientArgsMap.get(idload).remove("spawnprotected");
                    }
                    cClient.processActionLoadClient(actionload);
                }
                if(isnewclient == 1){
                    nServer.clientIds.add(idload);
                    ctr++;
                    gPlayer player = new gPlayer(-6000, -6000,150,150,
                            eUtils.getPath("animations/player_red/a03.png"));
                    player.put("id", idload);
                    player.putInt("tag", eManager.currentMap.scene.playersMap().size());
                    player.put("name", nameload);
                    eManager.currentMap.scene.playersMap().put(idload, player);
                }
            }
            //handle our own player to get things like stockhp from server
            if(idload.equals(uiInterface.uuid)) {
                gPlayer userPlayer = cGameLogic.userPlayer();
                int oldstockhp = userPlayer.getInt("stockhp");
                userPlayer.put("stockhp", packArgs.get("stockhp"));
                //detect old stockhp higher than recent
                if(userPlayer.getInt("stockhp") < oldstockhp) {
                    cScripts.processUserPlayerHPLoss();
                }

            }
            if(idload.equals("server")) {
                //this is where we update scores on client
                cVars.put("scoremap", packArgs.get("scoremap"));
                String[] stoks = packArgs.get("scoremap").split(":");
                for (int j = 0; j < stoks.length; j++) {
                    String scoreid = stoks[j].split("-")[0];
                    if(!scoresMap.containsKey(scoreid)) {
                        cScoreboard.addId(scoreid);
                    }
                    HashMap<String, Integer> scoresMapIdMap = scoresMap.get(scoreid);
                    if(scoresMapIdMap != null) {
                        scoresMapIdMap.put("wins", Integer.parseInt(stoks[j].split("-")[1]));
                        scoresMapIdMap.put("score", Integer.parseInt(stoks[j].split("-")[2]));
                        scoresMapIdMap.put("kills", Integer.parseInt(stoks[j].split("-")[3]));
                        scoresMapIdMap.put("ping", Integer.parseInt(stoks[j].split("-")[4]));
                    }
                }
            }
        }
        if(ctr < nServer.clientIds.size()) {
            String tr = "";
            for(String s : nServer.clientIds) {
                if(!foundIds.contains(s)) {
                    tr = s;
                }
            }
            if(tr.length() > 0) {
                nServer.clientArgsMap.remove(tr);
                cScoreboard.scoresMap.remove(tr);
                nServer.clientSendCmdQueues.remove(tr);
                nServer.clientSendMsgQueues.remove(tr);
                nServer.clientIds.remove(tr);
                eManager.currentMap.scene.playersMap().remove(tr);
            }
        }
    }

    public static void checkSpecialSound(String msg) {
        nClient.msgreceived = 1;
        String testmsg = msg.substring(msg.indexOf(':')+2);
        System.out.println(testmsg);
        for(String s : eManager.winClipSelection) {
            String[] ttoks = s.split("\\.");
            if(testmsg.equalsIgnoreCase(ttoks[0])) {
                String soundString = "playsound sounds/win/" + s;
                xCon.ex(soundString);
                break;
            }
        }
    }
}
