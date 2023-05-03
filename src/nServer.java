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
    private int ticks = 0;
    private long nextSecondNanos = 0;
    private static final int sendbatchsize = 320;
    private static final int timeout = 10000;
    private final Queue<DatagramPacket> receivedPackets = new LinkedList<>(); //packets from clients in order rcvd
    private final Queue<String> quitClientIds = new LinkedList<>(); //holds ids that are quitting
    HashMap<String, Long> banIds = new HashMap<>(); // ids mapped to the time to be allowed back
    nStateMap masterStateMap; //will be the source of truth for game state, messages, and console comms
    HashMap<String, Queue<String>> clientNetCmdMap = new HashMap<>(); //id maps to queue of cmds to be sent
    private final HashMap<String, String> clientCheckinMap; //track the timestamp of last received packet of a client
    final HashMap<String, String> clientStateSnapshots; // use to make deltas when sending state to clients
    private final HashMap<String, gDoableCmd> clientCmdDoables = new HashMap<>(); //doables for handling client cmds
    ArrayList<String> voteSkipList = new ArrayList<>();    //map of skip votes
    private final Queue<String> serverLocalCmdQueue = new LinkedList<>(); //local cmd queue for server
    private static nServer instance = null;    //singleton-instance
    public DatagramSocket serverSocket = null;    //socket object

    public static nServer instance() {
        if(instance == null)
            instance = new nServer();
        return instance;
    }

    public static void refreshInstance() {
        instance = new nServer();
    }

    private nServer() {
        masterStateMap = new nStateMap();
        clientCheckinMap = new HashMap<>();
        clientStateSnapshots = new HashMap<>();
        clientCmdDoables.put("fireweapon",
                new gDoableCmd() {
                    void ex(String id, String cmd) {
                        xCon.ex(cmd);
                        addExcludingNetCmd(id+",server,",
                                cmd.replaceFirst("fireweapon", "cl_fireweapon"));
                    }
                });
        clientCmdDoables.put("requestdisconnect",
                new gDoableCmd() {
                    void ex(String id, String cmd) {
                        quitClientIds.add(id);
                    }
                });
        clientCmdDoables.put("setthing", // dont want EVERY setthing on server to be synced, only ones requested here
                new gDoableCmd() {
                    void ex(String id, String cmd) {
                        xCon.ex(cmd);
                        addExcludingNetCmd("server", "cl_" + cmd);
                    }
                });
        for(String rcs : new String[]{
                "respawnnetplayer", "setnstate", "putblock", "putitem", "deleteblock", "deleteitem",
                "gamemode", "deleteprefab"
        }) {
            clientCmdDoables.put(rcs,
                    new gDoableCmd() {
                        void ex(String id, String cmd) {
                            xCon.ex(cmd);
                        }
                    });
        }
        clientCmdDoables.put("deleteplayer",
                new gDoableCmd() {
                    void ex(String id, String cmd) {
                        String[] toks = cmd.split(" ");
                        if(toks.length > 1 && toks[1].equals(id)) //client can only remove itself
                            xCon.ex(cmd);
                    }
                });
        clientCmdDoables.put("exec",
                new gDoableCmd() {
                    void ex(String id, String cmd) {
                        String[] toks = cmd.split(" ");
                        if(toks.length > 1 && toks[1].startsWith("prefabs/")) //client can only add prefabs
                            xCon.ex(cmd);
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
        //excludedids is any-char separated string of ids
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
        if(serverLocalCmdQueue.peek() != null)
            xCon.ex(serverLocalCmdQueue.remove());
    }

    public void processPackets() {
            HashMap<String, String> netVars = getNetVars();
//            if(receivedPackets.size() > 0) {
                try {
                    DatagramPacket receivePacket = receivedPackets.peek();
                    if(receivePacket == null)
                        return;
                    String receiveDataString = new String(receivePacket.getData());
                    receivedPackets.remove();
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
                        xCon.instance().debug("SERVER_STATE_" + clientId + " [" + masterStateMap.toString());
                        xCon.instance().debug("SERVER_SEND_" + clientId + " [" + sendDataString.length() + "]: " + sendDataString);
                        if(sendDataString.length() > sSettings.max_packet_size)
                            System.out.println("*WARNING* PACKET LENGTH EXCEED " + sSettings.max_packet_size + " BYTES: "
                                    + "SERVER_SEND_" + clientId + " [" + sendDataString.length() + "]: " + sendDataString);
                    }
                }
                catch (Exception e) {
                    eLogging.logException(e);
                    e.printStackTrace();
                }
//                receivedPackets.remove();
//            }
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
//        nStateMap deltaStateMap = new nStateMap(clientStateSnapshots.get(clientid)).getDelta(masterStateMap);
        nStateMap deltaStateMap = new nStateMap(clientStateSnapshots.get(clientid));
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
        clientCheckinMap.remove(id);
        masterStateMap.remove(id);
        clientNetCmdMap.remove(id);
        gScoreboard.scoresMap.remove(id);
        nState snap = new nState(clientStateSnapshots.get(id));
        addExcludingNetCmd("server", String.format("%s#%s left the game", snap.get("name"), snap.get("color")));
        xCon.ex("deleteplayer " + id);
        xCon.ex("exec scripts/sv_handleremoveclient " + id);
    }

    public void run() {
        try {
            if(serverSocket == null)
                serverSocket = new DatagramSocket(cServerLogic.listenPort);
            while (sSettings.IS_SERVER) {
                try {
                    byte[] receiveData = new byte[sSettings.rcvbytesserver];
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    serverSocket.receive(receivePacket);
                    receivedPackets.add(receivePacket);
                    ticks++;
                    long theTime = System.nanoTime();
                    if(nextSecondNanos < theTime) {
                        nextSecondNanos = theTime + 1000000000;
                        uiInterface.netReportServer = ticks;
                        ticks = 0;
                    }
                }
                catch (Exception e) {
                    eLogging.logException(e);
                    e.printStackTrace();
                }
            }
            System.out.println("ending server thread");
        }
        catch (Exception ee) {
            eLogging.logException(ee);
            ee.printStackTrace();
        }
    }

    public void sendMapAndRespawn(String id) {
        sendMap(id);
        if(!sSettings.show_mapmaker_ui) //spawn in after finished loading
            xCon.ex("respawnnetplayer " + id);
    }

    public void handleJoin(String id) {
        masterStateMap.put(id, new nStateBallGame());
        clientNetCmdMap.put(id, new LinkedList<>());
        clientStateSnapshots.put(id, masterStateMap.toString());
        gScoreboard.addId(id);
        sendMapAndRespawn(id);
        handleBackfill(id);
        String cname =  masterStateMap.get(id).get("name");
        String ccolor =  masterStateMap.get(id).get("color");
        xCon.ex(String.format("echo %s#%s joined the game", cname, ccolor));
    }

    public void handleBackfill(String id) {
        for(String cId : masterStateMap.keys()) {
            if(!id.equals(cId)) {
                gPlayer p = cServerLogic.getPlayerById(cId);
                if(p != null)
                    addNetCmd(id, String.format("cl_spawnplayer %s %s %s", cId, p.get("coordx"), p.get("coordy")));
            }
        }
    }

    public void readData(String receiveDataString) {
        if(receiveDataString.length() < 1)
            return;
        //load received string into state object
        nState receivedState = new nState(receiveDataString.trim());
        String stateId = receivedState.get("id");
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
        // MANUALLY streams map to joiner, needs all raw vars, can NOT use console comms like 'loadingscreen' to sync
        //these three are always here
        ArrayList<String> maplines = new ArrayList<>();
        maplines.add(String.format("cl_setvar cv_velocityplayer %s;cl_setvar cv_maploaded 0;cl_setvar cv_gamemode %d\n",
                xCon.ex("cl_setvar cv_velocityplayer"), cServerLogic.gameMode));
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
        maplines.add("cl_setvar cv_maploaded 1");
        //iterate through the maplines and send in batches
//        StringBuilder sendStringBuilder = new StringBuilder();
//        int linectr = 0;
        for(int i = 0; i < maplines.size(); i++) {
            String line = maplines.get(i);
            // slow way, but consistent with new exec loading and server sync
            addNetCmd(packId, line);
            // batch send below, old but better
//            String next = "";
//            if(maplines.size() > i+1)
//                next = maplines.get(i+1);
//            sendStringBuilder.append(";").append(line);
//            linectr++;
//            if(sendStringBuilder.length() + next.length() >= sendbatchsize || linectr == maplines.size()) {
//                String sendString = sendStringBuilder.toString();
//                addNetCmd(packId, sendString.substring(1));
//                sendStringBuilder = new StringBuilder();
//            }
        }
    }

    void handleClientCommand(String id, String cmd) {
        String ccmd = cmd.split(" ")[0];
            if(clientCmdDoables.containsKey(ccmd))
                clientCmdDoables.get(ccmd).ex(id, cmd);
            else
                addNetCmd(id, "echo NO HANDLER FOUND FOR CMD: " + cmd);
    }

    public void checkClientMessageForTimeAndVoteSkip(String id, String testmsg) {
        xCon.ex("exec scripts/sv_handleclientmessage " + id + " " + testmsg); //check for special sound
        if(testmsg.strip().equalsIgnoreCase("thetime"))
            addNetCmd(id, "cl_echo the time is " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        else if(testmsg.strip().equalsIgnoreCase("skip")) {
            if(voteSkipList.contains(id))
                addNetCmd(id, "cl_echo [SKIP] YOU HAVE ALREADY VOTED TO SKIP");
            else {
                voteSkipList.add(id);
                int votes = voteSkipList.size();
                int limit = cServerVars.voteskiplimit;
                if(votes < limit) {
                    xCon.ex(String.format("echo [SKIP] %s/%s VOTED TO SKIP. SAY 'skip' TO END ROUND.", votes, limit));
                }
                else {
                    addExcludingNetCmd("server", String.format("playsound sounds/win/%s",
                            eManager.winSoundFileSelection[(int)(Math.random() * eManager.winSoundFileSelection.length)]));
                    xCon.ex("echo [VOTE_SKIP] VOTE TARGET REACHED");
                    xCon.ex("echo changing map...");
                    cServerLogic.timedEvents.put(Long.toString(gTime.gameTime + cServerVars.voteskipdelay), new gTimeEvent(){
                        public void doCommand() {
                            xCon.ex("changemaprandom");
                        }
                    });
                }
            }
        }
    }

    public void disconnect() {
        sSettings.IS_SERVER = false;
        serverSocket.close();
        refreshInstance();
    }
}
