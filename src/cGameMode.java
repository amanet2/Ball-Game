import java.util.Arrays;
import java.util.HashMap;

public class cGameMode {
    static final int DEATHMATCH = 0;
    static final int RACE = 1;
    static final int SAFE_ZONES = 2;
    static final int CAPTURE_THE_FLAG = 3;
    static final int KING_OF_FLAGS = 4;
    static final int WAYPOINTS = 5;
    static final int VIRUS = 6;
    static final int FLAG_MASTER = 7;

    static String[] net_gamemode_texts = {
            "Deathmatch",
            "Race",
            "Safe Zone",
            "Capture the Flag",
            "King of Flags",
            "Waypoints",
            "Virus",
            "Flag Master"
    };
    static String[] net_gamemode_descriptions = {
            "Kill other players for points",
            "Capture every waypoint to complete a lap",
            "Be in the zone when the timer ends",
            "Bring the red flag to the blue flag",
            "Earn points for each flag held",
            "Race to the waypoint",
            "Don't catch the virus",
            "Hold onto the flag for the longest time"
    };

    public static void refreshSafeZones() {
        String[] propids = new String[]{};
        HashMap safezones = eManager.currentMap.scene.getThingMap("PROP_SCOREPOINT");
        if(safezones.size() > 0) {
            for(Object id : safezones.keySet()) {
                gProp safezone = (gProp) safezones.get(id);
                safezone.put("int0", "0");
                String[] tmp = Arrays.copyOf(propids, propids.length+1);
                tmp[tmp.length-1] = (String) id;
                propids = tmp;
            }
            int rando = (int)(Math.random()*(double)(propids.length));
            gProp nextactiveszone = (gProp) safezones.get(propids[rando]);
            nextactiveszone.put("int0", "1");
        }
    }

    public static void resetKingOfFlags() {
        HashMap<String, gThing> thingMap = eManager.currentMap.scene.getThingMap("PROP_FLAGRED");
        for(String id : thingMap.keySet()) {
            thingMap.get(id).put("str0", "null");
        }
    }

    public static void checkKingOfFlags() {
        if(sSettings.net_server) {
            if(cVars.getLong("kingofflagstime") < uiInterface.gameTime) {
                HashMap<String, gThing> thingMap = eManager.currentMap.scene.getThingMap("PROP_FLAGRED");
                for(String id : thingMap.keySet()) {
                    gProp flag = (gProp) thingMap.get(id);
                    gPlayer givePointPlayer = gScene.getPlayerById(flag.get("str0"));
                    if(givePointPlayer != null) {
                        xCon.ex("givepoint " + flag.get("str0"));
                    }
                }
                cVars.putLong("kingofflagstime", uiInterface.gameTime + 1000);
            }
        }
    }

    public static void checkWaypoints() {
        HashMap scorepoints = eManager.currentMap.scene.getThingMap("PROP_SCOREPOINT");
        if(scorepoints.size() > 0) {
            boolean noneActive = true;
            for(Object id : scorepoints.keySet()) {
                gProp scorepoint = (gProp) scorepoints.get(id);
                if(scorepoint.getInt("int0") > 0) {
                    noneActive = false;
                    break;
                }
            }
            if(noneActive)
                refreshWaypoints();
        }
    }

    public static void refreshWaypoints() {
        String[] propids = new String[]{};
        HashMap scorepoints = eManager.currentMap.scene.getThingMap("PROP_SCOREPOINT");
        for(Object id : scorepoints.keySet()) {
            gProp scorepoint = (gProp) scorepoints.get(id);
            scorepoint.put("int0", "0");
            String[] tmp = Arrays.copyOf(propids, propids.length+1);
            tmp[tmp.length-1] = (String) id;
            propids = tmp;
        }
        int rando = (int)(Math.random()*(double)(propids.length));
        gProp nextactivescorepoint = (gProp) scorepoints.get(propids[rando]);
        nextactivescorepoint.put("int0", "1");
    }

    public static void resetVirusPlayers() {
        cVars.assignRandomPlayerIdToVar("virusids");
        cVars.put("virusids", cVars.get("virusids")+"-");
    }

    public static void checkVirus() {
        if(sSettings.net_server) {
            if(cVars.getLong("virustime") < uiInterface.gameTime) {
                if(nServer.clientArgsMap.containsKey("server")) {
                    for(String id : gScene.getPlayerIds()) {
                        gPlayer p = gScene.getPlayerById(id);
                        if(nServer.clientArgsMap.get("server").containsKey("state")
                                && !nServer.clientArgsMap.get("server").get("state").contains(id)
                                && p.getInt("coordx") > -9000 && p.getInt("coordy") > -9000) {
                            xCon.ex("givepoint " + p.get("id"));
                        }
                    }
                }
                cVars.putLong("virustime", uiInterface.gameTime + 1000);
            }
        }
    }

    public static void checkFlagMaster() {
        if(sSettings.net_server) {
            if(cVars.get("flagmasterid").length() > 0
                    && cVars.getLong("flagmastertime") < uiInterface.gameTime) {
                xCon.ex("givepoint " + cVars.get("flagmasterid"));
                cVars.putLong("flagmastertime", uiInterface.gameTime + 1000);
            }
        }
    }
}
