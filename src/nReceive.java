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
                String packId = packArgMap.get("id");
                if(!nServer.clientArgsMap.containsKey(packId))
                    nServer.clientArgsMap.put(packId, packArgMap);
                if(!nServer.scoresMap.containsKey(packId))
                    nServer.scoresMap.put(packId, new HashMap<>());
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
                for(int i = 0; i < nServer.clientIds.size(); i++) {
                    String clientId = nServer.clientIds.get(i);
                    if(clientId.equals(packId)){
                        isnewclient = 0;
                        nServer.matchPings[i+1] = (int) Math.abs(System.currentTimeMillis() - oldTimestamp);
                        if(oldName.length() > 0 && !oldName.equals(packName)) {
                            nServer.clientNames.set(i, packName);
                            xCon.ex(String.format("say %s changed name to %s", oldName, packName));
                        }
                        if(System.currentTimeMillis() > oldTimestamp + sVars.getInt("timeout")) {
                            isnewclient = 0;
                            nServer.quitClientIds.add(packId);
                        }
                        if(cGameLogic.getPlayerByIndex(i+1) != null) {
                            if (nServer.clientArgsMap.get(clientId).containsKey("vels")) {
                                String[] veltoks = nServer.clientArgsMap.get(clientId).get("vels").split("-");
                                for(int vel = 0; vel < veltoks.length; vel++) {
                                    xCon.ex("THING_PLAYER."+(i+1)+".vel"+vel+" "+veltoks[vel]);
                                }
                            }
                            if (sVars.isOne("smoothing")) {
                                cGameLogic.getPlayerByIndex(i + 1).put("coordx",
                                        nServer.clientArgsMap.get(clientId).get("x"));
                                cGameLogic.getPlayerByIndex(i + 1).put("coordy",
                                        nServer.clientArgsMap.get(clientId).get("y"));
                            }
                        }
                        if(!packArgMap.containsKey("spawnprotected")
                                && nServer.clientArgsMap.get(packId).containsKey("spawnprotected")) {
                            nServer.clientArgsMap.get(packId).remove("spawnprotected");
                        }
                        cGameLogic.processActionLoadServer(packActions, i, packName, packId);
                        if(packArgMap.containsKey("quit") || packArgMap.containsKey("disconnect")) {
                            isnewclient = 0;
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
                }
                if(isnewclient == 1) {
                    nServer.newClientIds.add(packId);
                    nServer.clientsConnected++;
                    nServer.matchWins = Arrays.copyOf(nServer.matchWins, nServer.clientsConnected+1);
                    nServer.scores = Arrays.copyOf(nServer.scores, nServer.clientsConnected+1);
                    nServer.matchKills = Arrays.copyOf(nServer.matchKills, nServer.clientsConnected+1);
                    nServer.matchPings = Arrays.copyOf(nServer.matchPings, nServer.clientsConnected+1);
                    nServer.clientIds.add(packId);
                    nServer.clientNames.add(packName);
                    if(!packId.contains("bot")) {
                        gPlayer player = new gPlayer(-6000, -6000,150,150,
                                eUtils.getPath("animations/player_red/a03.png"));
                        player.put("name", packName);
                        player.putInt("tag", eManager.currentMap.scene.players().size());
                        player.put("id", packId);
                        player.putInt("weapon", packWeap);
                        eManager.currentMap.scene.players().add(player);
                    }
                    xCon.ex(String.format("say %s joined the game", packName));
                }
            }
        }
        else if(sSettings.net_client) {
            int w = 1;
            int ctr = 0;
            ArrayList<String> foundIds = new ArrayList<>();
            for(int i = 0; i < toks.length; i++) {
                String argload = toks[i];
                HashMap<String, String> packArgs = nVars.getMapFromNetString(argload);
                String idload = packArgs.get("id");
                String nameload = packArgs.get("name") != null ? packArgs.get("name")
                        : nServer.clientArgsMap.containsKey(idload) ? nServer.clientArgsMap.get(idload).get("name")
                        : "player";
                String actionload = packArgs.get("act") != null ? packArgs.get("act") : "";
                if(!nServer.clientArgsMap.containsKey(idload))
                    nServer.clientArgsMap.put(idload, packArgs);
                if(!nServer.scoresMap.containsKey(idload))
                    nServer.scoresMap.put(idload, new HashMap<>());
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
                        for(int d = 0; d < 4; d++) {
                            if(!(d == 1 && cVars.getInt("maptype") == gMap.MAP_SIDEVIEW)) {
                                xCon.ex("THING_PLAYER.0.vel"+d+" 0");
                                xCon.ex("THING_PLAYER.0.mov"+d+" 0");
                            }
                        }
                    }
                    else if(cVars.get("winnerid").length() > 0){
                        cVars.put("winnerid", "");
                    }
                    if(packArgs.containsKey("ballx") && packArgs.containsKey("bally")) {
                        for(gProp p : eManager.currentMap.scene.props()) {
                            if(p.getInt("code") == gProp.BALLBOUNCY) {
                                p.put("coordx", packArgs.get("ballx"));
                                p.put("coordy", packArgs.get("bally"));
                            }
                        }
                    }
                    if(packArgs.containsKey("flagmasterid")) {
                        cVars.put("flagmasterid", packArgs.get("flagmasterid"));
                    }
                    if(packArgs.containsKey("virussingleid")) {
                        cVars.put("virussingleid", packArgs.get("virussingleid"));
                    }
                    if(packArgs.containsKey("chosenoneid")) {
                            cVars.put("chosenoneid", packArgs.get("chosenoneid"));
                    }
                    if(!packArgs.get("map").contains(eManager.currentMap.mapName)) {
                        cVars.put("intermissiontime", "-1");
                        cVars.putInt("timeleft", sVars.getInt("timelimit"));
                        cGameLogic.resetGameState();
                        xCon.ex(String.format("load %s", packArgs.get("map") + sVars.get("mapextension")));
                        xCon.ex("respawn");
                    }
                    if(packArgs.get("state").contains("safezone")) {
                        for(int k = 0; k < eManager.currentMap.scene.props().size(); k++) {
                            gProp p = eManager.currentMap.scene.props().get(k);
                            if(k == Integer.parseInt(packArgs.get("state").split("-")[1]))
                                p.put("int0", "1");
                            else if(p.isInt("code", gProp.SAFEPOINT))
                                p.put("int0", "0");
                        }
                        cVars.put("safezonetime", packArgs.get("state").split("-")[2]);
                    }
                    if(packArgs.get("state").contains("waypoints")) {
                        for(int k = 0; k < eManager.currentMap.scene.props().size(); k++) {
                            gProp p = eManager.currentMap.scene.props().get(k);
                            if(k == Integer.parseInt(packArgs.get("state").split("-")[1])) {
                                if(p.getInt("int0") != 1)
                                    p.put("int0", "1");
                            }
                            else if(p.isInt("code", gProp.SCOREPOINT))
                                p.put("int0", "0");
                        }
                    }
                    if(packArgs.get("state").contains("bouncyball")) {
                        for(int k = 0; k < eManager.currentMap.scene.props().size(); k++) {
                            gProp p = eManager.currentMap.scene.props().get(k);
                            if(k == Integer.parseInt(packArgs.get("state").split("-")[1])) {
                                if(p.getInt("int0") != 1)
                                    p.put("int0", "1");
                            }
                            else if(p.isInt("code", gProp.SCOREPOINT))
                                p.put("int0", "0");
                        }
                    }
                    if(packArgs.get("state").contains("kingofflags")) {
                        int fctr = 0;
                        for(gProp p : eManager.currentMap.scene.props()) {
                            if(p.isInt("code", gProp.FLAGRED))
                                fctr++;
                        }
                        String ftags = packArgs.get("state").replace("kingofflags",
                                "").substring(0, fctr);
                        for(int f = 0; f < ftags.length(); f++) {
                            cVars.putInArray("kofflagcaps", Character.toString(ftags.charAt(f)), f);
                        }
                    }
                    cScripts.processPowerupStringClient(packArgs.get("powerups"));
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
                    for(int j = 0; j < nServer.clientIds.size(); j++) {
                        String clientId = nServer.clientIds.get(j);
                        if(clientId.equals(idload)) {
                            ctr ++;
                            foundIds.add(clientId);
                            String clientname = nServer.clientNames.get(j);
                            if(!clientname.equals(nameload)) {
                                nServer.clientNames.set(j, nameload);
                                xCon.ex("THING_PLAYER."+(j+1)+".name " + nameload);
                            }
                            if(sVars.isOne("smoothing")) {
                                cGameLogic.getPlayerByIndex(w).put("coordx", nServer.clientArgsMap.get(clientId).get("x"));
                                cGameLogic.getPlayerByIndex(w).put("coordy", nServer.clientArgsMap.get(clientId).get("y"));
                            }
                            String[] veltoks = nServer.clientArgsMap.get(clientId).get("vels").split("-");
                            for(int vel = 0; vel < veltoks.length; vel++) {
                                xCon.ex("THING_PLAYER."+w+".vel"+vel+" "+veltoks[vel]);
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
                            w++;
                        }
                    }
                    if(isnewclient == 1){
                        nServer.clientIds.add(idload);
                        nServer.clientNames.add(nameload);
                        nServer.matchWins = Arrays.copyOf(nServer.matchWins, nServer.clientIds.size()+1);
                        nServer.scores = Arrays.copyOf(nServer.scores, nServer.clientIds.size()+1);
                        nServer.matchKills = Arrays.copyOf(nServer.matchKills, nServer.clientIds.size()+1);
                        nServer.matchPings = Arrays.copyOf(nServer.matchPings, nServer.clientIds.size()+1);
                        ctr++;
                        gPlayer player = new gPlayer(-6000, -6000,150,150,
                                eUtils.getPath("animations/player_red/a03.png"));
                        player.put("id", idload);
                        player.putInt("tag", eManager.currentMap.scene.players().size());
                        player.put("name", nameload);
                        eManager.currentMap.scene.players().add(player);
                        w++;
                    }
                }
                else {
                    nClient.clientIndex = w-1;
                }
                if(idload.equals("server")) {
                    String[] stoks = packArgs.get("scores").split(":");
                    if(nServer.scores.length < stoks.length) {
                        nServer.matchWins = Arrays.copyOf(nServer.matchWins, stoks.length);
                        nServer.scores = Arrays.copyOf(nServer.scores, stoks.length);
                        nServer.matchKills = Arrays.copyOf(nServer.matchKills, stoks.length);
                        nServer.matchPings = Arrays.copyOf(nServer.matchPings, stoks.length);
                    }
                    for (int j = 0; j < stoks.length; j++) {
                        nServer.matchWins[j] = Integer.parseInt(stoks[j].split("-")[0]);
                        nServer.scores[j] = Integer.parseInt(stoks[j].split("-")[1]);
                        nServer.matchKills[j] = Integer.parseInt(stoks[j].split("-")[2]);
                        nServer.matchPings[j] = Integer.parseInt(stoks[j].split("-")[3]);
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
                    nServer.clientIds.remove(tr);
                    eManager.currentMap.scene.players().remove( qi + 1);
                    nServer.clientNames.remove(qi);
                    nServer.matchWins = Arrays.copyOf(nServer.matchWins, nServer.clientIds.size()+1);
                    nServer.scores = Arrays.copyOf(nServer.scores, nServer.clientIds.size()+1);
                    nServer.matchKills = Arrays.copyOf(nServer.matchKills, nServer.clientIds.size()+1);
                    nServer.matchPings = Arrays.copyOf(nServer.matchPings, nServer.clientIds.size()+1);
                }
            }
        }
    }
}
