import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;

public class dTileTops {
    public static void drawMapmakerOverlay(Graphics2D g2, gScene scene) {
        //draw the grid OVER everything
        if(sVars.isOne("drawmapmakergrid")) {
            g2.setColor(new Color(255,255,0,125));
            g2.setStroke(dFonts.defaultStroke);
            for(int i = -12000; i <= 12000; i+=300) {
                g2.drawLine(eUtils.scaleInt(-12000 - cVars.getInt("camx")),
                        eUtils.scaleInt(i - cVars.getInt("camy")),
                        eUtils.scaleInt(12000 - cVars.getInt("camx")),
                        eUtils.scaleInt(i - cVars.getInt("camy")));
                g2.drawLine(eUtils.scaleInt(i - cVars.getInt("camx")),
                        eUtils.scaleInt(-12000 - cVars.getInt("camy")),
                        eUtils.scaleInt(i - cVars.getInt("camx")),
                        eUtils.scaleInt(12000 - cVars.getInt("camy")));
            }
        }
        //draw hitboxes
        if(sVars.isOne("drawhitboxes")) {
            g2.setColor(Color.RED);
            for(String id : scene.getThingMap("THING_COLLISION").keySet()) {
                gCollision collision =
                        (gCollision) scene.getThingMap("THING_COLLISION").get(id);
                int[] transformedXarr = new int[collision.xarr.length];
                int[] transformedYarr = new int[collision.yarr.length];
                for(int i = 0; i < collision.xarr.length; i++) {
                    transformedXarr[i] = eUtils.scaleInt(collision.xarr[i] - cVars.getInt("camx"));
                }
                for(int i = 0; i < collision.yarr.length; i++) {
                    transformedYarr[i] = eUtils.scaleInt(collision.yarr[i] - cVars.getInt("camy"));
                }
                g2.drawPolygon(new Polygon(transformedXarr, transformedYarr, collision.npoints));

            }
            for(String id : scene.getThingMap("THING_PLAYER").keySet()) {
                gThing player = scene.getThingMap("THING_PLAYER").get(id);
                g2.setColor(Color.RED);
                if(!player.containsFields(new String[]{"coordx", "coordy", "dimw", "dimh"}))
                    continue;
                int x1 = player.getInt("coordx");
                int y1 = player.getInt("coordy");
                g2.drawRect(
                        eUtils.scaleInt(x1 - cVars.getInt("camx")),
                        eUtils.scaleInt(y1 - cVars.getInt("camy")),
                        eUtils.scaleInt(player.getInt("dimw")),
                        eUtils.scaleInt(player.getInt("dimh"))
                );
            }
        }
    }

