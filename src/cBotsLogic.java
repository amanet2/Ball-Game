import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

public class cBotsLogic {
    private static HashMap<String, gDoableThing> behaviors = null;
    public static gDoableThing getBehavior(String key) {
        if(behaviors == null) {
            init();
        }
        return behaviors.get(key);
    }

    public static Set<String> behaviors() {
        if(behaviors == null) {
            init();
        }
        return behaviors.keySet();
    }

    private static void init() {
        behaviors = new HashMap<>();
        behaviors.put("goto_player", new gDoableThing(){
            public void doItem(gThing p) {
                cBotsLogic.goToNearestPlayer(p);
            }
        });
        behaviors.put("goto_scorepoint", new gDoableThing(){
            public void doItem(gThing p) {
                cBotsLogic.goToNearestThingOfType(p, "PROP_SCOREPOINT");
            }
        });
        behaviors.put("ctf", new gDoableThing(){
            public void doItem(gThing p) {
                if(cVars.isVal("flagmasterid", p.get("id")))
                    cBotsLogic.goToFirstThing(p, "PROP_FLAGBLUE");
                else if(cVars.get("flagmasterid").length() > 0)
                    cBotsLogic.goToFlagPlayer(p);
                else
                    cBotsLogic.goToFirstThing(p, "PROP_FLAGRED");
            }
        });
        behaviors.put("flagmaster", new gDoableThing(){
            public void doItem(gThing p) {
                if(cVars.isVal("flagmasterid", p.get("id")))
                    cBotsLogic.runFromNearestPlayer(p);
                else if(cVars.get("flagmasterid").length() > 0)
                    cBotsLogic.goToFlagPlayer(p);
                else
                    cBotsLogic.goToFirstThing(p, "PROP_FLAGRED");
            }
        });
        behaviors.put("virus", new gDoableThing(){
            public void doItem(gThing p) {
                if(nServer.clientArgsMap.containsKey("server")
                        && nServer.clientArgsMap.get("server").containsKey("state")
                        && !nServer.clientArgsMap.get("server").get("state").contains(p.get("id"))){
                    cBotsLogic.runFromNearestVirusPlayer(p);
                }
                else {
                    cBotsLogic.goToNearestVirusPlayer(p);
                }
            }
        });
        behaviors.put("goto_teleporter_virus", new gDoableThing(){
            public void doItem(gThing p) {
                if(nServer.clientArgsMap.containsKey("server")
                        && nServer.clientArgsMap.get("server").containsKey("state")
                        && !nServer.clientArgsMap.get("server").get("state").contains(p.get("id"))){
                    if(!cBotsLogic.inVirusChaseRange(p))
                        cBotsLogic.goToNearestThingOfType(p, "PROP_TELEPORTER");
                    else
                        cBotsLogic.runFromNearestVirusPlayer(p);
                }
                else {
                    if(!cBotsLogic.inVirusChaseRange(p))
                        cBotsLogic.goToNearestThingOfType(p, "PROP_TELEPORTER");
                    else
                        cBotsLogic.goToNearestVirusPlayer(p);
                }
            }
        });
        behaviors.put("goto_safezone", new gDoableThing(){
            public void doItem(gThing p) {
                cBotsLogic.goToSafeZone(p);
            }
        });
        behaviors.put("goto_kofflag", new gDoableThing(){
            public void doItem(gThing p) {
                cBotsLogic.goToNearestThingOfType(p, "PROP_FLAGRED");
            }
        });
    }

    public static void shootAtNearestPlayer(gThing bot) {
        int x1 = bot.getInt("coordx") + bot.getInt("dimw") / 2;
        int y1 = bot.getInt("coordy") + bot.getInt("dimh") / 2;
        gPlayer waypoint = null;
        for(String id : gScene.getPlayerIds()) {
            gPlayer p = gScene.getPlayerById(id);
            int x2 = p.getInt("coordx") + p.getInt("dimw")/2;
            int y2 = p.getInt("coordy") + p.getInt("dimh")/2;
            if(waypoint == null || (Math.abs(x2 - x1) < Math.abs(waypoint.getInt("coordx") - x1)
                    && Math.abs(y2 - y1) < Math.abs(waypoint.getInt("coordy") - y1))) {
                if(!p.get("id").equals(bot.get("id")))
                    waypoint = p;
            }
        }
        if(waypoint != null) {
            if(inBotWeaponRange(bot,waypoint)) {
                gPlayer botPlayer = (gPlayer) bot;
                int waypointErrorMargin = eUtils.scaleInt(300);
                int rx = (int)(Math.random()*waypointErrorMargin-waypointErrorMargin/2);
                int ry = (int)(Math.random()*waypointErrorMargin-waypointErrorMargin/2);
                cScripts.pointPlayerAtCoords(botPlayer,
                        rx + waypoint.getInt("coordx") + waypoint.getInt("dimw")/2,
                        ry + waypoint.getInt("coordy") + waypoint.getInt("dimh")/2);
                botPlayer.fireWeapon();
                nServer.addSendCmd("fireweapon " + botPlayer.get("id") + " " + botPlayer.get("weapon"));
//                cVars.put("sendcmd", "fireweapon " + botPlayer.get("id") + " " + botPlayer.get("weapon"));
            }
//            else {
////                bot.put("sendshot", "0");
//            }
        }
    }

