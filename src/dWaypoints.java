import java.awt.*;
import java.util.HashMap;

public class dWaypoints {
    public static void drawWaypoints(Graphics2D g2) {
        switch (cVars.getInt("gamemode")) {
            case cGameMode.CAPTURE_THE_FLAG:
            case cGameMode.FLAG_MASTER:
                if(!cVars.isVal("flagmasterid", "")) {
                    if(!cVars.get("flagmasterid").equals(uiInterface.uuid)) {
                        gPlayer p = gScene.getPlayerById(cVars.get("flagmasterid"));
                        dScreenFX.drawNavPointer(g2, p.getInt("coordx") + p.getInt("dimw") / 2,
                                p.getInt("coordy") + p.getInt("dimh") / 2, "* KILL *");
                    }
                    else {
                        HashMap flagbluemap = eManager.currentMap.scene.getThingMap("PROP_FLAGBLUE");
                        for(Object id : flagbluemap.keySet()) {
                            gPropFlagBlue flag = (gPropFlagBlue) flagbluemap.get(id);
                            dScreenFX.drawNavPointer(g2,flag.getInt("coordx") + flag.getInt("dimw")/2,
                                    flag.getInt("coordy") + flag.getInt("dimh")/2, "* GO HERE *");
                        }
                    }
                }
                else {
                    HashMap flagredmap = eManager.currentMap.scene.getThingMap("PROP_FLAGRED");
                    for(Object id : flagredmap.keySet()) {
                        gPropFlagRed flag = (gPropFlagRed) flagredmap.get(id);
                        dScreenFX.drawNavPointer(g2,flag.getInt("coordx") + flag.getInt("dimw")/2,
                                flag.getInt("coordy") + flag.getInt("dimh")/2, "* GO HERE *");
                    }
                }
                break;
            case cGameMode.KING_OF_FLAGS:
                HashMap flagredmap = eManager.currentMap.scene.getThingMap("PROP_FLAGRED");
                for(Object id : flagredmap.keySet()) {
                    gPropFlagRed flag = (gPropFlagRed) flagredmap.get(id);
                    if(!flag.isVal("str0", cGameLogic.userPlayer().get("id"))) {
                        dScreenFX.drawNavPointer(g2,flag.getInt("coordx") + flag.getInt("dimw")/2,
                                flag.getInt("coordy") + flag.getInt("dimh")/2, "* GO HERE *");
                    }
                }
                break;
            case cGameMode.VIRUS:
                if(nServer.clientArgsMap != null && nServer.clientArgsMap.containsKey("server")
                        && nServer.clientArgsMap.get("server").containsKey("state")) {
                    String statestr = nServer.clientArgsMap.get("server").get("state");
                    for (String id : gScene.getPlayerIds()) {
                        gPlayer p = gScene.getPlayerById(id);
                        if (statestr.contains(p.get("id"))) {
                            dScreenFX.drawNavPointer(g2, p.getInt("coordx") + p.getInt("dimw") / 2,
                                    p.getInt("coordy") + p.getInt("dimh") / 2,
                                    "* INFECTED *");
                        }
                    }
                }
                break;
            case cGameMode.RACE:
            case cGameMode.WAYPOINTS:
            case cGameMode.SAFE_ZONES:
                HashMap scorepointMap = eManager.currentMap.scene.getThingMap("PROP_SCOREPOINT");
                for(Object id : scorepointMap.keySet()) {
                    gPropScorepoint scorepoint = (gPropScorepoint) scorepointMap.get(id);
                    if(((cVars.isInt("gamemode", cGameMode.WAYPOINTS)
                            || cVars.isInt("gamemode", cGameMode.SAFE_ZONES))
                            && scorepoint.getInt("int0") > 0)
                            || (cVars.isInt("gamemode", cGameMode.RACE) && scorepoint.getInt("int0") < 1)) {
                        dScreenFX.drawNavPointer(g2,
                                scorepoint.getInt("coordx") + scorepoint.getInt("dimw") / 2,
                                scorepoint.getInt("coordy") + scorepoint.getInt("dimh") / 2,
                                "* GO HERE *");
                    }
                }
                break;
            default:
                break;
        }
    }
}
