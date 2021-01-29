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
                cBotsLogic.goToNearestScorePoint(p);
            }
        });
        behaviors.put("ctf", new gDoableThing(){
            public void doItem(gThing p) {
                if(cVars.isVal("flagmasterid", p.get("id")))
                    cBotsLogic.goToBlueFlagCtf(p);
                else if(cVars.get("flagmasterid").length() > 0)
                    cBotsLogic.goToFlagPlayer(p);
                else
                    cBotsLogic.goToRedFlagCtf(p);
            }
        });
        behaviors.put("flagmaster", new gDoableThing(){
            public void doItem(gThing p) {
                if(cVars.isVal("flagmasterid", p.get("id")))
                    cBotsLogic.runFromNearestPlayer(p);
                else if(cVars.get("flagmasterid").length() > 0)
                    cBotsLogic.goToFlagPlayer(p);
                else
                    cBotsLogic.goToRedFlagCtf(p);
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
                        cBotsLogic.goToNearestTeleporter(p);
                    else
                        cBotsLogic.runFromNearestVirusPlayer(p);
                }
                else {
                    if(!cBotsLogic.inVirusChaseRange(p))
                        cBotsLogic.goToNearestTeleporter(p);
                    else
                        cBotsLogic.goToNearestVirusPlayer(p);
                }
            }
        });
        behaviors.put("virus_single", new gDoableThing(){
            public void doItem(gThing p) {
                if(cVars.isVal("virussingleid", p.get("id")))
                    cBotsLogic.goToNearestPlayer(p);
                else
                    cBotsLogic.runFromNearestVirusSinglePlayer(p);
            }
        });
        behaviors.put("anti_chosenone", new gDoableThing(){
            public void doItem(gThing p) {
                if(cVars.isVal("chosenoneid", p.get("id")))
                    cBotsLogic.goToNearestPlayer(p);
                else
                    cBotsLogic.goToChosenOne(p);
            }
        });
        behaviors.put("chosenone", new gDoableThing(){
            public void doItem(gThing p) {
                if(cVars.isVal("chosenoneid", p.get("id")))
                    cBotsLogic.runFromNearestPlayer(p);
                else
                    cBotsLogic.goToChosenOne(p);
            }
        });
        behaviors.put("goto_ball", new gDoableThing(){
            public void doItem(gThing p) {
                cBotsLogic.goToBall(p);
            }
        });
        behaviors.put("goto_safezone", new gDoableThing(){
            public void doItem(gThing p) {
                cBotsLogic.goToSafeZone(p);
            }
        });
        behaviors.put("goto_kofflag", new gDoableThing(){
            public void doItem(gThing p) {
                cBotsLogic.goToNearestRedFlagKof(p);
            }
        });
    }

    public static void goToNearestPlayer(gThing bot) {
        int x1 = bot.getInt("coordx") + bot.getInt("dimw") / 2;
        int y1 = bot.getInt("coordy") + bot.getInt("dimh") / 2;
        gPlayer waypoint = null;
        for(gPlayer p : eManager.currentMap.scene.players()) {
            int x2 = p.getInt("coordx") + p.getInt("dimw")/2;
            int y2 = p.getInt("coordy") + p.getInt("dimh")/2;
            if(waypoint == null || (Math.abs(x2 - x1) < Math.abs(waypoint.getInt("coordx") - x1)
                                    && Math.abs(y2 - y1) < Math.abs(waypoint.getInt("coordy") - y1))) {
                if(!p.get("id").contains("bot"))
                    waypoint = p;
            }
        }
        if(waypoint != null) {
            shootAtNearestPlayer(bot);
            goToWaypoint(bot, waypoint);
        }
    }

    public static void shootAtNearestPlayer(gThing bot) {
        if(!cVars.isVal("virussingleid", bot.get("id"))) {
            int x1 = bot.getInt("coordx") + bot.getInt("dimw") / 2;
            int y1 = bot.getInt("coordy") + bot.getInt("dimh") / 2;
            gPlayer waypoint = null;
            for(gPlayer p : eManager.currentMap.scene.players()) {
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
                    cScripts.pointPlayerAtCoords(botPlayer, rx + waypoint.getInt("coordx") + waypoint.getInt("dimw")/2,
                            ry + waypoint.getInt("coordy") + waypoint.getInt("dimh")/2);
                    botPlayer.fireWeapon();
                }
                else {
                    bot.put("sendshot", "0");
                }
            }
        }
    }

    public static void goToNearestScorePoint(gThing bot) {
        gProp waypoint = null;
        for(gProp p : eManager.currentMap.scene.props()) {
            if(p.isInt("code", gProp.SCOREPOINT)
                    && cVars.getInt("gamemode") == cGameMode.RACE
                    ? !p.get("racebotidcheckins").contains(bot.get("id")) : p.isOne("int0")) {
                waypoint = p;
                break;
            }
        }
        if(waypoint != null) {
            shootAtNearestPlayer(bot);
            goToWaypoint(bot, waypoint);
        }
    }

    public static void runFromNearestVirusSinglePlayer(gThing bot) {
        gPlayer waypoint = null;
        for(gPlayer p : eManager.currentMap.scene.players()) {
            if(cVars.isVal("virussingleid",p.get("id"))) {
                waypoint = p;
                break;
            }
        }
        if(waypoint != null) {
            shootAtNearestPlayer(bot);
            runFromWaypoint(bot, waypoint);
        }
    }

    public static void runFromNearestPlayer(gThing bot) {
        int x1 = bot.getInt("coordx") + bot.getInt("dimw") / 2;
        int y1 = bot.getInt("coordy") + bot.getInt("dimh") / 2;
        gPlayer waypoint = null;
        for(gPlayer p : eManager.currentMap.scene.players()) {
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
        gPlayer waypoint = null;
        for(gPlayer p : eManager.currentMap.scene.players()) {
            if(cVars.isVal("flagmasterid",p.get("id"))) {
                waypoint = p;
                break;
            }
        }
        if(waypoint != null) {
            shootAtNearestPlayer(bot);
            goToWaypoint(bot, waypoint);
        }
    }

    public static void goToChosenOne(gThing bot) {
        gPlayer waypoint = null;
        for(gPlayer p : eManager.currentMap.scene.players()) {
            if(cVars.isVal("chosenoneid",p.get("id"))) {
                waypoint = p;
                break;
            }
        }
        if(waypoint != null) {
            shootAtNearestPlayer(bot);
            goToWaypoint(bot, waypoint);
        }
    }

    public static void goToRedFlagCtf(gThing bot) {
        gProp waypoint = null;
        for(gProp p : eManager.currentMap.scene.props()) {
            if(p.isInt("code", gProp.FLAGRED)) {
                waypoint = p;
                break;
            }
        }
        if(waypoint != null) {
            shootAtNearestPlayer(bot);
            goToWaypoint(bot, waypoint);
        }
    }

    public static void goToBlueFlagCtf(gThing bot) {
        gProp waypoint = null;
        for(gProp p : eManager.currentMap.scene.props()) {
            if(p.isInt("code", gProp.FLAGBLUE)) {
                waypoint = p;
                break;
            }
        }
        if(waypoint != null) {
            shootAtNearestPlayer(bot);
            goToWaypoint(bot, waypoint);
        }
    }

    public static void goToSafeZone(gThing bot) {
        gProp waypoint = null;
        for(gPropScorepoint safezone : eManager.currentMap.scene.scorepoints()) {
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

    public static void goToBall(gThing bot) {
        gProp waypoint = null;
        for(gProp p : eManager.currentMap.scene.ballbouncys()) {
                waypoint = p;
                break;
        }
        if(waypoint != null) {
            shootAtNearestPlayer(bot);
            goToWaypoint(bot, waypoint);
        }
    }

    public static void goToNearestRedFlagKof(gThing bot) {
        int x1 = bot.getInt("coordx") + bot.getInt("dimw") / 2;
        int y1 = bot.getInt("coordy") + bot.getInt("dimh") / 2;
        gProp waypoint = null;
        for(gProp p : eManager.currentMap.scene.props()) {
            int x2 = p.getInt("coordx") + p.getInt("dimw")/2;
            int y2 = p.getInt("coordy") + p.getInt("dimh")/2;
            if(waypoint == null || (Math.abs(x2 - x1) < Math.abs(waypoint.getInt("coordx") - x1)
                    && Math.abs(y2 - y1) < Math.abs(waypoint.getInt("coordy") - y1))) {
                if(p.isInt("code", gProp.FLAGRED) && !p.isVal("str0", bot.get("id")))
                    waypoint = p;
            }
        }
        if(waypoint != null) {
            shootAtNearestPlayer(bot);
            goToWaypoint(bot, waypoint);
        }
    }

    public static void goToNearestTeleporter(gThing bot) {
        int x1 = bot.getInt("coordx") + bot.getInt("dimw") / 2;
        int y1 = bot.getInt("coordy") + bot.getInt("dimh") / 2;
        gProp waypoint = null;
        for(gProp p : eManager.currentMap.scene.props()) {
            int x2 = p.getInt("coordx") + p.getInt("dimw")/2;
            int y2 = p.getInt("coordy") + p.getInt("dimh")/2;
            if(p.isInt("code", gProp.TELEPORTER)
                    && !p.isVal("tag", bot.get("exitteleportertag"))) {
                if(waypoint == null || (Math.abs(x2 - x1) < Math.abs(waypoint.getInt("coordx") - x1)
                        && Math.abs(y2 - y1) < Math.abs(waypoint.getInt("coordy") - y1))) {
                    waypoint = p;
                }
            }
        }
        if(waypoint != null) {
            shootAtNearestPlayer(bot);
            goToWaypoint(bot, waypoint);
        }
    }
    
    public static void runFromNearestVirusPlayer(gThing bot) {
        int x1 = bot.getInt("coordx") + bot.getInt("dimw") / 2;
        int y1 = bot.getInt("coordy") + bot.getInt("dimh") / 2;
        gPlayer waypoint = null;
        for(gPlayer p : eManager.currentMap.scene.players()) {
            int x2 = p.getInt("coordx") + p.getInt("dimw")/2;
            int y2 = p.getInt("coordy") + p.getInt("dimh")/2;
            if(waypoint == null || (Math.abs(x2 - x1) < Math.abs(waypoint.getInt("coordx") - x1)
                    && Math.abs(y2 - y1) < Math.abs(waypoint.getInt("coordy") - y1))) {
                if(nServer.clientArgsMap.get("server").containsKey("state")
                        && nServer.clientArgsMap.get("server").get("state").contains(p.get("id"))
                        && p.getInt("coordx") > -9000 && p.getInt("coordy") > -9000){
                    waypoint = p;
                }
            }
        }
        if(waypoint != null) {
            shootAtNearestPlayer(bot);
            runFromWaypoint(bot, waypoint);
        }
    }

    public static void goToNearestVirusPlayer(gThing bot) {
        int x1 = bot.getInt("coordx") + bot.getInt("dimw") / 2;
        int y1 = bot.getInt("coordy") + bot.getInt("dimh") / 2;
        gPlayer waypoint = null;
        for(gPlayer p : eManager.currentMap.scene.players()) {
            int x2 = p.getInt("coordx") + p.getInt("dimw")/2;
            int y2 = p.getInt("coordy") + p.getInt("dimh")/2;
            if(waypoint == null || (Math.abs(x2 - x1) < Math.abs(waypoint.getInt("coordx") - x1)
                    && Math.abs(y2 - y1) < Math.abs(waypoint.getInt("coordy") - y1))) {
                if(nServer.clientArgsMap.containsKey("server")
                        && nServer.clientArgsMap.get("server").containsKey("state")
                        && !nServer.clientArgsMap.get("server").get("state").contains(p.get("id"))
                        && p.getInt("coordx") > -9000 && p.getInt("coordy") > -9000){
                    waypoint = p;
                }
            }
        }
        if(waypoint != null) {
            shootAtNearestPlayer(bot);
            goToWaypoint(bot, waypoint);
        }
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
        for(gPlayer waypoint : eManager.currentMap.scene.players()) {
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
        if(cVars.getInt("maptype") != gMap.MAP_SIDEVIEW) {
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
