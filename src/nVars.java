import java.util.HashMap;
import java.util.Set;

public class nVars {
    //start with only player1
    private static HashMap<String, String> keys = null;

    public static String get(String s) {
        refresh();
        return keys.get(s);
    }

    public static Set<String> keySet() {
        return keys.keySet();
    }

    public static HashMap<String, String> copy() {
        return new HashMap<>(nVars.keys);
    }

    public static String dump() {
        return keys.toString();
    }

    public static void update() {
        refresh();
        String nms = gMessages.networkMessage.length() > 0 ? gMessages.networkMessage:"";
        if(sSettings.net_client && nms.length() > 0) {
            gMessages.networkMessage = "";
        }
        keys.put("act", cGameLogic.getActionLoad());
        if(sSettings.net_server && nSend.focus_id.length() > 0 && !nSend.focus_id.equals(uiInterface.uuid)
                && cVars.get("sendsound").length() > 0
                && nServer.clientArgsMap.containsKey(nSend.focus_id)
                && !nServer.clientArgsMap.get(nSend.focus_id).containsKey("netsfxrcv")) {
            xCon.ex("playsound " + cVars.get("sendsound"));
            if(nSend.focus_id.contains("bot")) {
                nServer.clientArgsMap.get(nSend.focus_id).put("netsfxrcv", "1");
            }
            keys.put("act", "playsound"+cVars.get("sendsound")+"-"+keys.get("act"));
        }
        keys.put("id", sSettings.net_server ? "server" : uiInterface.uuid);
        keys.put("x", xCon.ex("THING_PLAYER.0.coordx"));
        keys.put("y", xCon.ex("THING_PLAYER.0.coordy"));
        keys.put("crouch", xCon.ex("THING_PLAYER.0.crouch"));
        keys.put("fire", cVars.isOne("firing") && (cVars.getInt("weaponstock"+cVars.get("currentweapon")) > 0
                || xCon.ex("THING_PLAYER.0.sendshot").equals("1")
                || cVars.getInt("currentweapon") == gWeapons.weapon_boxingglove
                || cVars.getInt("currentweapon") == gWeapons.weapon_none) ? "1" : "0");
        keys.put("fv", xCon.ex("THING_PLAYER.0.fv"));
        keys.put("dirs",String.format("%s%s%s%s", xCon.ex("THING_PLAYER.0.mov0"),
                xCon.ex("THING_PLAYER.0.mov1"), xCon.ex("THING_PLAYER.0.mov2"),
                xCon.ex("THING_PLAYER.0.mov3")));
        keys.put("color", sVars.get("playercolor"));
        keys.put("hat", sVars.get("playerhat"));
        keys.put("msg", nms);
        if(sSettings.net_server && nSend.focus_id.length() > 0 && nServer.clientIds.contains(nSend.focus_id)
            && !nSend.focus_id.equals(uiInterface.uuid)) {
            keys.put("msg", !nServer.clientArgsMap.get(nSend.focus_id).containsKey("netmsgrcv")
                    && gMessages.networkMessage.length() > 0 ? gMessages.networkMessage : "");
        }
        keys.put("name", sVars.get("playername"));
        keys.put("vels", String.format("%s-%s-%s-%s", xCon.ex("THING_PLAYER.0.vel0"),
            xCon.ex("THING_PLAYER.0.vel1"), xCon.ex("THING_PLAYER.0.vel2"),
            xCon.ex("THING_PLAYER.0.vel3")));
        keys.put("weapon", cVars.get("currentweapon"));
        keys.put("sicknessslow", xCon.ex("THING_PLAYER.0.sicknessslow"));
        keys.put("sicknessfast", xCon.ex("THING_PLAYER.0.sicknessfast"));
        if(cVars.isOne("quitting"))
            keys.put("quit", "");
        else
            keys.remove("quit");
        if(cVars.isOne("disconnecting"))
            keys.put("disconnect", "");
        else
            keys.remove("disconnect");
        keys.put("time", Long.toString(System.currentTimeMillis()));
        if(sSettings.net_server) {
            for(String s : new String[]{"virussingleid", "chosenoneid","ballx","bally"}) {
                if(cVars.get(s).length() > 0)
                    keys.put(s, cVars.get(s));
                else
                    keys.remove(s);
            }
            if(cVars.isInt("gamemode", cGameMode.CAPTURE_THE_FLAG)
                    || cVars.isInt("gamemode", cGameMode.FLAG_MASTER)) {
                keys.put("flagmasterid", cVars.get("flagmasterid"));
            }
            if(keys.containsKey("armed") && !keys.get("armed").equals(cVars.get("gamespawnarmed"))) {
                xCon.ex("say SPAWN ARMED: " + (cVars.isOne("gamespawnarmed") ? "ON" : "OFF"));
            }
            keys.put("armed", cVars.get("gamespawnarmed"));
            keys.put("kick", nServer.kickClientIds.size() > 0 ? nServer.kickClientIds.peek() : "");
            keys.put("map", eManager.currentMap.mapName);
            if(keys.containsKey("mode") && !keys.get("mode").equals(cVars.get("gamemode")))
                xCon.ex("say GAME MODE: "
                        + cGameMode.net_gamemode_texts[cVars.getInt("gamemode")].toUpperCase());
            keys.put("mode", cVars.get("gamemode"));
            keys.put("powerups", cScripts.getPowerupsString());
            if(keys.containsKey("teams") && !keys.get("teams").equals(cVars.get("gameteam"))) {
                xCon.ex("say TEAM GAME: " + (cVars.isOne("gameteam") ? "ON" : "OFF"));
            }
            keys.put("teams", cVars.get("gameteam"));
            if(keys.containsKey("tick") && !keys.get("tick").equals(sVars.get("gametick"))) {
                xCon.ex("say GAME SPEED: " + sVars.get("gametick"));
            }
            keys.put("tick", sVars.get("gametick"));
            keys.put("scores", cScripts.getScoreString());
            if(keys.containsKey("scorelimit") && !keys.get("scorelimit").equals(sVars.get("scorelimit"))) {
                xCon.ex("say SCORE LIMIT: " + sVars.get("scorelimit"));
            }
            keys.put("allowweaponreload", cVars.get("allowweaponreload"));
            keys.put("scorelimit", sVars.get("scorelimit"));
            if(keys.containsKey("timelimit") && !keys.get("timelimit").equals(sVars.get("timelimit"))) {
                xCon.ex("say TIME LIMIT: " + eUtils.getTimeString(sVars.getLong("timelimit")));
                cVars.putLong("starttime", System.currentTimeMillis());
                cVars.put("timeleft", sVars.get("timelimit"));
            }
            keys.put("timelimit", sVars.get("timelimit"));
            keys.put("timeleft", cVars.get("timeleft"));
            keys.put("topscore", cScripts.getTopScoreString());
            keys.put("spmaxtime", cVars.get("spawnprotectionmaxtime"));
            keys.put("state", cGameLogic.getGameState());
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

    private static void refresh() {
        if(keys == null) {
            keys = new HashMap<>();
            update();
        }
    }

    public static void reset() {
        keys = null;
    }

}
