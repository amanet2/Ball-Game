import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class dTileTops {
    public static void drawTops(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        for(gTile t : eManager.currentMap.scene.tiles()) {
            if(t.sprites[0] != null) {
                g2.drawImage(t.sprites[0],
                        eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                        eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                        null
                );
            }
            else {
                g2.setColor(Color.LIGHT_GRAY);
                g2.fillRect(eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                        eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                        eUtils.scaleInt(t.getInt("dim0w")),
                        eUtils.scaleInt(t.getInt("dim0h"))
                );
            }
            if(t.sprites[3] != null) {
                g2.drawImage(t.sprites[3],
                        eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                        eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dimh") - t.getInt("dim3h") - t.getInt("dim4h")),
                        null);
            }
            else {
                g2.setColor(Color.LIGHT_GRAY);
                g2.fillRect(eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                        eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dimh") - t.getInt("dim3h") - t.getInt("dim4h")),
                        eUtils.scaleInt(t.getInt("dim3w")),
                        eUtils.scaleInt(t.getInt("dim3h"))
                );
            }
            g2.setStroke(new BasicStroke(eUtils.scaleInt(16)));
            g2.setColor(new Color(0, 0, 0, 255));
            if(sVars.isOne("vfxenableshading")) {
                dTileTopsShading.drawTileTopShading(g2, t);
            }
            if(t.sprites[5] != null) {
                g2.drawImage(t.sprites[5],
                        eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                        eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dim0h")),
                        null
                );
            }
            else {
                g2.setColor(Color.LIGHT_GRAY);
                g2.fillRect(eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                        eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dim0h")),
                        eUtils.scaleInt(t.getInt("dim5w")),
                        eUtils.scaleInt(t.getInt("dim5h"))
                );
            }
            int d6w = t.getInt("dim6w");
            if(t.sprites[6] != null) {
                if(d6w > -1)
                    g2.drawImage(t.sprites[6],
                        eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx") + t.getInt("dimw") - t.getInt("dim6w")),
                        eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dim0h")),
                        null
                    );
            }
            else {
                dTileTopsCorners.drawTileCorners(g2, t);
            }
            if(sVars.isOne("vfxenableshading")) {
                dTileTopsShading.drawTileTopShadingPost(g2, t);
            }
        }
        /*
        * players extra stuff
        * */
        for(String id : gScene.getPlayerIds()) {
            gPlayer e = gScene.getPlayerById(id);
            //player hat
            g2.drawImage(e.spriteHat,
                    eUtils.scaleInt(e.getInt("coordx") - cVars.getInt("camx")),
                    eUtils.scaleInt(e.getInt("coordy") - cVars.getInt("camy") - 150),
                    null);
            //forbidden sign for spawn protection
            if(nServer.clientArgsMap.containsKey(e.get("id"))
                    && nServer.clientArgsMap.get(e.get("id")).containsKey("spawnprotected")
                    && (!cGameLogic.isUserPlayer(e) || cGameLogic.drawSpawnProtection())) {
                g2.drawImage(gTextures.getScaledImage(eUtils.getPath("misc/forbidden.png"), 150,150),
                        eUtils.scaleInt(e.getInt("coordx") - cVars.getInt("camx")),
                        eUtils.scaleInt(e.getInt("coordy") - cVars.getInt("camy")),
                        null);
            }
        }
        //flashlight overlay
        if(cVars.isOne("flashlight")) {
            int maxd = 900;
            int aimerx = eUtils.unscaleInt(cScripts.getMouseCoordinates()[0]);
            int aimery = eUtils.unscaleInt(cScripts.getMouseCoordinates()[1]);
            int cx = eUtils.unscaleInt(cVars.getInt("camx"));
            int cy = eUtils.unscaleInt(cVars.getInt("camy"));
            int snapX = aimerx + cx;
            int snapY = aimery + cy;
            snapX -= eUtils.unscaleInt(cVars.getInt("camx"));
            snapY -= eUtils.unscaleInt(cVars.getInt("camy"));
            snapX = eUtils.scaleInt(snapX);
            snapY = eUtils.scaleInt(snapY);
            for(gTile t : eManager.currentMap.scene.tiles()) {
                RadialGradientPaint df = new RadialGradientPaint(new Point(snapX, snapY),
                        eUtils.scaleInt(maxd/2), new float[]{0f, 1f},
                        new Color[]{new Color(0,0,0,0), new Color(0,0,0,255-t.getInt("brightness"))}
                );
                g2.setPaint(df);
                g2.fillRect(eUtils.scaleInt(t.getInt("coordx")-cVars.getInt("camx")),
                        eUtils.scaleInt(t.getInt("coordy")-cVars.getInt("camy")),
                        eUtils.scaleInt(t.getInt("dimw")), eUtils.scaleInt(t.getInt("dimh")));
            }
        }
        else {
            for(gTile t : eManager.currentMap.scene.tiles()) {
                g2.setColor(new Color(0,0,0,255-t.getInt("brightness")));
                g2.fillRect(eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                        eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                        eUtils.scaleInt(t.getInt("dimw")), eUtils.scaleInt(t.getInt("dimh"))
                );
            }
        }
        //flares
        dFlares.drawSceneFlares(g2);
        //bullets
        HashMap bulletsMap = eManager.currentMap.scene.getThingMap("THING_BULLET");
        for(Object id : bulletsMap.keySet()) {
            gBullet t = (gBullet) bulletsMap.get(id);
            g2.drawImage(t.sprite, eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                    eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")), null);
        }
        //animations
        dAnimations.drawAnimations(g2);
        //mapmaker indicators
        dMapmakerOverlay.drawSelectionBoxes(g2);
        //safezone pointer
        dWaypoints.drawWaypoints(g2);
        //popups
        drawPopups(g);
        //player highlight
        drawUserPlayerArrow(g2);
        //playernames
        drawPlayerNames(g, g2);
    }



    public static void drawPopups(Graphics g) {
        HashMap popupsMap = eManager.currentMap.scene.getThingMap("THING_POPUP");
        for(Object id : popupsMap.keySet()) {
            gPopup p = (gPopup) popupsMap.get(id);
            g.setColor(p.get("text").charAt(0) == '+' ?
                    new Color(Integer.parseInt(xCon.ex("fontcolorbonus").split(",")[0]),
                            Integer.parseInt(xCon.ex("fontcolorbonus").split(",")[1]),
                            Integer.parseInt(xCon.ex("fontcolorbonus").split(",")[2]),
                            Integer.parseInt(xCon.ex("fontcolorbonus").split(",")[3]))
                    : new Color(Integer.parseInt(xCon.ex("fontcoloralert").split(",")[0]),
                    Integer.parseInt(xCon.ex("fontcoloralert").split(",")[1]),
                    Integer.parseInt(xCon.ex("fontcoloralert").split(",")[2]),
                    Integer.parseInt(xCon.ex("fontcoloralert").split(",")[3])));
            g.drawString(p.get("text"),
                    eUtils.scaleInt(p.getInt("coordx") - cVars.getInt("camx")),
                    eUtils.scaleInt(p.getInt("coordy") - cVars.getInt("camy")));
        }
    }

    public static void drawUserPlayerArrow(Graphics2D g2) {
        if(sVars.isOne("playerarrow") && eManager.currentMap.scene.playersMap().size() > 0) {
            gPlayer userPlayer = cGameLogic.userPlayer();
            int[][] polygonBase = new int[][]{
                    new int[]{0,2,1},
                    new int[]{0,0,1}
            };
            int polygonSize = sSettings.width/32;
            int[][] polygon = new int[][]{
                    new int[]{
                            eUtils.scaleInt(userPlayer.getInt("coordx")
                                    - cVars.getInt("camx")) + polygonBase[0][0]*polygonSize,
                            eUtils.scaleInt(userPlayer.getInt("coordx")
                                    - cVars.getInt("camx")) + polygonBase[0][1]*polygonSize,
                            eUtils.scaleInt(userPlayer.getInt("coordx")
                                    - cVars.getInt("camx")) + polygonBase[0][2]*polygonSize},
                    new int[]{
                            eUtils.scaleInt(userPlayer.getInt("coordy")
                                    - cVars.getInt("camy")-200) + polygonBase[1][0]*polygonSize,
                            eUtils.scaleInt(userPlayer.getInt("coordy")
                                    - cVars.getInt("camy")-200) + polygonBase[1][1]*polygonSize,
                            eUtils.scaleInt(userPlayer.getInt("coordy")
                                    - cVars.getInt("camy")-200) + polygonBase[1][2]*polygonSize}
            };
            g2.setStroke(new BasicStroke(eUtils.scaleInt(16)));
            Polygon pg = new Polygon(polygon[0], polygon[1], polygon[0].length);
            g2.setColor(new Color(0,150,50, 255));
            g2.drawPolygon(pg);
            g2.setColor(new Color(0,200,0, 100));
            g2.fillPolygon(pg);
        }
    }

    public static void drawPlayerNames(Graphics g, Graphics2D g2) {
        for(String id : gScene.getPlayerIds()) {
            gPlayer p = gScene.getPlayerById(id);
            dFonts.setFontNormal(g);
            g.drawString(p.get("name"), eUtils.scaleInt(p.getInt("coordx") - cVars.getInt("camx")),
                    eUtils.scaleInt(p.getInt("coordy") - cVars.getInt("camy")));
            int x = eUtils.scaleInt(p.getInt("coordx")-cVars.getInt("camx")
                    - p.getInt("dimw")/4);
            int y = eUtils.scaleInt(p.getInt("coordy")-cVars.getInt("camy") - p.getInt("dimh")/4);
            int w = eUtils.scaleInt(3*p.getInt("dimw")/2);
            int h = eUtils.scaleInt(3*p.getInt("dimh")/2);
            if(sVars.isOne("vfxenableflares") && p.isOne("flashlight"))
                dFlares.drawFlare(g2,x,y,w,h,1,new int[]{255,255,255,255},new int[4]);
        }
    }
}
