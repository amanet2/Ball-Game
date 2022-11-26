import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Arrays;

public class nServer extends Thread {
    private static final int sendbatchsize = 320;
    private static final int timeout = 10000;
    private final Queue<DatagramPacket> receivedPackets = new LinkedList<>(); //packets from clients in order rcvd
    private final Queue<String> quitClientIds = new LinkedList<>(); //holds ids that are quitting
    HashMap<String, Long> banIds = new HashMap<>(); // ids mapped to the time to be allowed back
    nStateMap masterStateMap; //will be the source of truth for game state, messages, and console comms
    HashMap<String, Queue<String>> clientNetCmdMap = new HashMap<>(); //id maps to queue of cmds to be sent
    private final HashMap<String, String> clientCheckinMap; //track the timestamp of last received packet of a client
    private final HashMap<String, String> clientStateSnapshots; // use to make deltas when sending state to clients
    private final HashMap<String, gDoableCmd> clientCmdDoables = new HashMap<>(); //doables for handling client cmds
    ArrayList<String> voteSkipList = new ArrayList<>();    //map of skip votes
    private final Queue<String> serverLocalCmdQueue = new LinkedList<>(); //local cmd queue for server
    private static nServer instance = null;    //singleton-instance
    private DatagramSocket serverSocket = null;    //socket object
    //VERY IMPORTANT LIST. whats allowed to be done by the clients
    private static final ArrayList<String> legalClientCommands = new ArrayList<>(Arrays.asList(
            "deleteblock",
            "deleteitem",
            "deleteplayer",
            "deleteprefab",
            "setthing",
            "exec",
            "fireweapon",
            "putblock",
            "putitem",
            "requestdisconnect",
            "setnstate"
    ));

    public static nServer instance() {
        if(instance == null)
            instance = new nServer();
        return instance;
    }

    private nServer() {
        masterStateMap = new nStateMap();
        clientCheckinMap = new HashMap<>();
        clientStateSnapshots = new HashMap<>();
        clientCmdDoables.put("fireweapon",
                new gDoableCmd() {
                    void ex(String id, String cmd) {
                        addExcludingNetCmd(id+",server,",
                                cmd.replaceFirst("fireweapon", "cl_fireweapon"));
                        xCon.ex(cmd);
                    }
                });
        clientCmdDoables.put("setnstate",
                new gDoableCmd() {
                    void ex(String id, String cmd) {
                        xCon.ex(cmd);
                    }
                });
        clientCmdDoables.put("requestdisconnect",
                new gDoableCmd() {
                    void ex(String id, String cmd) {
                        quitClientIds.add(id);
                    }
                });

        for(String rcs : new String[]{"putblock", "putitem", "deleteblock", "deleteitem", "deleteprefab", "setthing"}) {
            clientCmdDoables.put(rcs,
                    new gDoableCmd() {
                        void ex(String id, String cmd) {
                            xCon.ex(cmd);
                            addExcludingNetCmd("server", cmd.replaceFirst(rcs, "cl_" + rcs));
                        }
                    });
        }
        clientCmdDoables.put("deleteplayer",
                new gDoableCmd() {
                    void ex(String id, String cmd) {
                        String[] toks = cmd.split(" ");
                        if(toks.length > 1) {
                            String reqid = toks[1];
                            if(reqid.equals(id)) //client can only remove itself
                                xCon.ex(cmd);
                            addExcludingNetCmd("server",
                                    cmd.replaceFirst("deleteplayer ", "cl_deleteplayer "));
                        }
                    }
                });
    }

    public void checkForUnhandledQuitters() {
        //other players
        for(String id : clientCheckinMap.keySet()) {
            long pt = Long.parseLong(clientCheckinMap.get(id));
            if(gTime.gameTime > pt + timeout)
                quitClientIds.add(id);
        }
        while(quitClientIds.size() > 0) {
            removeNetClient(quitClientIds.remove());
        }
    }

    void addExcludingNetCmd(String excludedids, String cmd) {
        //excludedids is comma-separated string of ids
        if(!excludedids.contains("server"))
            xCon.ex(cmd);
        for(String id : clientNetCmdMap.keySet()) {
            if(!excludedids.contains(id))
                addNetCmd(id, cmd);
        }
    }

    public void addNetCmd(String id, String cmd) {
        xCon.instance().debug("SERVER_ADDCOM_" + id + ": " + cmd);
        if(id.equalsIgnoreCase("server"))
            serverLocalCmdQueue.add(cmd);
        else
            addNetSendData(id, cmd);
    }

