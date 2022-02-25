import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;

public class dTileTops {
//    static Image forbiddenSign = gTextures.getScaledImage(eUtils.getPath("misc/forbidden.png"), 150, 150);
    public static void drawTops(Graphics g, gScene scene) {
        Graphics2D g2 = (Graphics2D) g;
        /*
         * players extra stuff
         * */
//        for (String id : cServerLogic.getPlayerIds()) {
//            gPlayer e = cServerLogic.getPlayerById(id);
////            //player hat
//            if (e.spriteHat != null && e.get("coordx") != null && e.get("coordy") != null) {
//                g2.drawImage(e.spriteHat,
//                        eUtils.scaleInt(e.getInt("coordx") - cVars.getInt("camx")),
//                        eUtils.scaleInt(e.getInt("coordy") - cVars.getInt("camy") - 150),
//                        null);
//            }
        HashMap<String, gThing> squareMap;
//        squareMap = scene.getThingMap("BLOCK_CORNERUR");
//        for(String tag : squareMap.keySet()) {
//            gBlockCornerUR block = (gBlockCornerUR) squareMap.get(tag);
//            if(block.contains("wallh") && block.isOne("frontwall")) {
//                gPlayer userplayer = cClientLogic.getUserPlayer();
//                if(userplayer != null) {
//                    boolean indepth = block.getInt("coordy") + block.getInt("dimh") - block.getInt("toph")
//                            > userplayer.getInt("coordy");
//                    int[][] bottomSectionPoints = new int[][]{
//                            new int[] {
//                                    block.getInt("coordx"),
//                                    block.getInt("coordx") + block.getInt("dimw"),
//                                    block.getInt("coordx") + block.getInt("dimw"),
//                                    block.getInt("coordx")
//                            },
//                            new int[] {
//                                    block.getInt("coordy") + block.getInt("wallh") - block.getInt("toph"),
//                                    block.getInt("coordy") + block.getInt("dimh") - block.getInt("toph"),
//                                    block.getInt("coordy") + block.getInt("dimh"),
//                                    block.getInt("coordy") + block.getInt("wallh")
//                            }};
//                    int[][] playerPoints = new int[][]{
//                            new int[] {
//                                    userplayer.getInt("coordx"),
//                                    userplayer.getInt("coordx") + userplayer.getInt("dimw"),
//                                    userplayer.getInt("coordx") + userplayer.getInt("dimw"),
//                                    userplayer.getInt("coordx")
//                            },
//                            new int[] {
//                                    userplayer.getInt("coordy"),
//                                    userplayer.getInt("coordy"),
//                                    userplayer.getInt("coordy") + userplayer.getInt("dimh"),
//                                    userplayer.getInt("coordy") + userplayer.getInt("dimh")
//                            }};
//                    Line2D bs1 = new Line2D.Float(bottomSectionPoints[0][0], bottomSectionPoints[1][0],
//                            bottomSectionPoints[0][1], bottomSectionPoints[1][1]);
//                    Line2D bs2 = new Line2D.Float(bottomSectionPoints[0][1], bottomSectionPoints[1][1],
//                            bottomSectionPoints[0][2], bottomSectionPoints[1][2]);
//                    Line2D bs3 = new Line2D.Float(bottomSectionPoints[0][2], bottomSectionPoints[1][2],
//                            bottomSectionPoints[0][3], bottomSectionPoints[1][3]);
//                    Line2D bs4 = new Line2D.Float(bottomSectionPoints[0][3], bottomSectionPoints[1][3],
//                            bottomSectionPoints[0][0], bottomSectionPoints[1][0]);
//                    Line2D ps1 = new Line2D.Float(playerPoints[0][0], playerPoints[1][0],
//                            playerPoints[0][1], playerPoints[1][1]);
//                    Line2D ps2 = new Line2D.Float(playerPoints[0][1], playerPoints[1][1],
//                            playerPoints[0][2], playerPoints[1][2]);
//                    Line2D ps3 = new Line2D.Float(playerPoints[0][2], playerPoints[1][2],
//                            playerPoints[0][3], playerPoints[1][3]);
//                    Line2D ps4 = new Line2D.Float(playerPoints[0][3], playerPoints[1][3],
//                            playerPoints[0][0], playerPoints[1][0]);
//                    if(!ps2.intersectsLine(bs3) && !ps4.intersectsLine(bs3) && indepth)
//                        dBlockWalls.drawBlockWallCornerUR(g2, block);
//                }
//                else
//                    dBlockWalls.drawBlockWallCornerUR(g2, block);
//            }
//            if(block.contains("toph") && block.isZero("backtop")) {
//                dBlockTops.drawBlockTopCornerUR(g2, block);
//            }
//        }
//        squareMap = scene.getThingMap("BLOCK_CORNERUL");
//        for(String tag : squareMap.keySet()) {
//            gBlockCornerUL block = (gBlockCornerUL) squareMap.get(tag);
//            if(block.contains("wallh") && block.isOne("frontwall")) {
//                gPlayer userplayer = cClientLogic.getUserPlayer();
//                if(userplayer != null) {
//                    boolean indepth = block.getInt("coordy") + block.getInt("dimh") - block.getInt("toph")
//                            > userplayer.getInt("coordy");
//                    int[][] bottomSectionPoints = new int[][]{
//                            new int[] {
//                                    block.getInt("coordx"),
//                                    block.getInt("coordx") + block.getInt("dimw"),
//                                    block.getInt("coordx") + block.getInt("dimw"),
//                                    block.getInt("coordx")
//                            },
//                            new int[] {
//                                    block.getInt("coordy") + block.getInt("dimh") - block.getInt("toph"),
//                                    block.getInt("coordy") + block.getInt("wallh") - block.getInt("toph"),
//                                    block.getInt("coordy") + block.getInt("wallh"),
//                                    block.getInt("coordy") + block.getInt("dimh")
//                            }};
//                    int[][] playerPoints = new int[][]{
//                            new int[] {
//                                    userplayer.getInt("coordx"),
//                                    userplayer.getInt("coordx") + userplayer.getInt("dimw"),
//                                    userplayer.getInt("coordx") + userplayer.getInt("dimw"),
//                                    userplayer.getInt("coordx")
//                            },
//                            new int[] {
//                                    userplayer.getInt("coordy"),
//                                    userplayer.getInt("coordy"),
//                                    userplayer.getInt("coordy") + userplayer.getInt("dimh"),
//                                    userplayer.getInt("coordy") + userplayer.getInt("dimh")
//                            }};
//                    Line2D bs1 = new Line2D.Float(bottomSectionPoints[0][0], bottomSectionPoints[1][0],
//                            bottomSectionPoints[0][1], bottomSectionPoints[1][1]);
//                    Line2D bs2 = new Line2D.Float(bottomSectionPoints[0][1], bottomSectionPoints[1][1],
//                            bottomSectionPoints[0][2], bottomSectionPoints[1][2]);
//                    Line2D bs3 = new Line2D.Float(bottomSectionPoints[0][2], bottomSectionPoints[1][2],
//                            bottomSectionPoints[0][3], bottomSectionPoints[1][3]);
//                    Line2D bs4 = new Line2D.Float(bottomSectionPoints[0][3], bottomSectionPoints[1][3],
//                            bottomSectionPoints[0][0], bottomSectionPoints[1][0]);
//                    Line2D ps1 = new Line2D.Float(playerPoints[0][0], playerPoints[1][0],
//                            playerPoints[0][1], playerPoints[1][1]);
//                    Line2D ps2 = new Line2D.Float(playerPoints[0][1], playerPoints[1][1],
//                            playerPoints[0][2], playerPoints[1][2]);
//                    Line2D ps3 = new Line2D.Float(playerPoints[0][2], playerPoints[1][2],
//                            playerPoints[0][3], playerPoints[1][3]);
//                    Line2D ps4 = new Line2D.Float(playerPoints[0][3], playerPoints[1][3],
//                            playerPoints[0][0], playerPoints[1][0]);
//                    if(!ps2.intersectsLine(bs3) && !ps4.intersectsLine(bs3) && indepth)
//                        dBlockWalls.drawBlockWallCornerUL(g2, block);
//                }
//                else
//                    dBlockWalls.drawBlockWallCornerUL(g2, block);
//            }
//            if(block.contains("toph") && block.isZero("backtop")) {
//                dBlockTops.drawBlockTopCornerUL(g2, block);
//            }
//        }
//        squareMap = scene.getThingMap("BLOCK_CORNERLR");
//        for(String tag : squareMap.keySet()) {
//            gBlockCornerLR block = (gBlockCornerLR) squareMap.get(tag);
//            if(block.contains("wallh") && block.isOne("frontwall")) {
//                gPlayer userplayer = cClientLogic.getUserPlayer();
//                if(userplayer != null) {
//                    if(block.getInt("coordy") + block.getInt("dimh") - block.getInt("toph")
//                            > userplayer.getInt("coordy"))
//                        dBlockWalls.drawBlockWallCornerLR(g2, block);
//                }
//                else
//                    dBlockWalls.drawBlockWallCornerLR(g2, block);
//            }
//            if(block.contains("toph") && block.isZero("backtop")) {
//                dBlockTops.drawBlockTopCornerLR(g2, block);
//            }
//        }
//        squareMap = scene.getThingMap("BLOCK_CORNERLL");
//        for(String tag : squareMap.keySet()) {
//            gBlockCornerLL block = (gBlockCornerLL) squareMap.get(tag);
//            if(block.contains("wallh") && block.isOne("frontwall")) {
//                gPlayer userplayer = cClientLogic.getUserPlayer();
//                if(userplayer != null) {
//                    if(block.getInt("coordy") + block.getInt("dimh") - block.getInt("toph")
//                            > userplayer.getInt("coordy"))
//                        dBlockWalls.drawBlockWallCornerLL(g2, block);
//                }
//                else
//                    dBlockWalls.drawBlockWallCornerLL(g2, block);
//            }
//            if(block.contains("toph") && block.isZero("backtop")) {
//                dBlockTops.drawBlockTopCornerLL(g2, block);
//            }
//        }
        squareMap = scene.getThingMap("BLOCK_CUBE");
        gBlockFactory.instance().topTexture = new TexturePaint(gBlockFactory.instance().topImage,
                new Rectangle2D.Double(
                        eUtils.scaleInt(-cVars.getInt("camx")),
                        eUtils.scaleInt(-cVars.getInt("camy")),
                        eUtils.scaleInt(150),
                        eUtils.scaleInt(1200)));
        for(String tag : squareMap.keySet()) {
            gBlockCube block = (gBlockCube) squareMap.get(tag);
            if(block.contains("toph") && block.isZero("backtop")) {
                dBlockTops.drawBlockTopCube(g2, block);

            }
            if(block.contains("wallh") && block.isOne("frontwall")) {
                gPlayer userplayer = cClientLogic.getUserPlayer();
                if(userplayer != null) {
                    if(block.getInt("coordy") + block.getInt("dimh") - block.getInt("toph")
                            > userplayer.getInt("coordy"))
                        dBlockWalls.drawBlockWallCube(g2, block);
                }
                else
                    dBlockWalls.drawBlockWallCube(g2, block);
            }
        }
    }

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
