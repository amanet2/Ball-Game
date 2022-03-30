import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.HashMap;

public class dWaypoints {
    public static void drawNavPointer(Graphics2D g2, int dx, int dy, String message) {
        if(uiInterface.inplay && cClientLogic.getUserPlayer() != null) {
            double[] deltas = new double[]{
                    dx - cClientLogic.getUserPlayer().getInt("coordx")
                            + cClientLogic.getUserPlayer().getDouble("dimw")/2,
                    dy - cClientLogic.getUserPlayer().getInt("coordy")
                            + cClientLogic.getUserPlayer().getDouble("dimh")/2
            };
            g2.setColor(gColors.getFontColorFromName("waypoint1"));
            int[][] polygondims = new int[][]{
                    new int[]{
                            dx - eUtils.unscaleInt(sSettings.height/16),
                            dx,
                            dx + eUtils.unscaleInt(sSettings.height/16),
                            dx
                    },
                    new int[]{
                            dy,
                            dy - eUtils.unscaleInt(sSettings.height/16),
                            dy,
                            dy + eUtils.unscaleInt(sSettings.height/16)
                    }
            };
            g2.fillPolygon(polygondims[0],polygondims[1], 4);
            g2.setStroke(dFonts.thickStroke);
            g2.setColor(gColors.getFontColorFromName("waypoint2"));
            g2.drawPolygon(polygondims[0], polygondims[1],4);
            //big font
            dFonts.setFontGNormal(g2);
            dFonts.drawCenteredString(g2, message, dx, dy);
            if(!cGameLogic.isVirus()) {
                AffineTransform backup = g2.getTransform();
                g2.translate(gCamera.getX(), gCamera.getY());
                double angle = Math.atan2(deltas[1], deltas[0]);
                if (angle < 0)
                    angle += 2 * Math.PI;
                angle += Math.PI / 2;
                AffineTransform a = g2.getTransform();
                a.rotate(angle,
                        eUtils.unscaleInt((int)((double)sSettings.width / 2)),
                        eUtils.unscaleInt((int)((double)sSettings.height / 2))
                );
                g2.setTransform(a);
                int[][] arrowpolygon = new int[][]{
                        new int[]{
                                eUtils.unscaleInt(sSettings.width / 2 - sSettings.width / 54),
                                eUtils.unscaleInt(sSettings.width / 2 + sSettings.width / 54),
                                eUtils.unscaleInt(sSettings.width / 2)
                        },
                        new int[]{
                                eUtils.unscaleInt(sSettings.height / 12),
                                eUtils.unscaleInt(sSettings.height / 12),
                                eUtils.unscaleInt(0)
                        }
                };
                g2.setColor(gColors.getFontColorFromName("waypoint1"));
                g2.fillPolygon(arrowpolygon[0], arrowpolygon[1], 3);
                g2.setColor(gColors.getFontColorFromName("waypoint2"));
                g2.setStroke(dFonts.waypointStroke);
                g2.drawPolygon(arrowpolygon[0], arrowpolygon[1], 3);
                g2.translate(-gCamera.getX(), -gCamera.getY());
                g2.setTransform(backup);
            }
        }
    }
    public static void drawWaypoints(Graphics2D g2, gScene scene) {
        if(uiInterface.inplay && nClient.instance().serverArgsMap.containsKey("server")) {
            if(nClient.instance().serverArgsMap.get("server").containsKey("flagmasterid")) {
                if(!nClient.instance().serverArgsMap.get("server").get("flagmasterid").equals(uiInterface.uuid)) {
                    gPlayer p = cClientLogic.getPlayerById(
                            nClient.instance().serverArgsMap.get("server").get("flagmasterid"));
                    if(p == null)
                        return;
                    dWaypoints.drawNavPointer(g2, p.getInt("coordx") + p.getInt("dimw") / 2,
                            p.getInt("coordy") + p.getInt("dimh") / 2, "KILL");
                }
            }
            else {
                HashMap<String, gThing> flagmap = scene.getThingMap("ITEM_FLAG");
                for(Object id : flagmap.keySet()) {
                    gItemFlag flag = (gItemFlag) flagmap.get(id);
                    dWaypoints.drawNavPointer(g2,flag.getInt("coordx") + flag.getInt("dimw")/2,
                            flag.getInt("coordy") + flag.getInt("dimh")/2, "PICK UP");
                }
            }

            if(nClient.instance().serverArgsMap.get("server").containsKey("virusids")) {
                String statestr = nClient.instance().serverArgsMap.get("server").get("virusids");
                for (String id : cClientLogic.getPlayerIds()) {
                    gPlayer p = cClientLogic.getPlayerById(id);
                    if (statestr.contains(p.get("id"))) {
                        dWaypoints.drawNavPointer(g2, p.getInt("coordx") + p.getInt("dimw") / 2,
                                p.getInt("coordy") + p.getInt("dimh") / 2, "INFECTED");
                    }
                }
            }
        }
    }
}
