import java.util.HashMap;

public class cBotsLogic {
    //string title maps to doable, fetched via key=gametype by server
    static long bottime = 0;
    private static HashMap<String, gDoableThing> behaviors = null;
    public static gDoableThing getBehavior(String key) {
        if(behaviors == null) {
            init();
        }
        return behaviors.get(key);
    }

    private static void init() {
        behaviors = new HashMap<>();
        behaviors.put("Kill Master", new gDoableThing(){
            public void doItem(gThing p) {
                cBotsLogic.goToNearestPlayer(p);
            }
        });
        behaviors.put("Flag Master", new gDoableThing(){
            public void doItem(gThing p) {
                String flagmasterid = null;
                if(nServer.instance().clientArgsMap.get("server").containsKey("flagmasterid"))
                    flagmasterid = nServer.instance().clientArgsMap.get("server").get("flagmasterid");
                if(flagmasterid == null)
                    cBotsLogic.goToFirstThing(p, "ITEM_FLAG");
                else if(p.contains("id") && flagmasterid.equals(p.get("id")))
                    cBotsLogic.runFromNearestPlayer(p);
                else if(flagmasterid.length() > 0)
                    cBotsLogic.goToFlagPlayer(p);
                else
                    cBotsLogic.goToFirstThing(p, "ITEM_FLAG");
            }
        });
        behaviors.put("Virus Master", new gDoableThing(){
            public void doItem(gThing p) {
                if(nServer.instance().clientArgsMap.containsKey("server")
                        && nServer.instance().clientArgsMap.get("server").containsKey("virusids")
                        && !nServer.instance().clientArgsMap.get("server").get("virusids").contains(p.get("id"))){
                    cBotsLogic.runFromNearestVirusPlayer(p);
                }
                else {
                    cBotsLogic.goToNearestVirusPlayer(p);
                }
            }
        });
    }