    public static void runFromNearestPlayer(gThing bot) {
        int x1 = bot.getInt("coordx") + bot.getInt("dimw") / 2;
        int y1 = bot.getInt("coordy") + bot.getInt("dimh") / 2;
        gPlayer waypoint = null;
        for(String id : gScene.getPlayerIds()) {
            gPlayer p = gScene.getPlayerById(id);
            int x2 = p.getInt("coordx") + p.getInt("dimw")/2;
            int y2 = p.getInt("coordy") + p.getInt("dimh")/2;
            if(!p.get("id").equals(bot.get("id"))) {
                if(waypoint == null || (Math.abs(x2 - x1) < Math.abs(waypoint.getInt("coordx") - x1)
                        && Math.abs(y2 - y1) < Math.abs(waypoint.getInt("coordy") - y1)
                        && p.getInt("coordx") > -9000 && p.getInt("coordy") > -9000)) {
                    waypoint = p;
                }
            }
        }
        if(waypoint != null) {
            shootAtNearestPlayer(bot);
            runFromWaypoint(bot, waypoint);
        }
    }

    public static void goToFlagPlayer(gThing bot) {
        gPlayer waypoint = gScene.getPlayerById(cVars.get("flagmasterid"));
        if(waypoint != null) {
            shootAtNearestPlayer(bot);
            goToWaypoint(bot, waypoint);
        }
    }

    public static void goToFirstThing(gThing bot, String thingType) {
        gProp waypoint = null;
        HashMap<String, gThing> thingMap = eManager.currentMap.scene.getThingMap(thingType);
        for(String id : thingMap.keySet()) {
            waypoint = (gProp) thingMap.get(id);
            break;
        }
        if(waypoint != null) {
            shootAtNearestPlayer(bot);
            goToWaypoint(bot, waypoint);
        }
    }

    public static void goToSafeZone(gThing bot) {
        gProp waypoint = null;
        HashMap scorepointsMap = eManager.currentMap.scene.getThingMap("PROP_SCOREPOINT");
        for(Object id : scorepointsMap.keySet()) {
            gProp safezone = (gProp) scorepointsMap.get(id);
            if(safezone.isOne("int0")) {
                waypoint = safezone;
                break;
            }
        }
        if(waypoint != null) {
            shootAtNearestPlayer(bot);
            goToWaypoint(bot, waypoint);
        }
    }

    private static boolean goToWaypointPropVerification(gThing bot, gThing p) {
        int propcode = p.getInt("code");
        switch (propcode) {
            case gProps.TELEPORTER: //generic teleporter operation
                return !p.isVal("tag", bot.get("exitteleportertag"));
            case gProps.SCOREPOINT: //waypoints game mode
                return p.isOne("int0");
            default:
                break;
        }
        return true;
    }

    public static void goToNearestPlayer(gThing bot) {
        int x1 = bot.getInt("coordx") + bot.getInt("dimw") / 2;
        int y1 = bot.getInt("coordy") + bot.getInt("dimh") / 2;
        gPlayer waypoint = null;
        for(String id : gScene.getPlayerIds()) {
            gPlayer p = gScene.getPlayerById(id);
            int x2 = p.getInt("coordx") + p.getInt("dimw")/2;
            int y2 = p.getInt("coordy") + p.getInt("dimh")/2;
            if(waypoint == null || (Math.abs(x2 - x1) < Math.abs(waypoint.getInt("coordx") - x1)
                    && Math.abs(y2 - y1) < Math.abs(waypoint.getInt("coordy") - y1))) {
                if(!p.get("id").equals(bot.get("id")))
                    waypoint = p;
            }
        }
        if(waypoint != null) {
            shootAtNearestPlayer(bot);
            goToWaypoint(bot, waypoint);
        }
    }

