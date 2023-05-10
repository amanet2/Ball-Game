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

public class eGameLogicServer extends eGameLogicAdapter {
    public String masterStateSnapshot; //what we want publicly accessible
    private final DatagramSocket serverSocket;
    private final Queue<String> quitClientIds;
    private final HashMap<String, Queue<String>> clientNetCmdMap;
    private final nStateMap masterStateMap; //will be the source of truth for game state, messages, and console comms
    private final HashMap<String, String> clientCheckinMap; //track the timestamp of last received packet of a client
    private final HashMap<String, gDoableCmd> clientCmdDoables; //doables for handling client cmds
    private final ArrayList<String> voteSkipList;

    public eGameLogicServer() {
        masterStateMap = new nStateMap();
        clientCheckinMap = new HashMap<>();
        clientCmdDoables = new HashMap<>();
        quitClientIds = new LinkedList<>();
        clientNetCmdMap = new HashMap<>();
        masterStateSnapshot = "{}";
        voteSkipList = new ArrayList<>();
        //init doables
        clientCmdDoables.put("fireweapon",
            new gDoableCmd() {
                void ex(String id, String cmd) {
                    xCon.ex(cmd);
                    addIgnoringNetCmd(id+",server,",
                            cmd.replaceFirst("fireweapon", "cl_fireweapon"));
                }
            });
        clientCmdDoables.put("setthing", // don't want EVERY setthing on server to be synced, only ones requested here
            new gDoableCmd() {
                void ex(String id, String cmd) {
                    xCon.ex(cmd);
                    addIgnoringNetCmd("server", "cl_" + cmd);
                }
            });
        for(String rcs : new String[]{
                "respawnnetplayer", "setnstate", "putblock", "putitem", "deleteblock", "deleteitem",
                "gamemode", "deleteprefab"
        }) {
            clientCmdDoables.put(rcs,
                    new gDoableCmd() {
                        void ex(String id, String cmd) {
                            //maybe add this as a net command for the server-only, to avoid concurrency issues
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
        clientCmdDoables.put("echo",
            new gDoableCmd() {
                void ex(String id, String cmd) {
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
                            xCon.ex(String.format("echo [SKIP] %s/%s VOTED TO SKIP. SAY 'skip' TO END ROUND.",
                                    voteSkipList.size(), cServerLogic.voteskiplimit));
                            checkForVoteSkip();
                        }
                    }
                }
            });
        // init the socket
        try {
            serverSocket = new DatagramSocket(cServerLogic.listenPort);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    public void checkForVoteSkip() {
        if(voteSkipList.size() >= cServerLogic.voteskiplimit) {
            voteSkipList.clear();
            xCon.ex("echo [SKIP] VOTE TARGET REACHED");
            xCon.ex("exec scripts/sv_endgame");
        }
    }

    private void checkForUnhandledQuitters() {
        //other players
        for(String id : clientCheckinMap.keySet()) {
            long pt = Long.parseLong(clientCheckinMap.get(id));
            if(gTime.gameTime > pt + 10000) //consider client a dc after 10 seconds
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
        xCon.instance().debug("SERVER_ADDCOM_" + id + ": " + cmd);
        if(id.equalsIgnoreCase("server"))
            cServerLogic.localGameThread.addLocalCmd(cmd);
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

    public void clientReceivedCmd(String id) {
        if(clientNetCmdMap.get(id).size() > 0)
            clientNetCmdMap.get(id).remove();
    }

    private String createSendDataString(HashMap<String, String> netVars, String clientid) {
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
        xCon.ex("exec scripts/sv_handleremoveclient " + id);
        clientCheckinMap.remove(id);
        masterStateMap.remove(id);
        clientNetCmdMap.remove(id);
        gScoreboard.scoresMap.remove(id);
        xCon.ex("deleteplayer " + id);
    }

    private void sendMapAndRespawn(String id) {
        sendMap(id);
        if(!sSettings.show_mapmaker_ui) //spawn in after finished loading
            xCon.ex("respawnnetplayer " + id);
    }

    private void handleJoin(String id) {
        masterStateMap.put(id, new nStateBallGame());
        clientNetCmdMap.put(id, new LinkedList<>());
        gScoreboard.addId(id);
        sendMapAndRespawn(id);
        handleBackfill(id);
        String cname =  masterStateMap.get(id).get("name");
        String ccolor =  masterStateMap.get(id).get("color");
        xCon.ex(String.format("echo %s#%s joined the game", cname, ccolor));
    }

    private void handleBackfill(String id) {
        for(String cId : masterStateMap.keys()) {
            if(!id.equals(cId)) {
                gPlayer p = cServerLogic.getPlayerById(cId);
                if(p != null)
                    addNetCmd(id, String.format("cl_spawnplayer %s %s %s", cId, p.get("coordx"), p.get("coordy")));
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
        if(pl != null) {    //store player object's health in outgoing network arg map
            masterStateMap.get(stateId).put("coords", pl.get("coordx") + ":" + pl.get("coordy"));
            masterStateMap.get(stateId).put("vel0", pl.get("vel0"));
            masterStateMap.get(stateId).put("vel1", pl.get("vel1"));
            masterStateMap.get(stateId).put("vel2", pl.get("vel2"));
            masterStateMap.get(stateId).put("vel3", pl.get("vel3"));
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

    private void sendMap(String packId) {
        // MANUALLY streams map to joiner, needs all raw vars, can NOT use console comms like 'loadingscreen' to sync
        //these three are always here
        ArrayList<String> maplines = new ArrayList<>();
        maplines.add(String.format("cl_setvar velocityplayerbase %s;cl_setvar maploaded 0;cl_setvar gamemode %d\n",
                cServerLogic.velocityplayerbase, cServerLogic.gameMode));
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
            clientCmdDoables.get(ccmd).ex(id, cmd);
        else
            addNetCmd(id, "cl_echo NO HANDLER FOUND FOR CMD: " + cmd);
    }

    @Override
    public void update() {
        super.update();
        try {
            checkForUnhandledQuitters();
            byte[] receiveData = new byte[sSettings.rcvbytesserver];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            try {
                String receiveDataString = new String(receivePacket.getData());
                xCon.instance().debug("SERVER RCV [" + receiveDataString.trim().length() + "]: "
                        + receiveDataString.trim());
                //get the ip address of the client
                InetAddress addr = receivePacket.getAddress();
                int port = receivePacket.getPort();
                //read data of packet
                readData(receiveDataString); //and respond too
                //get player id of client
                nState clientState = new nState(receiveDataString);
                String clientId = clientState.get("id");
                //create response
                HashMap<String, String> netVars = new HashMap<>();
                netVars.put("cmd", "");
                netVars.put("time", Long.toString(cServerLogic.timeleft));
                String sendDataString = createSendDataString(netVars, clientId);
                byte[] sendData = sendDataString.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, addr, port);
                serverSocket.send(sendPacket);
                xCon.instance().debug("SERVER_STATE_" + clientId + " [" + masterStateSnapshot + "]");
                xCon.instance().debug("SERVER_SEND_" + clientId + " [" + sendDataString.length() + "]: " + sendDataString);
                if(sendDataString.length() > sSettings.max_packet_size)
                    System.out.println("*WARNING* PACKET LENGTH EXCEED " + sSettings.max_packet_size + " BYTES: "
                            + "SERVER_SEND_" + clientId + " [" + sendDataString.length() + "]: " + sendDataString);
            }
            catch (Exception e) {
                eLogging.logException(e);
                e.printStackTrace();
            }
        }
        catch (SocketException se) {
            //just to catch the closing
            se.printStackTrace();
            return;
        }
        catch (Exception e) {
            eLogging.logException(e);
            e.printStackTrace();
        }
        uiInterface.tickReportServer = getTickReport();
    }

    @Override
    public void disconnect() {
        super.disconnect();
        sSettings.IS_SERVER = false;
        serverSocket.close();
    }

    @Override
    public void cleanup() {
        super.cleanup();
        sSettings.IS_SERVER = false;
        serverSocket.close();
        cServerLogic.netServerThread = null;
        uiInterface.tickReportServer = 0;
    }
}
