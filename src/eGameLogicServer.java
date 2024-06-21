import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

public class eGameLogicServer extends eGameLogicAdapter {
    public String masterStateSnapshot; //what we want publicly accessible
    private final DatagramSocket serverSocket;
    private final Queue<String> quitClientIds;
    private final HashMap<String, Queue<String>> clientNetCmdMap;
    private final nStateMap masterStateMap; //will be the source of truth for game state, messages, and console comms
    private final HashMap<String, String> clientCheckinMap; //track the timestamp of last received packet of a client
    private final HashMap<String, gDoable> clientCmdDoables; //doables for handling client cmds
    private final ArrayList<String> voteSkipList;

    public eGameLogicServer() {
        super();
        masterStateMap = new nStateMap();
        clientCheckinMap = new HashMap<>();
        clientCmdDoables = new HashMap<>();
        quitClientIds = new LinkedList<>();
        clientNetCmdMap = new HashMap<>();
        masterStateSnapshot = "{}";
        voteSkipList = new ArrayList<>();
        //init doables
        clientCmdDoables.put("fireweapon",
            new gDoable() {
                void doCommandAsServerFromClient(String id, String cmd) {
                    xMain.shellLogic.console.ex(cmd);
                    addIgnoringNetCmd(id+",server,",
                            cmd.replaceFirst("fireweapon", "cl_fireweapon"));
                }
            });
        clientCmdDoables.put("setthing", // don't want EVERY setthing on server to be synced, only ones requested here
            new gDoable() {
                void doCommandAsServerFromClient(String id, String cmd) {
                    xMain.shellLogic.console.ex(cmd);
                    addIgnoringNetCmd("server", "cl_" + cmd);
                }
            });
        for(String rcs : new String[]{
                "respawnnetplayer", "setnstate", "putitem", "deleteblock", "deleteitem",
                "gamemode", "deleteprefab"
        }) {
            clientCmdDoables.put(rcs,
                    new gDoable() {
                        void doCommandAsServerFromClient(String id, String cmd) {
                            //maybe add this as a net command for the server-only, to avoid concurrency issues
                            xMain.shellLogic.console.ex(cmd);
                        }
                    });
        }
        clientCmdDoables.put("deleteplayer",
            new gDoable() {
                void doCommandAsServerFromClient(String id, String cmd) {
                    String[] toks = cmd.split(" ");
                    if(toks.length > 1 && toks[1].equals(id)) //client can only remove itself
                        xMain.shellLogic.console.ex(cmd);
                }
            });
        clientCmdDoables.put("exec",
            new gDoable() {
                void doCommandAsServerFromClient(String id, String cmd) {
                    String[] toks = cmd.split(" ");
                    if(toks.length > 1 && toks[1].startsWith("prefabs/")) //client can only add prefabs
                        xMain.shellLogic.console.ex(cmd);
                }
            });
        clientCmdDoables.put("echo",
            new gDoable() {
                void doCommandAsServerFromClient(String id, String cmd) {
                    String[] toks = cmd.split(" ");
                    if(toks.length < 3) //only want to allow messages from clients, not any other echo usage
                        return;
                    addIgnoringNetCmd("server", "cl_" + cmd);
                    StringBuilder clientMessageBuilder = new StringBuilder();
                    for(int i = 2; i < toks.length; i++) {
                        clientMessageBuilder.append(" ").append(toks[i]);
                    }
                    //check msg for special string
                    String testmsg = clientMessageBuilder.substring(1);
                    if(testmsg.strip().equalsIgnoreCase("thetime"))
                        addNetCmd(id, "cl_echo the time is " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                    else if(testmsg.strip().equalsIgnoreCase("skip")) {
                        if(voteSkipList.contains(id))
                            addNetCmd(id, "cl_echo [SKIP] YOU HAVE ALREADY VOTED TO SKIP");
                        else {
                            voteSkipList.add(id);
                            xMain.shellLogic.console.ex(String.format("echo [SKIP] %s/%s VOTED TO SKIP. SAY 'skip' TO END ROUND.",
                                    voteSkipList.size(), sSettings.serverVoteSkipLimit));
                            checkForVoteSkip();
                        }
                    }
                }
            });
        // init the socket
        try {
            serverSocket = new DatagramSocket(sSettings.serverListenPort);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    public void checkForVoteSkip() {
        if(clientNetCmdMap.size() < 2 || voteSkipList.size() >= sSettings.serverVoteSkipLimit) {
            voteSkipList.clear();
            xMain.shellLogic.console.ex("echo [SKIP] VOTE TARGET REACHED");
            xMain.shellLogic.console.ex("exec scripts/sv_endgame");
        }
    }

    private void checkForUnhandledQuitters() {
        //other players
        for(String id : clientCheckinMap.keySet()) {
            long pt = Long.parseLong(clientCheckinMap.get(id));
            if(sSettings.gameTime > pt + 10000) //consider client a dc after 10 seconds
                quitClientIds.add(id);
        }
        while(quitClientIds.size() > 0) {
            removeNetClient(quitClientIds.remove());
        }
    }

    public void addIgnoringNetCmd(String ignoreIds, String cmd) {
        for(String id : masterStateMap.keys()) {
            if(!ignoreIds.contains(id))
                addNetCmd(id, cmd);
        }
    }

    public void addNetCmd(String id, String cmd) {
        xMain.shellLogic.console.debug("SERVER_ADDCOM_" + id + ": " + cmd);
        if(id.equalsIgnoreCase("server"))
            xMain.shellLogic.serverSimulationThread.addLocalCmd(cmd);
        else
            addNetSendData(id, cmd);
    }

    public void addNetCmd(String cmd) {
        xMain.shellLogic.console.debug("SERVER_ADDCOM_ALL: " + cmd);
        xMain.shellLogic.console.ex(cmd);
        addNetSendData(cmd);
    }

    private void addNetSendData(String id, String data) {
        if(clientNetCmdMap.containsKey(id))
            clientNetCmdMap.get(id).add(data);
    }

    private void addNetSendData(String data) {
        for(String id: clientNetCmdMap.keySet()) {
            addNetSendData( id, data);
        }
    }

    public void clientReceivedCmd(String id) {
        if(clientNetCmdMap.get(id).size() > 0) {
            try { //needed here
                clientNetCmdMap.get(id).remove();
            }
            catch(Exception cre) {
                cre.printStackTrace();
                clientNetCmdMap.get(id).clear();
            }
        }
    }

    private String createSendDataString(String clientid) {
        //create response
        HashMap<String, String> netVars = new HashMap<>();
        netVars.put("cmd", "");
        netVars.put("time", Long.toString(sSettings.serverTimeLeft));
        if(clientNetCmdMap.containsKey(clientid) && clientNetCmdMap.get(clientid).size() > 0)
            netVars.put("cmd", clientNetCmdMap.get(clientid).peek());
        nStateMap deltaStateMap = new nStateMap(masterStateSnapshot);
        //add server vars to the sending map
        deltaStateMap.put("server", new nState());
        for(String k : netVars.keySet()) {
            deltaStateMap.get("server").put(k, netVars.get(k));
        }
        return deltaStateMap.toString().replace(", ", ",");
    }

    private void removeNetClient(String id) {
        xMain.shellLogic.console.ex("exec scripts/sv_handleremoveclient " + id);
        clientCheckinMap.remove(id);
        masterStateMap.remove(id);
        clientNetCmdMap.remove(id);
        gScoreboard.scoresMap.remove(id);
        xMain.shellLogic.console.ex("deleteplayer " + id);
    }

    private void sendMapAndRespawn(String id) {
        sendMap(id);
        if(!sSettings.show_mapmaker_ui) //spawn in after finished loading
            xMain.shellLogic.console.ex("respawnnetplayer " + id);
    }

    public void handleJoin(String id) {
        if(!id.startsWith("bot"))
            masterStateMap.put(id, new nStateBallGame());
        else {
            nStateBallGame botState = new nStateBallGame();
            String botName = id;
            int idInt = Integer.parseInt(id.replace("bot",""));
            if(idInt > 90000000)
                botName = "BotLite";
            else if(idInt > 80000000)
                botName = "BotRick";
            else if(idInt > 70000000)
                botName = "BotDuke";
            else if(idInt > 60000000)
                botName = "BotChief";
            else if(idInt > 50000000)
                botName = "BotLite";
            else if(idInt > 40000000)
                botName = "BotMustard";
            else if(idInt > 30000000)
                botName = "BotLite";
            else if(idInt > 20000000)
                botName = "BotMustard";
            else if(idInt > 10000000)
                botName = "BotGeorge";
            botState.put("name", botName);
            String botColor = sSettings.colorSelection[(int)(Math.random()*(sSettings.colorSelection.length-1))];
            botState.put("color", botColor);
            masterStateMap.put(id, botState);
            xMain.shellLogic.console.ex(String.format("echo %s#%s joined the game", botName, botColor));
        }

        if(!id.startsWith("bot"))
            clientNetCmdMap.put(id, new LinkedList<>());
        gScoreboard.addId(id);
        if(!id.startsWith("bot")) {
            sendMapAndRespawn(id);
            handleBackfill(id);
        }
    }

    private void handleBackfill(String id) {
        for(String cId : masterStateMap.keys()) {
            if(!id.equals(cId)) {
                gPlayer p = xMain.shellLogic.serverScene.getPlayerById(cId);
                if(p != null)
                    addNetCmd(id, String.format("cl_spawnplayer %s %d %d", cId, p.coords[0], p.coords[1]));
            }
        }
    }

    private void readData(String receiveDataString) {
        if(receiveDataString.length() < 1)
            return;
        //load received string into state object
        nState receivedState = new nState(receiveDataString);
        String stateId = receivedState.get("id");
        //check if masterState contains
        if(!masterStateMap.contains(stateId)) {
            handleJoin(stateId);
            String cname =  receivedState.get("name");
            String ccolor = receivedState.get("color");
            xMain.shellLogic.console.ex(String.format("echo %s#%s joined the game", cname, ccolor));

        }
        //record checkin time for client
        clientCheckinMap.put(stateId, Long.toString(sSettings.gameTime));
        //load the keys from received data into our state map
        for(String k : receivedState.keys()) {
            masterStateMap.get(stateId).put(k, receivedState.get(k));
        }
        //update players
        gPlayer pl = xMain.shellLogic.serverScene.getPlayerById(stateId);
        if(pl != null) {    //store player object's health in outgoing network arg map
            masterStateMap.get(stateId).put("coords", pl.coords[0] + ":" + pl.coords[1]);
            masterStateMap.get(stateId).put("vel0", Integer.toString(pl.vel0));
            masterStateMap.get(stateId).put("vel1", Integer.toString(pl.vel1));
            masterStateMap.get(stateId).put("vel2", Integer.toString(pl.vel2));
            masterStateMap.get(stateId).put("vel3", Integer.toString(pl.vel3));
        }
        //update bots
        for(String id : xMain.shellLogic.serverScene.getThingMapIds("THING_PLAYER")) {
            if(id.startsWith("bot")) {
                gPlayer bpl = xMain.shellLogic.serverScene.getPlayerById(id);
                if(bpl != null) {    //store player object's health in outgoing network arg map
                    masterStateMap.get(id).put("coords", bpl.coords[0] + ":" + bpl.coords[1]);
                    masterStateMap.get(id).put("vel0", Integer.toString(bpl.vel0));
                    masterStateMap.get(id).put("vel1", Integer.toString(bpl.vel1));
                    masterStateMap.get(id).put("vel2", Integer.toString(bpl.vel2));
                    masterStateMap.get(id).put("vel3", Integer.toString(bpl.vel3));
                    masterStateMap.get(id).put("fv", Double.toString(bpl.fv));
                }
                masterStateMap.get(id).put("score",  String.format("%d:%d",
                        gScoreboard.scoresMap.get(id).get("wins"), gScoreboard.scoresMap.get(id).get("score")));
            }
        }
        //update scores
        masterStateMap.get(stateId).put("score",  String.format("%d:%d",
                gScoreboard.scoresMap.get(stateId).get("wins"), gScoreboard.scoresMap.get(stateId).get("score")));
        masterStateSnapshot = masterStateMap.toString().replace(", ", ",");
    }

    public String setClientState(String id, String key, String val) {
        if(!masterStateMap.contains(id))
            return "null";
        masterStateMap.get(id).put(key, val);
        return masterStateMap.get(id).get(key);
    }

    public String getClientStateVal(String id, String key) {
        if(!masterStateMap.contains(id))
            return "null";
        return masterStateMap.get(id).get(key);
    }

    private void sendMap(String packId) {
        // MANUALLY streams map to joiner, needs all raw vars, can NOT use console comms like 'loadingscreen' to sync
        //these three are always here
        ArrayList<String> maplines = new ArrayList<>();
        maplines.add(String.format("cl_setvar velocityplayerbase %s;cl_setvar maploaded 0;cl_setvar gamemode %d\n",
                sSettings.serverVelocityPlayerBase, sSettings.serverGameMode));
        ConcurrentHashMap<String, gThing> floorMap = xMain.shellLogic.serverScene.getThingMap("BLOCK_FLOOR");
        ConcurrentHashMap<String, gThing> cubeMap = xMain.shellLogic.serverScene.getThingMap("BLOCK_CUBE");
        ConcurrentHashMap<String, gThing> collisionMap = xMain.shellLogic.serverScene.getThingMap("BLOCK_COLLISION");
        for(String id : floorMap.keySet()) {
            gBlockFloor floor = (gBlockFloor) floorMap.get(id);
            String[] args = new String[] {
                    floor.id, floor.prefabId,
                    Integer.toString(floor.coords[0]), Integer.toString(floor.coords[1])
            };
            StringBuilder floorString = new StringBuilder("cl_putfloor");
            for(String arg : args) {
                if(arg != null)
                    floorString.append(" ").append(arg);
            }
            maplines.add(floorString.toString());
        }
        for(String id : cubeMap.keySet()) {
            gBlockCube cube = (gBlockCube) cubeMap.get(id);
            String[] args = new String[] {
                    cube.id, cube.prefabId,
                    Integer.toString(cube.coords[0]), Integer.toString(cube.coords[1]),
                    Integer.toString(cube.dims[0]), Integer.toString(cube.dims[1]),
                    Integer.toString(cube.toph), Integer.toString(cube.wallh),
            };
            StringBuilder cubeString = new StringBuilder("cl_putcube");
            for(String arg : args) {
                if(arg != null)
                    cubeString.append(" ").append(arg);
            }
            maplines.add(cubeString.toString());
        }
        for(String id : collisionMap.keySet()) {
            gBlockCollision collision = (gBlockCollision) collisionMap.get(id);
            String[] args = new String[] {
                    collision.id, collision.prefabId,
                    Integer.toString(collision.coords[0]), Integer.toString(collision.coords[1]),
                    Integer.toString(collision.dims[0]), Integer.toString(collision.dims[1]),
            };
            StringBuilder collisionString = new StringBuilder("cl_putcollision");
            for(String arg : args) {
                if(arg != null)
                    collisionString.append(" ").append(arg);
            }
            maplines.add(collisionString.toString());
        }
        ConcurrentHashMap<String, gThing> itemMap = xMain.shellLogic.serverScene.getThingMap("THING_ITEM");
        for(String id : itemMap.keySet()) {
            gItem item = (gItem) itemMap.get(id);
            String[] args = new String[]{
                    item.type,
                    item.id,
                    Integer.toString(item.coords[0]),
                    Integer.toString(item.coords[1])
            };
            StringBuilder str = new StringBuilder("cl_putitem");
            for(String arg : args) {
                if(arg != null)
                    str.append(" ").append(arg);
            }
            maplines.add(str.toString());
        }
        maplines.add("cl_setvar maploaded 1");
        for(int i = 0; i < maplines.size(); i++) {
            String line = maplines.get(i);
            // slow way, but consistent with new exec loading and server sync
            // try batching in the future
            addNetCmd(packId, line);
        }
    }

    public void handleClientCommand(String id, String cmd) {
        String ccmd = cmd.split(" ")[0];
        if(clientCmdDoables.containsKey(ccmd))
            clientCmdDoables.get(ccmd).doCommandAsServerFromClient(id, cmd);
        else
            addNetCmd(id, "cl_echo NO HANDLER FOUND FOR CMD: " + cmd);
    }

    @Override
    public void update() {
        if(!sSettings.IS_SERVER) //avoids more exceptions when socket closed
            return;
        super.update();
        try {
            checkForUnhandledQuitters();
            byte[] receiveData = new byte[sSettings.rcvbytesserver];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            if(serverSocket.isClosed() || !sSettings.IS_SERVER) //avoids more exceptions when socket closed
                return;
            serverSocket.receive(receivePacket);
            try {
                String receiveDataString = new String(receivePacket.getData());
                xMain.shellLogic.console.debug("SERVER RCV [" + receiveDataString.trim().length() + "]: "
                        + receiveDataString.trim());
                //get the ip address of the client
                InetAddress addr = receivePacket.getAddress();
                int port = receivePacket.getPort();
                //read data of packet
                readData(receiveDataString); //and respond too
                //get player id of client
                nState clientState = new nState(receiveDataString);
                String clientId = clientState.get("id");
                String sendDataString = createSendDataString(clientId);
                byte[] sendData = sendDataString.getBytes();
                serverSocket.send(new DatagramPacket(sendData, sendData.length, addr, port));
                xMain.shellLogic.console.debug("SERVER_STATE_" + clientId + " [" + masterStateSnapshot + "]");
                xMain.shellLogic.console.debug("SERVER_SEND_" + clientId + " [" + sendDataString.length() + "]: " + sendDataString);
                if(sendDataString.length() > sSettings.max_packet_size)
                    System.out.println("*WARNING* PACKET LENGTH EXCEED " + sSettings.max_packet_size + " BYTES: "
                            + "SERVER_SEND_" + clientId + " [" + sendDataString.length() + "]: " + sendDataString);
            }
            catch (Exception e) {
                xMain.shellLogic.console.logException(e);
                e.printStackTrace();
            }
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
        sSettings.tickReportServer = getTickReport();
    }

    @Override
    public void disconnect() {
        super.disconnect();
        sSettings.IS_SERVER = false;
    }

    @Override
    public void cleanup() {
        super.cleanup();
        sSettings.IS_SERVER = false;
        serverSocket.close();
        xMain.shellLogic.serverNetThread = null;
        sSettings.tickReportServer = 0;
    }
}
