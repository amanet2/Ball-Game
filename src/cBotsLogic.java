import java.util.HashMap;

public class cBotsLogic {
    //string title maps to doable, fetched via key=gametype by server
    static long bottime = 0;
    static int botviruschaserange = 600;
    static int[] weaponranges = {
            300,
            800,
            400,
            600,
            600,
            300
    };
    private static HashMap<String, gDoableThing> behaviors = null;
    public static gDoableThing getBehavior(String key) {
        if(behaviors == null) {
            init();
        }
        return behaviors.get(key);
    }

    private static void init() {
        behaviors = new HashMap<>();
        behaviors.put("Rock Master", new gDoableThing(){
            public void doItem(gThing p) {
                cBotsLogic.goToNearestPlayer(p);
            }
        });
        behaviors.put("Flag Master", new gDoableThing(){
            public void doItem(gThing p) {
                String flagmasterid = null;
                if(nServer.instance().serverVars.containsKey("flagmasterid"))
                    flagmasterid = nServer.instance().serverVars.get("flagmasterid");
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
                if(nServer.instance().serverVars.containsKey("virusids")
                        && !nServer.instance().serverVars.get("virusids").contains(p.get("id"))){
                    cBotsLogic.runFromNearestVirusPlayer(p);
                }
                else {
                    cBotsLogic.goToNearestVirusPlayer(p);
                }
            }
        });
        behaviors.put("Gold Master", new gDoableThing(){
            public void doItem(gThing p) {
                cBotsLogic.goToNearestPlayer(p);
            }
        });

    }

    public static void shootAtNearestPlayer(gThing bot) {
        int x1 = bot.getInt("coordx") + bot.getInt("dimw") / 2;
        int y1 = bot.getInt("coordy") + bot.getInt("dimh") / 2;
        gPlayer waypoint = null;
        for(String id : nServer.instance().masterStateMap.keys()) {
            gPlayer p = cServerLogic.getPlayerById(id);
            if(p == null)
                continue;
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
                xCon.ex(String.format("exec scripts/botshoot %s", botPlayer.get("id")));
            }
        }
    }

    public static void runFromNearestPlayer(gThing bot) {
        int x1 = bot.getInt("coordx") + bot.getInt("dimw") / 2;
        int y1 = bot.getInt("coordy") + bot.getInt("dimh") / 2;
        gPlayer waypoint = null;
        for(String id : nServer.instance().masterStateMap.keys()) {
            gPlayer p = cServerLogic.getPlayerById(id);
            if(p == null)
                continue;
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
        if(nServer.instance().serverVars.containsKey("flagmasterid")) {
            gPlayer waypoint = cServerLogic.getPlayerById(
                    nServer.instance().serverVars.get("flagmasterid"));
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
        for(String id : nServer.instance().masterStateMap.keys()) {
            gPlayer p = cServerLogic.getPlayerById(id);
            if(p == null)
                continue;
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
        int x1 = bot.getInt("coordx") + bot.getInt("dimw") / 2;
        int y1 = bot.getInt("coordy") + bot.getInt("dimh") / 2;
        gPlayer waypoint = null;
        if(nServer.instance().serverVars.containsKey("virusids")) {
            String stateString = nServer.instance().serverVars.get("virusids");
            if(offense) {
                for(String id : nServer.instance().masterStateMap.keys()) {
                    if(!stateString.contains(id)) {
                        gPlayer p = cServerLogic.getPlayerById(id);
                        if(p == null)
                            continue;
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
        return Math.abs(x2 - x1) <= weaponranges[bot.getInt("weapon")]
                && Math.abs(y2 - y1) <= weaponranges[bot.getInt("weapon")];
    }

    public static boolean inVirusChaseRange(gThing bot) {
        int x1 = bot.getInt("coordx") + bot.getInt("dimw") / 2;
        int y1 = bot.getInt("coordy") + bot.getInt("dimh") / 2;
        for(String id : nServer.instance().masterStateMap.keys()) {
            gPlayer waypoint = cServerLogic.getPlayerById(id);
            if(waypoint == null)
                continue;
            if(!waypoint.isVal("id", bot.get("id"))) {
                int x2 = waypoint.getInt("coordx") + waypoint.getInt("dimw")/2;
                int y2 = waypoint.getInt("coordy") + waypoint.getInt("dimh")/2;
                if(Math.abs(x2 - x1) <= botviruschaserange && Math.abs(y2-y1) <= botviruschaserange)
                    return true;
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
        int speed = 3*cServerLogic.velocityplayerbase/4;
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
        int speed = 3*cServerLogic.velocityplayerbase/4;
        if(x2 > x1)
            bot.putInt("botvel3", speed + (int)(Math.random()*(speed)-(speed)));
        if(x1 > x2)
            bot.putInt("botvel2", speed + (int)(Math.random()*(speed)-(speed)));
        if(y2 > y1)
            bot.putInt("botvel1", speed + (int)(Math.random()*(speed)-(speed)));
        if(y1 > y2)
            bot.putInt("botvel0", speed + (int)(Math.random()*(speed)-(speed)));
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
