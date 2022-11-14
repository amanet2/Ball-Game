import java.awt.Graphics2D;
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
            g2.setColor(gColors.instance().getColorFromName("clrp_" + cClientVars.instance().get("playercolor")));
            int[][] polygondims = new int[][]{
                    new int[]{
                            dx - eUtils.unscaleInt(sSettings.height / 16),
                            dx,
                            dx + eUtils.unscaleInt(sSettings.height / 16),
                            dx
                    },
                    new int[]{
                            dy,
                            dy - eUtils.unscaleInt(sSettings.height / 16),
                            dy,
                            dy + eUtils.unscaleInt(sSettings.height / 16)
                    }
            };
            g2.fillPolygon(polygondims[0], polygondims[1], 4);
            g2.setStroke(dFonts.thickStroke);
            dFonts.setFontColor(g2, "clrf_normaltransparent");
            g2.drawPolygon(polygondims[0], polygondims[1], 4);
            //big font
            dFonts.setFontGNormal(g2);
            dFonts.drawCenteredString(g2, message, dx, dy);
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
            g2.setColor(gColors.instance().getColorFromName("clrp_" + cClientVars.instance().get("playercolor")));
            g2.fillPolygon(arrowpolygon[0], arrowpolygon[1], 3);
            dFonts.setFontColor(g2, "clrf_normaltransparent");
            g2.setStroke(dFonts.waypointStroke);
            g2.drawPolygon(arrowpolygon[0], arrowpolygon[1], 3);
            g2.translate(-gCamera.getX(), -gCamera.getY());
            g2.setTransform(backup);
        }
    }
    public static void drawWaypoints(Graphics2D g2, gScene scene) {
        if(uiInterface.inplay) {
            // players
            for(String id : nClient.instance().clientStateMap.keys()) {
                if(!id.equalsIgnoreCase(uiInterface.uuid)
                        && nClient.instance().clientStateMap.get(id).get("waypoint").equals("1")) {
                    gPlayer p = cClientLogic.getPlayerById(id);
                    if(p != null)
                        drawNavPointer(g2, p.getInt("coordx") + p.getInt("dimw") / 2,
                                p.getInt("coordy") + p.getInt("dimh") / 2, "ROCK");
                }
            }
            // items
            HashMap<String, gThing> itemMap = scene.getThingMap("THING_ITEM");
            for(Object id : itemMap.keySet()) {
                gThing item = itemMap.get(id);
                if(item.contains("waypoint") && item.isVal("waypoint", "1"))
                    drawNavPointer(g2,item.getInt("coordx") + item.getInt("dimw")/2,
                            item.getInt("coordy") + item.getInt("dimh")/2,
                            item.get("type").replace("ITEM_",""));
            }
        }
    }
}
