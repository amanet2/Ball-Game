import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.HashMap;

public class dWaypoints {
    public static void drawNavPointer(Graphics2D g2, int dx, int dy, String message) {
        if(dx < -9000 && dy < -9000) {
            return;
        }
        if(uiInterface.inplay && cGameLogic.userPlayer() != null) {
            double[] deltas = new double[]{
                    dx - cGameLogic.userPlayer().getInt("coordx")
                            + cGameLogic.userPlayer().getInt("dimw")/2,
                    dy - cGameLogic.userPlayer().getInt("coordy")
                            + cGameLogic.userPlayer().getInt("dimh")/2};
            g2.setColor(new Color(255,100,50,150));
            int[][] polygondims = new int[][]{
                    new int[]{
                            eUtils.scaleInt(dx - cVars.getInt("camx")) - sSettings.height/16,
                            eUtils.scaleInt(dx - cVars.getInt("camx")),
                            eUtils.scaleInt(dx - cVars.getInt("camx")) + sSettings.height/16,
                            eUtils.scaleInt(dx - cVars.getInt("camx"))
                    },
                    new int[]{
                            eUtils.scaleInt(dy - cVars.getInt("camy")),
                            eUtils.scaleInt(dy - cVars.getInt("camy")) - sSettings.height/16,
                            eUtils.scaleInt(dy - cVars.getInt("camy")),
                            eUtils.scaleInt(dy - cVars.getInt("camy")) + sSettings.height/16
                    }
            };
            g2.fillPolygon(polygondims[0],polygondims[1], 4);
            g2.setStroke(new BasicStroke(eUtils.scaleInt(16)));
            g2.setColor(new Color(255,100,50,220));
            g2.drawPolygon(polygondims[0], polygondims[1],4);
            //big font
            String waypointdistance = String.format("%dm",(int)Math.sqrt((deltas[0]*deltas[0])+(deltas[1]*deltas[1])));
            dFonts.setFontNormal(g2);
            dFonts.drawCenteredString(g2,
                    waypointdistance,
                    eUtils.scaleInt(dx - cVars.getInt("camx")),
                    eUtils.scaleInt(dy - cVars.getInt("camy")));
            FontRenderContext frc =
                    new FontRenderContext(null, false, true);
            dFonts.drawCenteredString(g2, message,
                    eUtils.scaleInt(dx - cVars.getInt("camx")),
                    eUtils.scaleInt(dy - cVars.getInt("camy"))
                            -(int)g2.getFont().getStringBounds(waypointdistance,frc).getHeight());
            if(!cVars.isInt("gamemode", cGameMode.VIRUS)
                    && (Math.abs(deltas[0]) > sSettings.width || Math.abs(deltas[1]) > sSettings.height)) {
                double angle = Math.atan2(deltas[1], deltas[0]);
                if (angle < 0)
                    angle += 2 * Math.PI;
                angle += Math.PI / 2;
                AffineTransform backup = g2.getTransform();
                AffineTransform a = g2.getTransform();
                a.rotate(angle,
                        sSettings.width / 2,
                        sSettings.height / 2);
                g2.setTransform(a);
                int[][] arrowpolygon = new int[][]{
                        new int[]{sSettings.width / 2 - sSettings.width / 54,
                                sSettings.width / 2 + sSettings.width / 54, sSettings.width / 2},
                        new int[]{sSettings.height / 12, sSettings.height / 12, 0}
                };
                g2.setColor(new Color(255,100,50,150));
                g2.fillPolygon(arrowpolygon[0], arrowpolygon[1], 3);
                g2.setColor(new Color(255,100,50,220));
                g2.setStroke(new BasicStroke(eUtils.scaleInt(8)));
                g2.drawPolygon(arrowpolygon[0], arrowpolygon[1], 3);
                g2.setTransform(backup);
            }
        }
    }
    public static void drawWaypoints(Graphics2D g2) {
        if(uiInterface.inplay) {
            switch (cVars.getInt("gamemode")) {
                case cGameMode.CAPTURE_THE_FLAG:
                    HashMap unheldflagsmap = eManager.currentMap.scene.getThingMap("PROP_FLAGRED");
                    for(Object id : unheldflagsmap.keySet()) {
                        gPropFlagRed flag = (gPropFlagRed) unheldflagsmap.get(id);
                        if(cGameLogic.userPlayer() != null
                        && !flag.get("str0").equals(cGameLogic.userPlayer().get("id")))
                            dWaypoints.drawNavPointer(g2,flag.getInt("coordx") + flag.getInt("dimw")/2,
                                flag.getInt("coordy") + flag.getInt("dimh")/2, "* GO HERE *");
                    }
                    break;
                case cGameMode.FLAG_MASTER:
                    if(!cVars.isVal("flagmasterid", "")) {
                        if(!cVars.get("flagmasterid").equals(uiInterface.uuid)) {
                            gPlayer p = gScene.getPlayerById(cVars.get("flagmasterid"));
                            dWaypoints.drawNavPointer(g2, p.getInt("coordx") + p.getInt("dimw") / 2,
                                    p.getInt("coordy") + p.getInt("dimh") / 2, "* KILL *");
                        }
                        else {
                            HashMap flagbluemap = eManager.currentMap.scene.getThingMap("PROP_FLAGBLUE");
                            for(Object id : flagbluemap.keySet()) {
                                gPropFlagBlue flag = (gPropFlagBlue) flagbluemap.get(id);
                                dWaypoints.drawNavPointer(g2,flag.getInt("coordx") + flag.getInt("dimw")/2,
                                        flag.getInt("coordy") + flag.getInt("dimh")/2, "* GO HERE *");
                            }
                        }
                    }
                    else {
                        HashMap flagredmap = eManager.currentMap.scene.getThingMap("PROP_FLAGRED");
                        for(Object id : flagredmap.keySet()) {
                            gPropFlagRed flag = (gPropFlagRed) flagredmap.get(id);
                            dWaypoints.drawNavPointer(g2,flag.getInt("coordx") + flag.getInt("dimw")/2,
                                    flag.getInt("coordy") + flag.getInt("dimh")/2, "* GO HERE *");
                        }
                    }
                    break;
                case cGameMode.KING_OF_FLAGS:
                    HashMap flagredmap = eManager.currentMap.scene.getThingMap("PROP_FLAGRED");
                    for(Object id : flagredmap.keySet()) {
                        gPropFlagRed flag = (gPropFlagRed) flagredmap.get(id);
                        if(!flag.isVal("str0", cGameLogic.userPlayer().get("id"))) {
                            dWaypoints.drawNavPointer(g2,flag.getInt("coordx") + flag.getInt("dimw")/2,
                                    flag.getInt("coordy") + flag.getInt("dimh")/2, "* GO HERE *");
                        }
                    }
                    break;
                case cGameMode.VIRUS:
                    if(nServer.instance().clientArgsMap != null && nServer.instance().clientArgsMap.containsKey("server")
                            && nServer.instance().clientArgsMap.get("server").containsKey("state")) {
                        String statestr = nServer.instance().clientArgsMap.get("server").get("state");
                        for (String id : gScene.getPlayerIds()) {
                            gPlayer p = gScene.getPlayerById(id);
                            if (statestr.contains(p.get("id"))) {
                                dWaypoints.drawNavPointer(g2, p.getInt("coordx") + p.getInt("dimw") / 2,
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
                                || (cVars.isInt("gamemode", cGameMode.RACE)
                                && !scorepoint.get("racebotidcheckins").contains(cGameLogic.userPlayer().get("id")))) {
                            //racebots is for server, int0 is for clients
                            dWaypoints.drawNavPointer(g2,
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
}