    public static void goToNearestThingOfType(gThing bot, String thingType) {
        int x1 = bot.getInt("coordx") + bot.getInt("dimw") / 2;
        int y1 = bot.getInt("coordy") + bot.getInt("dimh") / 2;
        gThing waypoint = null;
        HashMap thingMap = eManager.currentMap.scene.getThingMap(thingType);
        for(Object id: thingMap.keySet()) {
            gThing p = (gThing) thingMap.get(id);
            int x2 = p.getInt("coordx") + p.getInt("dimw")/2;
            int y2 = p.getInt("coordy") + p.getInt("dimh")/2;
            if(waypoint == null || (Math.abs(x2 - x1) < Math.abs(waypoint.getInt("coordx") - x1)
                    && Math.abs(y2 - y1) < Math.abs(waypoint.getInt("coordy") - y1))) {
                //this str0 value is used in KOF
                int gameMode = cVars.getInt("gamemode");
                switch (gameMode) {
                    case cGameMode.KING_OF_FLAGS:
                        if(!p.isVal("str0", bot.get("id"))) {
                            if (goToWaypointPropVerification(bot, p))
                                waypoint = p;
                        }
                        break;
                    case cGameMode.RACE:
                        if(!p.get("racebotidcheckins").contains(bot.get("id")))
                            waypoint = p;
                        break;
                    default:
                        if(goToWaypointPropVerification(bot, p))
                            waypoint = p;
                        break;
                }
            }
        }
        if(waypoint != null) {
            shootAtNearestPlayer(bot);
            goToWaypoint(bot, waypoint);
        }
    }

    private static void actOnNearestVirusPlayer(gThing bot, boolean offense) {
        if(nServer.clientArgsMap.containsKey("server")) {
            int x1 = bot.getInt("coordx") + bot.getInt("dimw") / 2;
            int y1 = bot.getInt("coordy") + bot.getInt("dimh") / 2;
            gPlayer waypoint = null;
            if(nServer.clientArgsMap.get("server").containsKey("state")
            && nServer.clientArgsMap.get("server").get("state").contains("virus")) {
                String stateString = nServer.clientArgsMap.get("server").get("state").replace("virus", "");
                if(offense) {
                    for(String id : gScene.getPlayerIds()) {
                        if(!stateString.contains(id)) {
                            gPlayer p = gScene.getPlayerById(id);
                            int x2 = p.getInt("coordx") + p.getInt("dimw")/2;
                            int y2 = p.getInt("coordy") + p.getInt("dimh")/2;
                            if(waypoint == null || (Math.abs(x2 - x1) < Math.abs(waypoint.getInt("coordx") - x1)
                                    && Math.abs(y2 - y1) < Math.abs(waypoint.getInt("coordy") - y1))) {
                                if(p.getInt("coordx") > -9000 && p.getInt("coordy") > -9000){
                                    waypoint = p;
                                }
                            }
                        }
                    }
                }
                else {
                    String[] virusIds = stateString.split("-");
                    for(String id : virusIds) {
                        if(id.length() > 0) {
                            gPlayer p = gScene.getPlayerById(id);
                            if(p != null) {
                                int x2 = p.getInt("coordx") + p.getInt("dimw")/2;
                                int y2 = p.getInt("coordy") + p.getInt("dimh")/2;
                                if(waypoint == null || (Math.abs(x2 - x1) < Math.abs(waypoint.getInt("coordx") - x1)
                                        && Math.abs(y2 - y1) < Math.abs(waypoint.getInt("coordy") - y1))) {
                                    if(p.getInt("coordx") > -9000 && p.getInt("coordy") > -9000){
                                        waypoint = p;
                                    }
                                }
                            }
                        }
                    }
                }

            }
            if(waypoint != null) {
                shootAtNearestPlayer(bot);
                if(offense)
                    goToWaypoint(bot, waypoint);
                else
                    runFromWaypoint(bot, waypoint);
            }
        }
    }
    
    public static void runFromNearestVirusPlayer(gThing bot) {
        actOnNearestVirusPlayer(bot, false);
    }

    public static void goToNearestVirusPlayer(gThing bot) {
        actOnNearestVirusPlayer(bot, true);
    }

    private static boolean inBotWeaponRange(gThing bot, gThing waypoint) {
        int x1 = bot.getInt("coordx") + bot.getInt("dimw") / 2;
        int y1 = bot.getInt("coordy") + bot.getInt("dimh") / 2;
        int x2 = waypoint.getInt("coordx") + waypoint.getInt("dimw")/2;
        int y2 = waypoint.getInt("coordy") + waypoint.getInt("dimh")/2;
        if(Math.abs(x2 - x1) <= cVars.getInt("weaponbotrange"+bot.get("weapon"))
                && Math.abs(y2-y1) <= cVars.getInt("weaponbotrange"+bot.get("weapon"))) {
            return true;
        }
        return false;
    }