    public static void drawBullets(Graphics2D g2, gScene scene) {
        HashMap bulletsMap = scene.getThingMap("THING_BULLET");
        for (Object id : bulletsMap.keySet()) {
            gBullet t = (gBullet) bulletsMap.get(id);
            g2.drawImage(t.sprite, eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                    eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")), null);
        }
    }

    public static void drawPopups(Graphics g, gScene scene) {
        HashMap popupsMap = scene.getThingMap("THING_POPUP");
        if(popupsMap.size() > 0)
            dFonts.setFontNormal(g);
        for(Object id : popupsMap.keySet()) {
            gPopup p = (gPopup) popupsMap.get(id);
            g.setColor(Color.BLACK);
            g.drawString(p.get("text"),
                    eUtils.scaleInt(p.getInt("coordx") - cVars.getInt("camx") + 2),
                    eUtils.scaleInt(p.getInt("coordy") - cVars.getInt("camy") + 2));
            dFonts.setFontColorNormal(g);
            if(p.get("text").charAt(0) == '+')
                dFonts.setFontColorBonus(g);
            else if(p.get("text").charAt(0) == '-')
                dFonts.setFontColorAlert(g);
            g.drawString(p.get("text"),
                    eUtils.scaleInt(p.getInt("coordx") - cVars.getInt("camx")),
                    eUtils.scaleInt(p.getInt("coordy") - cVars.getInt("camy")));
        }
    }

    public static void drawUserPlayerArrow(Graphics2D g2) {
        if(sVars.isOne("drawplayerarrow")) {
            gPlayer userPlayer = cClientLogic.getUserPlayer();
            if(userPlayer == null || (sSettings.show_mapmaker_ui && !uiInterface.inplay))
                return;
            int midx = eUtils.scaleInt(userPlayer.getInt("coordx") + userPlayer.getInt("dimw")/2
                    - cVars.getInt("camx"));
            int coordy = eUtils.scaleInt(userPlayer.getInt("coordy") - cVars.getInt("camy")
                    - 200);
            int[][] polygonBase = new int[][]{
                    new int[]{1,1,1},
                    new int[]{0,0,1}
            };
            int polygonSize = sSettings.width/32;
            int[][] polygon = new int[][]{
                    new int[]{midx - polygonBase[0][0]*polygonSize,
                            midx + polygonBase[0][1]*polygonSize,
                            midx
                    },
                    new int[]{coordy + polygonBase[1][0]*polygonSize, coordy + polygonBase[1][1]*polygonSize,
                            coordy + polygonBase[1][2]*polygonSize}
            };
            g2.setStroke(dFonts.thickStroke);
            Polygon pg = new Polygon(polygon[0], polygon[1], polygon[0].length);
            g2.setColor(new Color(0,150,50, 255));
            g2.drawPolygon(pg);
            g2.setColor(new Color(0,200,0, 100));
            g2.fillPolygon(pg);
        }
    }

    public static void drawPlayerNames(Graphics g) {
        for(String id : nClient.instance().serverArgsMap.keySet()) {
            HashMap<String, String> clientMap = nClient.instance().serverArgsMap.get(id);
            gPlayer p = cClientLogic.getPlayerById(id);
            if(p == null || clientMap == null)
                continue;
            if(!p.containsFields(new String[]{"coordx", "coordy"}))
                continue;
            if(!eUtils.containsFields(clientMap, new String[]{"name"}))
                continue;
            dFonts.setFontNormal(g);
            String name = clientMap.get("name");
            int coordx = p.getInt("coordx") - cVars.getInt("camx");
            int coordy = p.getInt("coordy") - cVars.getInt("camy");
//            dFonts.drawCenteredString(g, name,
//                    eUtils.scaleInt(coordx + p.getInt("dimw")/2), eUtils.scaleInt(coordy));
            Color color = Color.white;
            String ck = nClient.instance().serverArgsMap.get(id).get("color");
            if(dHUD.playerHudColors.containsKey(ck))
                color = dHUD.playerHudColors.get(ck);
            dFonts.drawPlayerNameHud(g, name,
                    eUtils.scaleInt(coordx + p.getInt("dimw")/2), eUtils.scaleInt(coordy), color);
            //SAVE THIS: draw flashlight/spawnprotection glow
//            if(sVars.isOne("vfxenableflares") && p.isOne("flashlight")) {
//                if (!p.containsFields(new String[]{"coordx", "coordy", "dimw", "dimh", "flashlight"}))
//                    continue;
//                int x = eUtils.scaleInt(p.getInt("coordx") - cVars.getInt("camx")
//                        - p.getInt("dimw") / 4);
//                int y = eUtils.scaleInt(p.getInt("coordy") - cVars.getInt("camy")
//                        - p.getInt("dimh") / 4);
//                int w = eUtils.scaleInt(3 * p.getInt("dimw") / 2);
//                int h = eUtils.scaleInt(3 * p.getInt("dimh") / 2);
//                dFlares.drawFlare(g2, x, y, w, h, 1, new int[]{255, 255, 255, 255}, new int[4]);
//            }
        }
    }
}
