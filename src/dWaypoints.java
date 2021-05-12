import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.HashMap;

public class dWaypoints {
    public static void drawNavPointer(Graphics2D g2, int dx, int dy, String message) {
        if(uiInterface.inplay && cClientLogic.getUserPlayer() != null) {
            double[] deltas = new double[]{
                    dx - cClientLogic.getUserPlayer().getInt("coordx")
                            + cClientLogic.getUserPlayer().getInt("dimw")/2,
                    dy - cClientLogic.getUserPlayer().getInt("coordy")
                            + cClientLogic.getUserPlayer().getInt("dimh")/2};
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
            g2.setStroke(dFonts.thickStroke);
            g2.setColor(new Color(255,100,50,220));
            g2.drawPolygon(polygondims[0], polygondims[1],4);
            //big font
            dFonts.setFontNormal(g2);
            dFonts.drawCenteredString(g2, message,
                    eUtils.scaleInt(dx - cVars.getInt("camx")),
                    eUtils.scaleInt(dy - cVars.getInt("camy")));
            if(!cVars.isInt("gamemode", cGameLogic.VIRUS)
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
                g2.setStroke(dFonts.waypointStroke);
                g2.drawPolygon(arrowpolygon[0], arrowpolygon[1], 3);
                g2.setTransform(backup);
            }
        }
    }
    public static void drawWaypoints(Graphics2D g2, gScene scene) {
        if(uiInterface.inplay && nClient.instance().serverArgsMap.containsKey("server")
        && nClient.instance().serverArgsMap.get("server").containsKey("state")) {
            switch (cVars.getInt("gamemode")) {
                case cGameLogic.FLAG_MASTER:
                    if(nClient.instance().serverArgsMap.get("server").get("state").length() > 0) {
                        if(!nClient.instance().serverArgsMap.get("server").get("state").equals(uiInterface.uuid)) {
                            gPlayer p = cClientLogic.getPlayerById(
                                    nClient.instance().serverArgsMap.get("server").get("state"));
                            if(p == null)
                                break;
                            dWaypoints.drawNavPointer(g2, p.getInt("coordx") + p.getInt("dimw") / 2,
                                    p.getInt("coordy") + p.getInt("dimh") / 2, "* KILL *");
                        }
                    }
                    else {
                        HashMap flagmap = scene.getThingMap("ITEM_FLAG");
                        for(Object id : flagmap.keySet()) {
                            gItemFlag flag = (gItemFlag) flagmap.get(id);
                            dWaypoints.drawNavPointer(g2,flag.getInt("coordx") + flag.getInt("dimw")/2,
                                    flag.getInt("coordy") + flag.getInt("dimh")/2, "* GO HERE *");
                        }
                    }
                    break;
                case cGameLogic.VIRUS:
                    if(nClient.instance().serverArgsMap != null && nClient.instance().serverArgsMap.containsKey("server")
                            && nClient.instance().serverArgsMap.get("server").containsKey("state")) {
                        String statestr = nClient.instance().serverArgsMap.get("server").get("state");
                        for (String id : cClientLogic.getPlayerIds()) {
                            gPlayer p = cClientLogic.getPlayerById(id);
                            if (statestr.contains(p.get("id"))) {
                                dWaypoints.drawNavPointer(g2, p.getInt("coordx") + p.getInt("dimw") / 2,
                                        p.getInt("coordy") + p.getInt("dimh") / 2, "* INFECTED *");
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