    public static boolean inVirusChaseRange(gThing bot) {
        int x1 = bot.getInt("coordx") + bot.getInt("dimw") / 2;
        int y1 = bot.getInt("coordy") + bot.getInt("dimh") / 2;
        for(String id : gScene.getPlayerIds()) {
            gPlayer waypoint = gScene.getPlayerById(id);
            if(!waypoint.isVal("id", bot.get("id"))) {
                int x2 = waypoint.getInt("coordx") + waypoint.getInt("dimw")/2;
                int y2 = waypoint.getInt("coordy") + waypoint.getInt("dimh")/2;
                if(Math.abs(x2 - x1) <= cVars.getInt("botviruschaserange")
                        && Math.abs(y2-y1) <= cVars.getInt("botviruschaserange")) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void runFromWaypoint(gThing bot, gThing waypoint) {
        int x1 = bot.getInt("coordx") + bot.getInt("dimw") / 2;
        int y1 = bot.getInt("coordy") + bot.getInt("dimh") / 2;
        int x2 = waypoint.getInt("coordx") + waypoint.getInt("dimw")/2;
        int y2 = waypoint.getInt("coordy") + waypoint.getInt("dimh")/2;
        bot.put("vel0", "0");
        bot.put("vel1", "0");
        bot.put("vel2", "0");
        bot.put("vel3", "0");
        int speed = 3*cVars.getInt("velocityplayerbase")/4;
        if(x2 > x1) {
            int modspeed = speed + (int)(
                    Math.random()*(3*cVars.getInt("velocityplayerbase")/4)-(3*cVars.getInt("velocityplayerbase")/4));
            bot.putInt("vel2", modspeed);
        }
        if(y2 > y1) {
            int modspeed = speed + (int)(
                    Math.random()*(3*cVars.getInt("velocityplayerbase")/4)-(3*cVars.getInt("velocityplayerbase")/4));
            bot.putInt("vel0", modspeed);
        }
        if(x1 > x2) {
            int modspeed = speed + (int)(
                    Math.random()*(3*cVars.getInt("velocityplayerbase")/4)-(3*cVars.getInt("velocityplayerbase")/4));
            bot.putInt("vel3", modspeed);
        }
        if(y1 > y2) {
            int modspeed = speed + (int)(
                    Math.random()*(3*cVars.getInt("velocityplayerbase")/4)-(3*cVars.getInt("velocityplayerbase")/4));
            bot.putInt("vel1", modspeed);
        }
        if(bot.getInt("vel1") > 0 && bot.getInt("vel3") > 0)
            bot.put("fv", Double.toString(3*Math.PI/4));
        else if(bot.getInt("vel1") > 0 && bot.getInt("vel2") > 0)
            bot.put("fv", Double.toString(3*Math.PI/2));
        else if(bot.getInt("vel1") > 0)
            bot.put("fv", Double.toString(Math.PI));
        else
            bot.put("fv", Double.toString(2*Math.PI));
    }

    private static void goToWaypoint(gThing bot, gThing waypoint) {
        int x1 = bot.getInt("coordx") + bot.getInt("dimw") / 2;
        int y1 = bot.getInt("coordy") + bot.getInt("dimh") / 2;
        int x2 = waypoint.getInt("coordx") + waypoint.getInt("dimw")/2;
        int y2 = waypoint.getInt("coordy") + waypoint.getInt("dimh")/2;
        bot.put("vel0", "0");
        bot.put("vel1", "0");
        bot.put("vel2", "0");
        bot.put("vel3", "0");
        int speed = 3*cVars.getInt("velocityplayerbase")/4;
        if(x2 > x1) {
            int modspeed = speed + (int)(
                    Math.random()*(3*cVars.getInt("velocityplayerbase")/4)-(3*cVars.getInt("velocityplayerbase")/4));
            bot.putInt("vel3", modspeed);
        }
        if(x1 > x2) {
            int modspeed = speed + (int)(
                    Math.random()*(3*cVars.getInt("velocityplayerbase")/4)-(3*cVars.getInt("velocityplayerbase")/4));
            bot.putInt("vel2", modspeed);
        }
        if(cVars.getInt("mapview") != gMap.MAP_SIDEVIEW) {
            if(y2 > y1) {
                int modspeed = speed + (int)(
                        Math.random()*(3*cVars.getInt("velocityplayerbase")/4)-(3*cVars.getInt("velocityplayerbase")/4));
                bot.putInt("vel1", modspeed);
            }
            if(y1 > y2) {
                int modspeed = speed + (int)(
                        Math.random()*(3*cVars.getInt("velocityplayerbase")/4)-(3*cVars.getInt("velocityplayerbase")/4));
                bot.putInt("vel0", modspeed);
            }
        }
        else {
            bot.putInt("vel1", cVars.getInt("velocityplayerbase")/2);
        }
        if(bot.getInt("vel1") > 0 && bot.getInt("vel3") > 0)
            bot.put("fv", Double.toString(Math.PI/2));
        else if(bot.getInt("vel1") > 0 && bot.getInt("vel2") > 0)
            bot.put("fv", Double.toString(3*Math.PI/2));
        else if(bot.getInt("vel1") > 0)
            bot.put("fv", Double.toString(Math.PI));
        else
            bot.put("fv", Double.toString(2*Math.PI));
    }
}