    public void addNetCmd(String cmd) {
        xCon.instance().debug("SERVER_ADDCOM_ALL: " + cmd);
        xCon.ex(cmd);
        addNetSendData(cmd);
    }

    private void addNetSendData(String id, String data) {
        clientNetCmdMap.get(id).add(data);
    }

    private void addNetSendData(String data) {
        for(String id: clientNetCmdMap.keySet()) {
            addNetSendData( id, data);
        }
    }

    public void checkLocalCmds() {
        if(serverLocalCmdQueue.size() > 0)
            xCon.ex(serverLocalCmdQueue.remove());
    }

    public void processPackets(long gameTimeMillis) {
        try {
            HashMap<String, String> netVars = getNetVars();
            if(receivedPackets.size() > 0) {
                DatagramPacket receivePacket = receivedPackets.peek();
                String receiveDataString = new String(receivePacket.getData());
                xCon.instance().debug("SERVER RCV [" + receiveDataString.trim().length() + "]: "
                        + receiveDataString.trim());
                //get the ip address of the client
                InetAddress addr = receivePacket.getAddress();
                int port = receivePacket.getPort();
                //read data of packet
                readData(receiveDataString); //and respond too
                //get player id of client
                HashMap<String, String> clientmap = nVars.getMapFromNetString(receiveDataString);
                String clientId = clientmap.get("id");
                if(clientId != null) {
                    //create response
                    String sendDataString = createSendDataString(netVars, clientId);
                    byte[] sendData = sendDataString.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, addr, port);
                    serverSocket.send(sendPacket);
                    xCon.instance().debug("SERVER_SEND_" + clientId + " [" + sendDataString.length() + "]: " + sendDataString);
                    xCon.instance().debug("SERVER_STATE_" + clientId + " [" + masterStateMap.toString());
                    xCon.instance().debug("SERVER_SEND_" + clientId + " [" + sendDataString.length() + "]: " + sendDataString);
                    if(sendDataString.length() > sSettings.max_packet_size)
                        System.out.println("*WARNING* PACKET LENGTH EXCEED " + sSettings.max_packet_size + " BYTES: "
                                + "SERVER_SEND_" + clientId + " [" + sendDataString.length() + "]: " + sendDataString);
                }
                receivedPackets.remove();
            }
            HashMap botsMap = cServerLogic.scene.getThingMap("THING_BOTPLAYER");
            if(botsMap.size() > 0 && cBotsLogic.bottime < gameTimeMillis) {
                cBotsLogic.bottime = gameTimeMillis + (long)(1000.0/(double)sSettings.ratebots);
                for(Object id : botsMap.keySet()) {
                    gPlayer p = (gPlayer) botsMap.get(id);
                    nVarsBot.update(p, gameTimeMillis);
                    String receiveDataString = nVarsBot.dumpArgsForId(p.get("id"));
                    xCon.instance().debug("SERVER RCV [" + receiveDataString.trim().length() + "]: "
                            + receiveDataString.trim());
                    readData(receiveDataString);
                    //get player id of client
                    HashMap<String, String> clientmap = nVars.getMapFromNetString(receiveDataString);
                    String clientId = clientmap.get("id");
                    //act as if responding
                    createSendDataString(netVars, clientId);
                }
            }
        }
        catch (Exception e) {
            eUtils.echoException(e);
            e.printStackTrace();
        }
    }

    public HashMap<String, String> getNetVars() {
        HashMap<String, String> keys = new HashMap<>();
        //handle outgoing cmd
        keys.put("cmd", "");
        //handle server outgoing cmds that loopback to the server
        checkLocalCmds();
        //send scores
        keys.put("time", Long.toString(cServerLogic.timeleft));
        return keys;
    }

    private String createSendDataString(HashMap<String, String> netVars, String clientid) {
        if(clientNetCmdMap.containsKey(clientid) && clientNetCmdMap.get(clientid).size() > 0)
            netVars.put("cmd", clientNetCmdMap.get(clientid).peek());
        //fetch old snapshot for client
        nStateMap deltaStateMap = new nStateMap(clientStateSnapshots.get(clientid)).getDelta(masterStateMap);
        //record the master state at last communication time
        clientStateSnapshots.put(clientid, masterStateMap.toString());
        //add server vars to the sending map
        deltaStateMap.put("server", new nState());
        for(String k : netVars.keySet()) {
            deltaStateMap.get("server").put(k, netVars.get(k));
        }
        return deltaStateMap.toString().replace(", ", ",");
    }

    void removeNetClient(String id) {
        String qn = masterStateMap.get(id).get("name");
        String qc = masterStateMap.get(id).get("color");
        clientCheckinMap.remove(id);
        masterStateMap.remove(id);
        clientNetCmdMap.remove(id);
        clientStateSnapshots.remove(id);
        gScoreboard.scoresMap.remove(id);
        cServerLogic.scene.getThingMap("THING_PLAYER").remove(id);
        addExcludingNetCmd("server", String.format("echo %s#%s left the game", qn, qc));
        if(masterStateMap.get(id).get("flag").equalsIgnoreCase("1")) {
            gPlayer player = cServerLogic.getPlayerById(id);
            addNetCmd(String.format("putitem ITEM_FLAG %d %d %d", cServerLogic.getNewItemId(),
                    player.getInt("coordx"), player.getInt("coordy")));
        }
    }

    public void run() {
        try {
            serverSocket = new DatagramSocket(cServerLogic.listenPort);
            while (sSettings.IS_SERVER) {
                try {
                    long gameTime = gTime.gameTime;
                    byte[] receiveData = new byte[sSettings.rcvbytesserver];
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    serverSocket.receive(receivePacket);
                    receivedPackets.add(receivePacket);
                    long networkTime = gameTime + (long) (1000.0 / (double) sSettings.rateserver);
                    processPackets(gameTime);
                    checkForUnhandledQuitters();
                    cServerLogic.gameLoop(gameTime);
                    sleep(Math.max(0, networkTime - gameTime));
                }
                catch (Exception e) {
                    eUtils.echoException(e);
                    e.printStackTrace();
                }
            }
            interrupt();
        }
        catch (Exception ee) {
            eUtils.echoException(ee);
            ee.printStackTrace();
        }
    }

    public void sendMapAndRespawn(String id) {
        sendMap(id);
        if(!sSettings.show_mapmaker_ui) //spawn in after finished loading
            xCon.ex("exec scripts/respawnnetplayer " + id);
//        xCon.ex("exec scripts/respawnnetplayerbackfill " + id);
    }

    public void handleJoin(String id) {
        masterStateMap.put(id, new nStateBallGame());
        clientNetCmdMap.put(id, new LinkedList<>());
        clientStateSnapshots.put(id, masterStateMap.toString());
        gScoreboard.addId(id);
        sendMapAndRespawn(id);
        // respawn the already-present players on the joining client
        for(String clientId : masterStateMap.keys()) {
            gThing player = cServerLogic.scene.getPlayerById(clientId);
            if(clientId.equals(id) || player == null)
                continue;
            addNetCmd(id, String.format("cl_spawnplayer %s %s %s", clientId,
                    player.get("coordx"), player.get("coordy")));
        }
        addExcludingNetCmd("server", String.format("echo %s#%s joined the game",
                masterStateMap.get(id).get("name"), masterStateMap.get(id).get("color")));
    }

    public void checkBanStatus(String stateId) {
        if(banIds.containsKey(stateId)) {
            if(banIds.get(stateId) < gTime.gameTime)
                banIds.remove(stateId);
            else {
                addNetCmd(stateId, "echo You are banned for " + (banIds.get(stateId) - gTime.gameTime) + "ms");
                addNetCmd(stateId, "disconnect");
            }
        }
    }

    public void readData(String receiveDataString) {
        if(receiveDataString.length() < 1)
            return;
        //load received string into state object
        nState receivedState = new nState(receiveDataString.trim());
        String stateId = receivedState.get("id");
        //relieve bans
//        checkBanStatus(stateId);
        //check if masterState contains
        if(!masterStateMap.contains(stateId))
            handleJoin(stateId);
        //record checkin time for client
        clientCheckinMap.put(stateId, Long.toString(gTime.gameTime));
        //load the keys from received data into our state map
        for(String k : receivedState.keys()) {
            masterStateMap.get(stateId).put(k, receivedState.get(k));
        }
        //update players
        gPlayer pl = cServerLogic.getPlayerById(stateId);
        if(pl != null)    //store player object's health in outgoing network arg map
            masterStateMap.get(stateId).put("hp", cServerLogic.getPlayerById(stateId).get("stockhp"));
        //update scores
        masterStateMap.get(stateId).put("score",  String.format("%d:%d",
                gScoreboard.scoresMap.get(stateId).get("wins"), gScoreboard.scoresMap.get(stateId).get("score")));
    }
    
    public void sendMap(String packId) {
        //these three are always here
        ArrayList<String> maplines = new ArrayList<>();
        maplines.add(String.format("cv_velocityplayer %d;cv_maploaded 0;cv_gamemode %d\n",
                cServerLogic.velocityplayerbase, cClientLogic.gamemode));
        HashMap<String, gThing> blockMap = cServerLogic.scene.getThingMap("THING_BLOCK");
        for(String id : blockMap.keySet()) {
            gBlock block = (gBlock) blockMap.get(id);
            String[] args = new String[]{
                    block.get("type"),
                    block.get("id"),
                    block.get("prefabid"),
                    block.get("coordx"),
                    block.get("coordy"),
                    block.get("dimw"),
                    block.get("dimh"),
                    block.get("toph"),
                    block.get("wallh")
            };
            StringBuilder blockString = new StringBuilder("cl_putblock");
            for(String arg : args) {
                if(arg != null)
                    blockString.append(" ").append(arg);
            }
            maplines.add(blockString.toString());
        }
        HashMap<String, gThing> itemMap = cServerLogic.scene.getThingMap("THING_ITEM");
        for(String id : itemMap.keySet()) {
            gItem item = (gItem) itemMap.get(id);
            String[] args = new String[]{
                    item.get("type"),
                    item.get("id"),
                    item.get("coordx"),
                    item.get("coordy")
            };
            StringBuilder str = new StringBuilder("cl_putitem");
            for(String arg : args) {
                if(arg != null)
                    str.append(" ").append(arg);
            }
            maplines.add(str.toString());
        }
        maplines.add("cv_maploaded 1");
        //iterate through the maplines and send in batches
        StringBuilder sendStringBuilder = new StringBuilder();
        int linectr = 0;
        for(int i = 0; i < maplines.size(); i++) {
            String line = maplines.get(i);
            String next = "";
            if(maplines.size() > i+1)
                next = maplines.get(i+1);
            sendStringBuilder.append(line).append(";");
            linectr++;
            if(sendStringBuilder.length() + next.length() >= sendbatchsize || linectr == maplines.size()) {
                String sendString = sendStringBuilder.toString();
                addNetCmd(packId, sendString.substring(0, sendString.lastIndexOf(';')));
                sendStringBuilder = new StringBuilder();
            }
        }
    }

    void handleClientCommand(String id, String cmd) {
        String ccmd = cmd.split(" ")[0];
        if(legalClientCommands.contains(ccmd)) {
            if(clientCmdDoables.containsKey(ccmd))
                clientCmdDoables.get(ccmd).ex(id, cmd);
            else if(cmd.startsWith("exec prefabs/")) {
                xCon.ex(cmd);
                addExcludingNetCmd("server", String.format("%s",
                        cmd.replace("exec ", "cl_exec ")));
            }
            else if(cmd.startsWith("exec scripts/respawnnetplayer"))
                xCon.ex(cmd);
            else
                addNetCmd(id, "echo NO HANDLER FOUND FOR CMD: " + cmd);
        }
        else
            addNetCmd(id, "echo ILLEGAL CMD REQUEST: " + cmd);
    }

    public void checkClientMessageForTimeAndVoteSkip(String id, String testmsg) {
        if(testmsg.strip().equalsIgnoreCase("thetime")) {
            addNetCmd(id, "echo the time is " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            return;
        }
        else if(!testmsg.strip().equalsIgnoreCase("skip"))
            return;
        if(voteSkipList.contains(id)) {
            addNetCmd(id, "echo [VOTE_SKIP] YOU HAVE ALREADY VOTED TO SKIP");
            return;
        }
        voteSkipList.add(id);
        int limit = Integer.parseInt(xCon.ex("setvar voteskiplimit"));
        if(voteSkipList.size() < limit) {
            addExcludingNetCmd("server", String.format("echo [VOTE_SKIP] SAY 'skip' TO END ROUND. (%s/%s)",
                    voteSkipList.size(), limit));
            return;
        }
        addExcludingNetCmd("server", String.format("playsound sounds/win/%s",
                eManager.winSoundFileSelection[(int)(Math.random() * eManager.winSoundFileSelection.length)]));
        xCon.ex("exec scripts/sv_voteskip");
    }

    public void sendMapToClients() {
        for(String id : masterStateMap.keys()) {
            sendMapAndRespawn(id);
        }
    }
}
