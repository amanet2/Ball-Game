import java.util.HashMap;
import java.util.Set;

public class nVars {
    //start with only player1
    private static HashMap<String, String> keys = new HashMap<>();

    private static void refresh() {
        if(keys == null) {
            keys = new HashMap<>();
            update();
        }
    }

    public static void reset() {
        keys = null;
    }

    public static String get(String s) {
        refresh();
        return keys.get(s);
    }

    public static Set<String> keySet() {
        return keys.keySet();
    }

    public static HashMap<String, String> copy() {
        return keys != null ? new HashMap<>(keys) : new HashMap<>();
    }

    public static String dump() {
        return keys.toString();
    }

    public static void update() {
        refresh();
        gPlayer userPlayer = cGameLogic.userPlayer();
        //handle outgoing actions
        keys.put("act", cGameLogic.getActionLoad());
        //handle outgoing msg
        keys.put("msg", "");
        //for client
        if(sSettings.net_client && nClient.netSendMsgs.size() > 0) {
            keys.put("msg", nClient.netSendMsgs.remove());
        }
        //handle outgoing cmd
        keys.put("cmd", "");
        if(sSettings.net_server && nSend.focus_id.length() > 0 && !nSend.focus_id.equals(uiInterface.uuid)
                && nServer.clientSendCmdQueues.containsKey(nSend.focus_id)
                && nServer.clientSendCmdQueues.get(nSend.focus_id).size() > 0
                && nServer.clientArgsMap.containsKey(nSend.focus_id)
                && !nServer.clientArgsMap.get(nSend.focus_id).containsKey("netcmdrcv")) {
            //act as if bot has instantly received outgoing cmds (bots dont have a "client" to exec things on)
            if(nSend.focus_id.contains("bot"))
                nServer.clientArgsMap.get(nSend.focus_id).put("netcmdrcv", "1");
            keys.put("cmd", nServer.clientSendCmdQueues.get(nSend.focus_id).peek());
        }
        //update id in net args
        keys.put("id", sSettings.net_server ? "server" : uiInterface.uuid);
        //userplayer vars like coords and dirs and weapon
        if(userPlayer != null) {
            keys.put("x", userPlayer.get("coordx"));
            keys.put("y", userPlayer.get("coordy"));
            keys.put("crouch", userPlayer.get("crouch"));
            keys.put("fv", userPlayer.get("fv"));
            keys.put("dirs", String.format("%s%s%s%s", userPlayer.get("mov0"), userPlayer.get("mov1"),
                    userPlayer.get("mov2"), userPlayer.get("mov3")));
            keys.put("vels", String.format("%s-%s-%s-%s", userPlayer.get("vel0"), userPlayer.get("vel1"),
                    userPlayer.get("vel2"), userPlayer.get("vel3")));
            keys.put("weapon", userPlayer.get("weapon"));
        }
        //values that come from svars and cvars like name, color, etc
        keys.put("color", sVars.get("playercolor"));
        keys.put("hat", sVars.get("playerhat"));
        keys.put("name", sVars.get("playername"));
        keys.put("flashlight", cVars.get("flashlight"));
        //key whose presence depends on value of cvar like quitting, disconnecting
        keys.remove("quit");
        keys.remove("disconnect");
        if(cVars.isOne("quitting"))
            keys.put("quit", "");
        if(cVars.isOne("disconnecting"))
            keys.put("disconnect", "");
        //server-specific values, mostly for gamemode stuff
        if(sSettings.net_server) {
            //flagmaster for ctf and flagmaster
            if(cVars.isInt("gamemode", cGameMode.CAPTURE_THE_FLAG)
                    || cVars.isInt("gamemode", cGameMode.FLAG_MASTER)) {
                keys.put("flagmasterid", cVars.get("flagmasterid"));
            }
            //the name of the current map
            keys.put("map", eManager.currentMap.mapName);
            //the current gamemode
            keys.put("mode", cVars.get("gamemode"));
            //describes the powerups that should be on map
            keys.put("powerups", cPowerups.createPowerupStringServer());
            //team game stuff
            if(keys.containsKey("teams") && !keys.get("teams").equals(cVars.get("gameteam"))) {
                xCon.ex("say TEAM GAME: " + (cVars.isOne("gameteam") ? "ON" : "OFF"));
            }
            keys.put("teams", cVars.get("gameteam"));
            //tickrate sync
            if(keys.containsKey("tick") && !keys.get("tick").equals(sVars.get("gametick"))) {
                xCon.ex("say GAME SPEED: " + sVars.get("gametick"));
            }
            keys.put("tick", sVars.get("gametick"));
            //send scores
            keys.put("scoremap", cScoreboard.createSortedScoreMapStringServer());
            cVars.put("scoremap", keys.get("scoremap"));
            //other gamemode stuff like scorelimit, gravity, etc
            if(keys.containsKey("scorelimit") && !keys.get("scorelimit").equals(sVars.get("scorelimit"))) {
                xCon.ex("say SCORE LIMIT: " + sVars.get("scorelimit"));
            }
            keys.put("scorelimit", sVars.get("scorelimit"));
            keys.put("gravity", cVars.get("gravity"));
            if(keys.containsKey("timelimit") && !keys.get("timelimit").equals(sVars.get("timelimit"))) {
                xCon.ex("say TIME LIMIT: " + eUtils.getTimeString(sVars.getLong("timelimit")));
                cVars.putLong("starttime", System.currentTimeMillis());
                cVars.put("timeleft", sVars.get("timelimit"));
            }
            keys.put("timelimit", sVars.get("timelimit"));
            keys.put("timeleft", cVars.get("timeleft"));
            keys.put("topscore", cScoreboard.getTopScoreString());
            keys.put("spmaxtime", cVars.get("spawnprotectionmaxtime"));
            keys.put("state", cServer.getGameStateServer());
            keys.put("win", cVars.get("winnerid"));
        }
    }

    public static HashMap<String,String> getMapFromNetString(String argload) {
        HashMap<String,String> toReturn = new HashMap<>();
        String argstr = argload.substring(1,argload.length()-1);
        for(String pair : argstr.split(",")) {
            String[] vals = pair.split("=");
            toReturn.put(vals[0].trim(), vals.length > 1 ? vals[1].trim() : "");
        }
        return  toReturn;
    }
}
