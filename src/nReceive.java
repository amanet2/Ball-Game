import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class nReceive {
    public static void processReceiveDataString(String receiveDataString) {
        String[] toks = receiveDataString.trim().split("@");
        if(sSettings.net_server) {
            if(toks[0].length() > 0) {
                int isnewclient = 1;
                String argload = toks[0];
                //process new packet
                HashMap<String, String> packArgMap = nVars.getMapFromNetString(argload);
                HashMap<String, HashMap<String, Integer>> scoresMap = cScoreboard.scoresMap;
                String packId = packArgMap.get("id");
                if(!nServer.clientArgsMap.containsKey(packId))
                    nServer.clientArgsMap.put(packId, packArgMap);
                if(!scoresMap.containsKey(packId)) {
                    cScoreboard.addId(packId);
                }
                String packName = packArgMap.get("name") != null ? packArgMap.get("name") : nServer.clientArgsMap.get(packId).get("name");
                String packActions = packArgMap.get("act") != null ? packArgMap.get("act") : "";
                int packWeap = packArgMap.get("weapon") != null ? Integer.parseInt(packArgMap.get("weapon")) : 0;

                //fetch old packet
                HashMap<String, String> oldArgMap = nServer.clientArgsMap.get(packId);
                String oldName = "";
                long oldTimestamp = 0;
                if(oldArgMap != null) {
                    oldName = oldArgMap.get("name");
                    oldTimestamp = Long.parseLong(oldArgMap.get("time"));
                }
                for(String k : packArgMap.keySet()) {
                    if(!nServer.clientArgsMap.get(packId).containsKey(k)
                            || nServer.clientArgsMap.get(packId).get(k) != packArgMap.get(k)) {
                        nServer.clientArgsMap.get(packId).put(k, packArgMap.get(k));
                    }
                }
                if(nServer.clientIds.contains(packId)) {
                    isnewclient = 0;
                    scoresMap.get(packId).put("ping", (int) Math.abs(System.currentTimeMillis() - oldTimestamp));
                    if(oldName.length() > 0 && !oldName.equals(packName))
                        xCon.ex(String.format("say %s changed name to %s", oldName, packName));
                    if(System.currentTimeMillis() > oldTimestamp + sVars.getInt("timeout")) {
                        nServer.quitClientIds.add(packId);
                    }
                    gPlayer packPlayer = gScene.getPlayerById(packId);
                    if(packPlayer != null) {
                        if (nServer.clientArgsMap.get(packId).containsKey("vels")) {
                            String[] veltoks = nServer.clientArgsMap.get(packId).get("vels").split("-");
                            packPlayer.put("vel0", veltoks[0]);
                            packPlayer.put("vel1", veltoks[1]);
                            packPlayer.put("vel2", veltoks[2]);
                            packPlayer.put("vel3", veltoks[3]);
                        }
                        if (sVars.isOne("smoothing")) {
                            packPlayer.put("coordx", nServer.clientArgsMap.get(packId).get("x"));
                            packPlayer.put("coordy", nServer.clientArgsMap.get(packId).get("y"));
                        }
                    }
                    if(!packArgMap.containsKey("spawnprotected")
                            && nServer.clientArgsMap.get(packId).containsKey("spawnprotected")) {
                        nServer.clientArgsMap.get(packId).remove("spawnprotected");
                    }
                    cGameLogic.processActionLoadServer(packActions, packName, packId);
                    if(packArgMap.containsKey("quit") || packArgMap.containsKey("disconnect")) {
                        nServer.quitClientIds.add(packId);
                    }
                    if(packArgMap.get("msg") != null && packArgMap.get("msg").length() > 0) {
                        String msg = packArgMap.get("msg");
                        xCon.ex(String.format("say %s", msg));
                        String[] t = msg.split(" ");
                        if(t.length > 1)
                            cScripts.checkMsgSpecialFunction(t[1]);
                    }
                }
                if(isnewclient == 1) {
                    nServer.newClientIds.add(packId);
                    nServer.clientIds.add(packId);
                    if(!packId.contains("bot")) {
                        gPlayer player = new gPlayer(-6000, -6000,150,150,
                                eUtils.getPath("animations/player_red/a03.png"));
                        player.put("name", packName);
                        player.putInt("tag", eManager.currentMap.scene.playersMap().size());
                        player.put("id", packId);
                        player.putInt("weapon", packWeap);
                        eManager.currentMap.scene.players().add(player);
                        eManager.currentMap.scene.playersMap().put(packId, player);
                    }
                    xCon.ex(String.format("say %s joined the game", packName));
                }
            }
        }
        else if(sSettings.net_client) {
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
                    if(packArgs.containsKey("flagmasterid")) {
                        cVars.put("flagmasterid", packArgs.get("flagmasterid"));
                    }
                    if(!packArgs.get("map").contains(eManager.currentMap.mapName)) {
                        cVars.put("intermissiontime", "-1");
                        cVars.putInt("timeleft", sVars.getInt("timelimit"));
                        cGameLogic.resetGameState();
                        xCon.ex(String.format("load %s", packArgs.get("map") + sVars.get("mapextension")));
                        xCon.ex("respawn");
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
                            if(pr.isVal("id", args[1]))
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
                    cPowerups.processPowerupStringClient(packArgs.get("powerups"));
                    cVars.put("gamemode", packArgs.get("mode"));
                    cVars.put("gameteam", packArgs.get("teams"));
                    cVars.put("gamespawnarmed", packArgs.get("armed"));
//                    if(cVars.isZero("gamespawnarmed") && !cVars.isZero("currentweapon")) {
//                        cScripts.changeWeapon(0);
//                    }
                    cVars.put("scorelimit", packArgs.get("scorelimit"));
                    cVars.put("allowweaponreload", packArgs.get("allowweaponreload"));
                    cVars.put("gametick", packArgs.get("tick"));
                    cVars.put("timeleft", packArgs.get("timeleft"));
                    cVars.put("spawnprotectionmaxtime", packArgs.get("spmaxtime"));
                    if(packArgs.get("msg") != null && packArgs.get("msg").length() > 0) {
                        nClient.msgreceived = 1;
                        String msg = packArgs.get("msg");
                        gMessages.addScreenMessage(msg);
                        cScripts.checkMsgSpecialFunction(msg);
                        String[] t = msg.split(" ");
                        if(t.length > 1)
                            cScripts.checkMsgSpecialFunction(t[1]);
                    }
                }
                if(!idload.equals(uiInterface.uuid)) {
                    int isnewclient = 1;
                    if(nServer.clientIds.contains(idload)) {
                        ctr ++;
                        foundIds.add(idload);
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
                        cGameLogic.processActionLoadClient(actionload);
                    }
                    if(isnewclient == 1){
                        nServer.clientIds.add(idload);
                        ctr++;
                        gPlayer player = new gPlayer(-6000, -6000,150,150,
                                eUtils.getPath("animations/player_red/a03.png"));
                        player.put("id", idload);
                        player.putInt("tag", eManager.currentMap.scene.playersMap().size());
                        player.put("name", nameload);
                        eManager.currentMap.scene.players().add(player);
                        eManager.currentMap.scene.playersMap().put(idload, player);
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
                        scoresMapIdMap.put("wins", Integer.parseInt(stoks[j].split("-")[1]));
                        scoresMapIdMap.put("score", Integer.parseInt(stoks[j].split("-")[2]));
                        scoresMapIdMap.put("kills", Integer.parseInt(stoks[j].split("-")[3]));
                        scoresMapIdMap.put("ping", Integer.parseInt(stoks[j].split("-")[4]));
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
                    int qi = nServer.clientIds.indexOf(tr);
                    nServer.clientArgsMap.remove(tr);
                    cScoreboard.scoresMap.remove(tr);
                    nServer.clientIds.remove(tr);
                    eManager.currentMap.scene.players().remove( qi + 1);
                    eManager.currentMap.scene.playersMap().remove(tr);
                }
            }
        }
    }
}
