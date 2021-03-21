import java.awt.*;
import java.awt.geom.Line2D;
import java.util.HashMap;

public class dTileTops {
//    static Image forbiddenSign = gTextures.getScaledImage(eUtils.getPath("misc/forbidden.png"), 150, 150);
    public static void drawTops(Graphics g) {
        if (cVars.isOne("maploaded")) {
            Graphics2D g2 = (Graphics2D) g;
            /*
             * players extra stuff
             * */
            for (String id : gScene.getPlayerIds()) {
                gPlayer e = gScene.getPlayerById(id);
//            //player hat
                if (e.spriteHat != null && e.get("coordx") != null && e.get("coordy") != null) {
                    g2.drawImage(e.spriteHat,
                            eUtils.scaleInt(e.getInt("coordx") - cVars.getInt("camx")),
                            eUtils.scaleInt(e.getInt("coordy") - cVars.getInt("camy") - 150),
                            null);
                }
                //forbidden sign for spawn protection
//                if (!cGameLogic.isUserPlayer(e) && nServer.instance().clientArgsMap.containsKey(e.get("id"))
//                        && nServer.instance().clientArgsMap.get(e.get("id")).containsKey("spawnprotected")) {
//                    g2.drawImage(forbiddenSign,
//                            eUtils.scaleInt(e.getInt("coordx") - cVars.getInt("camx")),
//                            eUtils.scaleInt(e.getInt("coordy") - cVars.getInt("camy")),
//                            null);
//                }
            }
            HashMap<String, gThing> squareMap = eManager.currentMap.scene.getThingMap("BLOCK_CUBE");
            for(String tag : squareMap.keySet()) {
                gBlockCube block = (gBlockCube) squareMap.get(tag);
                if(block.contains("toph") && block.isZero("backtop")) {
                    dBlockTops.drawBlockTopCube(g2, block);
                }
                if(block.contains("wallh") && block.isOne("frontwall")) {
                    gPlayer userplayer = cGameLogic.userPlayer();
                    if(userplayer != null) {
                        if(block.getInt("coordy") + block.getInt("dimh") - block.getInt("toph")
                                > userplayer.getInt("coordy"))
                            dBlockWalls.drawBlockWallCube(g2, block);
                    }
                    else
                        dBlockWalls.drawBlockWallCube(g2, block);
                }
            }
            squareMap = eManager.currentMap.scene.getThingMap("BLOCK_CORNERUR");
            for(String tag : squareMap.keySet()) {
                gBlockCornerUR block = (gBlockCornerUR) squareMap.get(tag);
                if(block.contains("wallh") && block.isOne("frontwall")) {
                    gPlayer userplayer = cGameLogic.userPlayer();
                    if(userplayer != null) {
                        boolean indepth = block.getInt("coordy") + block.getInt("dimh") - block.getInt("toph")
                                > userplayer.getInt("coordy");
                        int[][] bottomSectionPoints = new int[][]{
                                new int[] {
                                        block.getInt("coordx"),
                                        block.getInt("coordx") + block.getInt("dimw"),
                                        block.getInt("coordx") + block.getInt("dimw"),
                                        block.getInt("coordx")
                                },
                                new int[] {
                                        block.getInt("coordy") + block.getInt("wallh") - block.getInt("toph"),
                                        block.getInt("coordy") + block.getInt("dimh") - block.getInt("toph"),
                                        block.getInt("coordy") + block.getInt("dimh"),
                                        block.getInt("coordy") + block.getInt("wallh")
                                }};
                        int[][] playerPoints = new int[][]{
                                new int[] {
                                        userplayer.getInt("coordx"),
                                        userplayer.getInt("coordx") + userplayer.getInt("dimw"),
                                        userplayer.getInt("coordx") + userplayer.getInt("dimw"),
                                        userplayer.getInt("coordx")
                                },
                                new int[] {
                                        userplayer.getInt("coordy"),
                                        userplayer.getInt("coordy"),
                                        userplayer.getInt("coordy") + userplayer.getInt("dimh"),
                                        userplayer.getInt("coordy") + userplayer.getInt("dimh")
                                }};
                        Line2D bs1 = new Line2D.Float(bottomSectionPoints[0][0], bottomSectionPoints[1][0],
                                bottomSectionPoints[0][1], bottomSectionPoints[1][1]);
                        Line2D bs2 = new Line2D.Float(bottomSectionPoints[0][1], bottomSectionPoints[1][1],
                                bottomSectionPoints[0][2], bottomSectionPoints[1][2]);
                        Line2D bs3 = new Line2D.Float(bottomSectionPoints[0][2], bottomSectionPoints[1][2],
                                bottomSectionPoints[0][3], bottomSectionPoints[1][3]);
                        Line2D bs4 = new Line2D.Float(bottomSectionPoints[0][3], bottomSectionPoints[1][3],
                                bottomSectionPoints[0][0], bottomSectionPoints[1][0]);
                        Line2D ps1 = new Line2D.Float(playerPoints[0][0], playerPoints[1][0],
                                playerPoints[0][1], playerPoints[1][1]);
                        Line2D ps2 = new Line2D.Float(playerPoints[0][1], playerPoints[1][1],
                                playerPoints[0][2], playerPoints[1][2]);
                        Line2D ps3 = new Line2D.Float(playerPoints[0][2], playerPoints[1][2],
                                playerPoints[0][3], playerPoints[1][3]);
                        Line2D ps4 = new Line2D.Float(playerPoints[0][3], playerPoints[1][3],
                                playerPoints[0][0], playerPoints[1][0]);
                        if(!ps2.intersectsLine(bs3) && !ps4.intersectsLine(bs3) && indepth)
                            dBlockWalls.drawBlockWallCornerUR(g2, block);
                    }
                    else
                        dBlockWalls.drawBlockWallCornerUR(g2, block);
//                    dBlockWalls.drawBlockWallCornerUR(g2, block);
                }
                if(block.contains("toph") && block.isZero("backtop")) {
                    dBlockTops.drawBlockTopCornerUR(g2, block);
                }
            }
            squareMap = eManager.currentMap.scene.getThingMap("BLOCK_CORNERUL");
            for(String tag : squareMap.keySet()) {
                gBlockCornerUL block = (gBlockCornerUL) squareMap.get(tag);
                if(block.contains("wallh") && block.isOne("frontwall")) {
                    gPlayer userplayer = cGameLogic.userPlayer();
                    if(userplayer != null) {
                        boolean indepth = block.getInt("coordy") + block.getInt("dimh") - block.getInt("toph")
                                > userplayer.getInt("coordy");
                        int[][] bottomSectionPoints = new int[][]{
                                new int[] {
                                        block.getInt("coordx"),
                                        block.getInt("coordx") + block.getInt("dimw"),
                                        block.getInt("coordx") + block.getInt("dimw"),
                                        block.getInt("coordx")
                                },
                                new int[] {
                                        block.getInt("coordy") + block.getInt("dimh") - block.getInt("toph"),
                                        block.getInt("coordy") + block.getInt("wallh") - block.getInt("toph"),
                                        block.getInt("coordy") + block.getInt("wallh"),
                                        block.getInt("coordy") + block.getInt("dimh")
                                }};
                        int[][] playerPoints = new int[][]{
                                new int[] {
                                        userplayer.getInt("coordx"),
                                        userplayer.getInt("coordx") + userplayer.getInt("dimw"),
                                        userplayer.getInt("coordx") + userplayer.getInt("dimw"),
                                        userplayer.getInt("coordx")
                                },
                                new int[] {
                                        userplayer.getInt("coordy"),
                                        userplayer.getInt("coordy"),
                                        userplayer.getInt("coordy") + userplayer.getInt("dimh"),
                                        userplayer.getInt("coordy") + userplayer.getInt("dimh")
                                }};
                        Line2D bs1 = new Line2D.Float(bottomSectionPoints[0][0], bottomSectionPoints[1][0],
                                bottomSectionPoints[0][1], bottomSectionPoints[1][1]);
                        Line2D bs2 = new Line2D.Float(bottomSectionPoints[0][1], bottomSectionPoints[1][1],
                                bottomSectionPoints[0][2], bottomSectionPoints[1][2]);
                        Line2D bs3 = new Line2D.Float(bottomSectionPoints[0][2], bottomSectionPoints[1][2],
                                bottomSectionPoints[0][3], bottomSectionPoints[1][3]);
                        Line2D bs4 = new Line2D.Float(bottomSectionPoints[0][3], bottomSectionPoints[1][3],
                                bottomSectionPoints[0][0], bottomSectionPoints[1][0]);
                        Line2D ps1 = new Line2D.Float(playerPoints[0][0], playerPoints[1][0],
                                playerPoints[0][1], playerPoints[1][1]);
                        Line2D ps2 = new Line2D.Float(playerPoints[0][1], playerPoints[1][1],
                                playerPoints[0][2], playerPoints[1][2]);
                        Line2D ps3 = new Line2D.Float(playerPoints[0][2], playerPoints[1][2],
                                playerPoints[0][3], playerPoints[1][3]);
                        Line2D ps4 = new Line2D.Float(playerPoints[0][3], playerPoints[1][3],
                                playerPoints[0][0], playerPoints[1][0]);
                        if(!ps2.intersectsLine(bs3) && !ps4.intersectsLine(bs3) && indepth)
                            dBlockWalls.drawBlockWallCornerUL(g2, block);
                    }
                    else
                        dBlockWalls.drawBlockWallCornerUL(g2, block);
//                    dBlockWalls.drawBlockWallCornerUL(g2, block);
                }
                if(block.contains("toph") && block.isZero("backtop")) {
                    dBlockTops.drawBlockTopCornerUL(g2, block);
                }
            }
            squareMap = eManager.currentMap.scene.getThingMap("BLOCK_CORNERLR");
            for(String tag : squareMap.keySet()) {
                gBlockCornerLR block = (gBlockCornerLR) squareMap.get(tag);
                if(block.contains("wallh") && block.isOne("frontwall")) {
                    gPlayer userplayer = cGameLogic.userPlayer();
                    if(userplayer != null) {
                        if(block.getInt("coordy") + block.getInt("dimh") - block.getInt("toph")
                                > userplayer.getInt("coordy"))
                            dBlockWalls.drawBlockWallCornerLR(g2, block);
                    }
                    else
                        dBlockWalls.drawBlockWallCornerLR(g2, block);
                }
                if(block.contains("toph") && block.isZero("backtop")) {
                    dBlockTops.drawBlockTopCornerLR(g2, block);
                }
            }
            squareMap = eManager.currentMap.scene.getThingMap("BLOCK_CORNERLL");
            for(String tag : squareMap.keySet()) {
                gBlockCornerLL block = (gBlockCornerLL) squareMap.get(tag);
                if(block.contains("wallh") && block.isOne("frontwall")) {
                    gPlayer userplayer = cGameLogic.userPlayer();
                    if(userplayer != null) {
                        if(block.getInt("coordy") + block.getInt("dimh") - block.getInt("toph")
                                > userplayer.getInt("coordy"))
                            dBlockWalls.drawBlockWallCornerLL(g2, block);
                    }
                    else
                        dBlockWalls.drawBlockWallCornerLL(g2, block);
                }
                if(block.contains("toph") && block.isZero("backtop")) {
                    dBlockTops.drawBlockTopCornerLL(g2, block);
                }
            }
            //
            // --- NEW ABOVE OLD BELOW ---
            //

            for (gTile t : eManager.currentMap.scene.tiles()) {
                if (t.sprites[0] != null) {
                    g2.drawImage(t.sprites[0],
                            eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                            eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                            null
                    );
                } else {
                    g2.setColor(Color.LIGHT_GRAY);
                    g2.fillRect(eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                            eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                            eUtils.scaleInt(t.getInt("dim0w")),
                            eUtils.scaleInt(t.getInt("dim0h"))
                    );
                }
                if (t.sprites[3] != null) {
                    g2.drawImage(t.sprites[3],
                            eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                            eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dimh") - t.getInt("dim3h") - t.getInt("dim4h")),
                            null);
                } else {
                    g2.setColor(Color.LIGHT_GRAY);
                    g2.fillRect(eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                            eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dimh") - t.getInt("dim3h") - t.getInt("dim4h")),
                            eUtils.scaleInt(t.getInt("dim3w")),
                            eUtils.scaleInt(t.getInt("dim3h"))
                    );
                }
                g2.setStroke(new BasicStroke(eUtils.scaleInt(16)));
                g2.setColor(new Color(0, 0, 0, 255));
                if (sVars.isOne("vfxenableshading")) {
                    dTileTopsShading.drawTileTopShadingPre(g2, t);
                }
                if (t.sprites[5] != null) {
                    g2.drawImage(t.sprites[5],
                            eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                            eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dim0h")),
                            null
                    );
                } else {
                    g2.setColor(Color.LIGHT_GRAY);
                    g2.fillRect(eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                            eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dim0h")),
                            eUtils.scaleInt(t.getInt("dim5w")),
                            eUtils.scaleInt(t.getInt("dim5h"))
                    );
                }
                int d6w = t.getInt("dim6w");
                if (t.sprites[6] != null) {
                    if (d6w > -1)
                        g2.drawImage(t.sprites[6],
                                eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx") + t.getInt("dimw") - t.getInt("dim6w")),
                                eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dim0h")),
                                null
                        );
                } else {
                    if (d6w > -1) {
                        g2.setColor(Color.LIGHT_GRAY);
                        g2.fillRect(eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")
                                        + t.getInt("dimw") - t.getInt("dim6w")),
                                eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")
                                        + t.getInt("dim0h")),
                                eUtils.scaleInt(t.getInt("dim6w")),
                                eUtils.scaleInt(t.getInt("dim6h"))
                        );
                    }
                    else {
                        dTileTopsCorners.drawTileCorners(g2, t);
                    }
                }
                if (sVars.isOne("vfxenableshading")) {
                    dTileTopsShading.drawTileTopShadingPost(g2, t);
                }
            }
            //BLOCK BRIGHTNESS
            dBlockBrightness.drawBlockBrightness(g2);
            //flashlight overlay
            if (cVars.isOne("flashlight")) {
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
                for (gTile t : eManager.currentMap.scene.tiles()) {
                    RadialGradientPaint df = new RadialGradientPaint(new Point(snapX, snapY),
                            eUtils.scaleInt(maxd / 2), new float[]{0f, 1f},
                            new Color[]{new Color(0, 0, 0, 0), new Color(0, 0, 0, 255 - t.getInt("brightness"))}
                    );
                    g2.setPaint(df);
                    g2.fillRect(eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                            eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                            eUtils.scaleInt(t.getInt("dimw")), eUtils.scaleInt(t.getInt("dimh")));
                }
            } else {
                for (gTile t : eManager.currentMap.scene.tiles()) {
                    g2.setColor(new Color(0, 0, 0, 255 - t.getInt("brightness")));
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
            for (Object id : bulletsMap.keySet()) {
                gBullet t = (gBullet) bulletsMap.get(id);
                g2.drawImage(t.sprite, eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                        eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")), null);
            }
            //animations
            dAnimations.drawAnimations(g2);
            //safezone pointer
            dWaypoints.drawWaypoints(g2);
            //popups
            drawPopups(g);
            //player highlight
            drawUserPlayerArrow(g2);
            //playernames
            drawPlayerNames(g, g2);
        }
    }



    public static void drawPopups(Graphics g) {
        HashMap popupsMap = eManager.currentMap.scene.getThingMap("THING_POPUP");
        if(popupsMap.size() > 0)
            dFonts.setFontNormal(g);
        for(Object id : popupsMap.keySet()) {
            gPopup p = (gPopup) popupsMap.get(id);
            g.setColor(Color.BLACK);
            g.drawString(p.get("text").substring(1),
                    eUtils.scaleInt(p.getInt("coordx") - cVars.getInt("camx") + 2),
                    eUtils.scaleInt(p.getInt("coordy") - cVars.getInt("camy") + 2));
            g.setColor(p.get("text").charAt(0) == '+' ?
                    new Color(Integer.parseInt(xCon.ex("fontcolorbonus").split(",")[0]),
                            Integer.parseInt(xCon.ex("fontcolorbonus").split(",")[1]),
                            Integer.parseInt(xCon.ex("fontcolorbonus").split(",")[2]),
                            Integer.parseInt(xCon.ex("fontcolorbonus").split(",")[3]))
                    : new Color(Integer.parseInt(xCon.ex("fontcoloralert").split(",")[0]),
                    Integer.parseInt(xCon.ex("fontcoloralert").split(",")[1]),
                    Integer.parseInt(xCon.ex("fontcoloralert").split(",")[2]),
                    Integer.parseInt(xCon.ex("fontcoloralert").split(",")[3])));
            g.drawString(p.get("text").substring(1),
                    eUtils.scaleInt(p.getInt("coordx") - cVars.getInt("camx")),
                    eUtils.scaleInt(p.getInt("coordy") - cVars.getInt("camy")));
        }
    }

    public static void drawUserPlayerArrow(Graphics2D g2) {
        if(sVars.isOne("playerarrow")) {
            gPlayer userPlayer = cGameLogic.userPlayer();
            if(userPlayer == null || (sSettings.show_mapmaker_ui && !uiInterface.inplay))
                return;
            int coordx = eUtils.scaleInt(Integer.parseInt(userPlayer.get("coordx")) - cVars.getInt("camx"));
            int coordy = eUtils.scaleInt(Integer.parseInt(userPlayer.get("coordy")) - cVars.getInt("camy")
                    - 200);
            int[][] polygonBase = new int[][]{
                    new int[]{0,2,1},
                    new int[]{0,0,1}
            };
            int polygonSize = sSettings.width/32;
            int[][] polygon = new int[][]{
                    new int[]{coordx + polygonBase[0][0]*polygonSize, coordx + polygonBase[0][1]*polygonSize,
                            coordx + polygonBase[0][2]*polygonSize},
                    new int[]{coordy + polygonBase[1][0]*polygonSize, coordy + polygonBase[1][1]*polygonSize,
                            coordy + polygonBase[1][2]*polygonSize}
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
        for(String id : nServer.instance().clientArgsMap.keySet()) {
            HashMap<String, String> clientMap = nServer.instance().clientArgsMap.get(id);
            gPlayer p = gScene.getPlayerById(id);
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
            dFonts.drawCenteredString(g, name,
                    eUtils.scaleInt(coordx + p.getInt("dimw")/2), eUtils.scaleInt(coordy));
            //draw flashlight glow
            if(sVars.isOne("vfxenableflares") && p.isOne("flashlight")) {
                if (!p.containsFields(new String[]{"coordx", "coordy", "dimw", "dimh", "flashlight"}))
                    continue;
                int x = eUtils.scaleInt(p.getInt("coordx") - cVars.getInt("camx")
                        - p.getInt("dimw") / 4);
                int y = eUtils.scaleInt(p.getInt("coordy") - cVars.getInt("camy")
                        - p.getInt("dimh") / 4);
                int w = eUtils.scaleInt(3 * p.getInt("dimw") / 2);
                int h = eUtils.scaleInt(3 * p.getInt("dimh") / 2);
                dFlares.drawFlare(g2, x, y, w, h, 1, new int[]{255, 255, 255, 255}, new int[4]);
            }
        }
    }
}