    public static void shootAtNearestPlayer(gThing bot) {
        int x1 = bot.getInt("coordx") + bot.getInt("dimw") / 2;
        int y1 = bot.getInt("coordy") + bot.getInt("dimh") / 2;
        gPlayer waypoint = null;
        for(String id : cServerLogic.getPlayerIds()) {
            gPlayer p = cServerLogic.getPlayerById(id);
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
                botPlayer.pointAtCoords(
                        rx + waypoint.getInt("coordx") + waypoint.getInt("dimw")/2,
                        ry + waypoint.getInt("coordy") + waypoint.getInt("dimh")/2);
                String cmd = "fireweapon " + botPlayer.get("id") + " " + botPlayer.get("weapon");
                nServer.instance().addExcludingNetCmd("server",
                        cmd.replaceFirst("fireweapon", "cl_fireweapon"));
                xCon.ex(cmd);
            }
        }
    }

    public static void runFromNearestPlayer(gThing bot) {
        int x1 = bot.getInt("coordx") + bot.getInt("dimw") / 2;
        int y1 = bot.getInt("coordy") + bot.getInt("dimh") / 2;
        gPlayer waypoint = null;
        for(String id : cServerLogic.getPlayerIds()) {
            gPlayer p = cServerLogic.getPlayerById(id);
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
        if(nServer.instance().clientArgsMap.get("server").containsKey("flagmasterid")) {
            gPlayer waypoint = cServerLogic.getPlayerById(
                    nServer.instance().clientArgsMap.get("server").get("flagmasterid"));
            if(waypoint != null) {
                shootAtNearestPlayer(bot);
                goToWaypoint(bot, waypoint);
            }
        }
    }

    public static void goToFirstThing(gThing bot, String thingType) {
        gThing waypoint = null;
        HashMap<String, gThing> thingMap = cServerLogic.scene.getThingMap(thingType);
        for(String id : thingMap.keySet()) {
            waypoint = thingMap.get(id);
            break;
        }
        if(waypoint != null) {
            shootAtNearestPlayer(bot);
            goToWaypoint(bot, waypoint);
        }
    }


    public static void goToNearestPlayer(gThing bot) {
        int x1 = bot.getInt("coordx") + bot.getInt("dimw") / 2;
        int y1 = bot.getInt("coordy") + bot.getInt("dimh") / 2;
        gPlayer waypoint = null;
        for(String id : cServerLogic.getPlayerIds()) {
            gPlayer p = cServerLogic.getPlayerById(id);
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

    private static void actOnNearestVirusPlayer(gThing bot, boolean offense) {
        if(nServer.instance().clientArgsMap.containsKey("server")) {
            int x1 = bot.getInt("coordx") + bot.getInt("dimw") / 2;
            int y1 = bot.getInt("coordy") + bot.getInt("dimh") / 2;
            gPlayer waypoint = null;
            if(nServer.instance().clientArgsMap.get("server").containsKey("virusids")) {
                String stateString = nServer.instance().clientArgsMap.get("server").get("virusids");
                if(offense) {
                    for(String id : cServerLogic.getPlayerIds()) {
                        if(!stateString.contains(id)) {
                            gPlayer p = cServerLogic.getPlayerById(id);
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
                            gPlayer p = cServerLogic.getPlayerById(id);
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
        for(String id : cServerLogic.getPlayerIds()) {
            gPlayer waypoint = cServerLogic.getPlayerById(id);
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
        bot.put("botvel0", "0");
        bot.put("botvel1", "0");
        bot.put("botvel2", "0");
        bot.put("botvel3", "0");
        int velocityPlayerBase = cVars.getInt("velocityplayerbase");
        int speed = 3*velocityPlayerBase/4;
        if(x2 > x1) {
            int modspeed = speed + (int)(
                    Math.random()*(speed)-(speed));
            bot.putInt("botvel2", modspeed);
        }
        if(y2 > y1) {
            int modspeed = speed + (int)(
                    Math.random()*(speed)-(speed));
            bot.putInt("botvel0", modspeed);
        }
        if(x1 > x2) {
            int modspeed = speed + (int)(
                    Math.random()*(speed)-(speed));
            bot.putInt("botvel3", modspeed);
        }
        if(y1 > y2) {
            int modspeed = speed + (int)(
                    Math.random()*(speed)-(speed));
            bot.putInt("botvel1", modspeed);
        }
        if(bot.getInt("botvel1") > 0 && bot.getInt("botvel3") > 0)
            bot.put("fv", Double.toString(3*Math.PI/4));
        else if(bot.getInt("botvel1") > 0 && bot.getInt("botvel2") > 0)
            bot.put("fv", Double.toString(3*Math.PI/2));
        else if(bot.getInt("botvel1") > 0)
            bot.put("fv", Double.toString(Math.PI));
        else
            bot.put("fv", Double.toString(2*Math.PI));
    }

    private static void goToWaypoint(gThing bot, gThing waypoint) {
        int x1 = bot.getInt("coordx") + bot.getInt("dimw") / 2;
        int y1 = bot.getInt("coordy") + bot.getInt("dimh") / 2;
        int x2 = waypoint.getInt("coordx") + waypoint.getInt("dimw")/2;
        int y2 = waypoint.getInt("coordy") + waypoint.getInt("dimh")/2;
        bot.put("botvel0", "0");
        bot.put("botvel1", "0");
        bot.put("botvel2", "0");
        bot.put("botvel3", "0");
        int speed = 3*cVars.getInt("velocityplayerbase")/4;
        if(x2 > x1) {
            int modspeed = speed + (int)(
                    Math.random()*(3*cVars.getInt("velocityplayerbase")/4)-(3*cVars.getInt("velocityplayerbase")/4));
            bot.putInt("botvel3", modspeed);
        }
        if(x1 > x2) {
            int modspeed = speed + (int)(
                    Math.random()*(3*cVars.getInt("velocityplayerbase")/4)-(3*cVars.getInt("velocityplayerbase")/4));
            bot.putInt("botvel2", modspeed);
        }
        if(y2 > y1) {
            int modspeed = speed + (int)(
                    Math.random()*(3*cVars.getInt("velocityplayerbase")/4)-(3*cVars.getInt("velocityplayerbase")/4));
            bot.putInt("botvel1", modspeed);
        }
        if(y1 > y2) {
            int modspeed = speed + (int)(
                    Math.random()*(3*cVars.getInt("velocityplayerbase")/4)-(3*cVars.getInt("velocityplayerbase")/4));
            bot.putInt("botvel0", modspeed);
        }
        if(bot.getInt("botvel1") > 0 && bot.getInt("botvel3") > 0)
            bot.put("fv", Double.toString(Math.PI/2));
        else if(bot.getInt("botvel1") > 0 && bot.getInt("botvel2") > 0)
            bot.put("fv", Double.toString(3*Math.PI/2));
        else if(bot.getInt("botvel1") > 0)
            bot.put("fv", Double.toString(Math.PI));
        else
            bot.put("fv", Double.toString(2*Math.PI));
    }
}
