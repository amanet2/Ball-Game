import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class dBlockWalls {
    public static void drawBlockWallsAndPlayers(Graphics2D g2, gScene scene) {
        LinkedHashMap<String, gThing> combinedMap = getWallsAndPlayersSortedByCoordY(scene);
        for(String tag : combinedMap.keySet()) {
            gThing thing = combinedMap.get(tag);
            if(thing.contains("fv")) {
                gPlayer player = (gPlayer) thing;
                //player shadow
                if(sVars.isOne("vfxenableplayershadow")) {
                    //check null fields
                    if(!player.containsFields(new String[]{"coordx", "coordy", "dimw", "dimh"}))
                        break;
                    int yadj = 5*player.getInt("dimh")/6;
                    Rectangle2D shadowBounds = new Rectangle.Double(
                            eUtils.scaleInt(player.getInt("coordx") - cVars.getInt("camx")),
                            eUtils.scaleInt(player.getInt("coordy") - cVars.getInt("camy")
                                    + yadj),
                            eUtils.scaleInt(player.getInt("dimw")),
                            eUtils.scaleInt(player.getInt("dimh")/3));
                    RadialGradientPaint df = new RadialGradientPaint(
                            shadowBounds, new float[]{0f, 1f},
                            new Color[]{
                                    new Color(0,0, 0, cVars.getInt("vfxshadowalpha1")),
                                    new Color(0, 0, 0, 0)
                            }, MultipleGradientPaint.CycleMethod.NO_CYCLE);
                    g2.setPaint(df);
                    g2.fillRect((int)shadowBounds.getX(), (int)shadowBounds.getY(), (int)shadowBounds.getWidth(),
                            (int)shadowBounds.getHeight());
                }
                //player itself
                g2.drawImage(
                        player.sprite,
                        eUtils.scaleInt(player.getInt("coordx") - cVars.getInt("camx")),
                        eUtils.scaleInt(player.getInt("coordy") - cVars.getInt("camy")),
                        null
                );
                //shading
                if(sVars.isOne("vfxenableshading")) {
                    GradientPaint df = new GradientPaint(
                            eUtils.scaleInt(player.getInt("coordx") - cVars.getInt("camx")),
                            eUtils.scaleInt(player.getInt("coordy") + 2*player.getInt("dimh")/3
                                    - cVars.getInt("camy")),
                            new Color(0,0, 0,0),
                            eUtils.scaleInt(player.getInt("coordx") - cVars.getInt("camx")),
                            eUtils.scaleInt(player.getInt("coordy") + player.getInt("dimh")
                                    - cVars.getInt("camy")),
                            new Color(0,0, 0,cVars.getInt("vfxshadowalpha1")/2));
                    g2.setPaint(df);
                    g2.fillOval(eUtils.scaleInt(player.getInt("coordx") - cVars.getInt("camx")),
                            eUtils.scaleInt(player.getInt("coordy") - cVars.getInt("camy")),
                            eUtils.scaleInt(player.getInt("dimw")),
                            eUtils.scaleInt(player.getInt("dimh")));
                }
            }
            else {
                if(thing.get("type").contains("CUBE")) {
                    if (thing.contains("wallh")) {
                        dBlockShadows.drawShadowBlockFlat(g2, (gBlockCube) thing);
                        drawBlockWallCube(g2, (gBlockCube) thing);
                    }
                    if (thing.contains("toph") && thing.isOne("backtop")) {
                        dBlockTops.drawBlockTopCube(g2, (gBlockCube) thing);
                    }
                }
                if(thing.get("type").contains("CORNERUR")) {
                    if (thing.contains("wallh")) {
                        dBlockShadows.drawShadowBlockCornerUR(g2, (gBlockCornerUR) thing);
                        drawBlockWallCornerUR(g2, (gBlockCornerUR) thing);
                    }
                    if (thing.contains("toph") && thing.isOne("backtop")) {
                        dBlockTops.drawBlockTopCornerUR(g2, (gBlockCornerUR) thing);
                    }
                }
                if(thing.get("type").contains("CORNERUL")) {
                    if (thing.contains("wallh")) {
                        dBlockShadows.drawShadowBlockCornerUL(g2, (gBlockCornerUL) thing);
                        drawBlockWallCornerUL(g2, (gBlockCornerUL) thing);
                    }
                    if (thing.contains("toph") && thing.isOne("backtop")) {
                        dBlockTops.drawBlockTopCornerUL(g2, (gBlockCornerUL) thing);
                    }
                }
            }
        }
    }

    public static void drawBlockWalls(Graphics2D g2, gScene scene) {
        HashMap<String, gThing> squareMap = scene.getThingMap("BLOCK_CUBE");
        for(String tag : squareMap.keySet()) {
            gBlockCube block = (gBlockCube) squareMap.get(tag);
            if(block.contains("wallh")) {
                dBlockShadows.drawShadowBlockFlat(g2, block);
                if(block.isZero("frontwall"))
                    drawBlockWallCube(g2, block);
                else {
                    gPlayer userplayer = cClientLogic.getUserPlayer();
                    if(userplayer != null) {
                        if(block.getInt("coordy") + block.getInt("dimh") - block.getInt("toph")
                                <= userplayer.getInt("coordy"))
                            drawBlockWallCube(g2, block);
                    }
                    else
                        drawBlockWallCube(g2, block);
                }
            }
            if(block.contains("toph") && block.isOne("backtop")) {
                dBlockTops.drawBlockTopCube(g2, block);
            }
        }
        squareMap = scene.getThingMap("BLOCK_CORNERUR");
        for(String tag : squareMap.keySet()) {
            gBlockCornerUR block = (gBlockCornerUR) squareMap.get(tag);
            if(block.contains("wallh")) {
                dBlockShadows.drawShadowBlockCornerUR(g2, block);
                if(block.isZero("frontwall"))
                    drawBlockWallCornerUR(g2, block);
                else {
                    gPlayer userplayer = cClientLogic.getUserPlayer();
                    if(userplayer != null) {
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
                        boolean indepth = block.getInt("coordy") + block.getInt("dimh") - block.getInt("toph")
                                <= userplayer.getInt("coordy");
                        if(ps2.intersectsLine(bs3) || ps4.intersectsLine(bs3) || indepth)
                            drawBlockWallCornerUR(g2, block);
                    }
                    else
                        drawBlockWallCornerUR(g2, block);
                }
            }
            if(block.contains("toph") && block.isOne("backtop")) {
                dBlockTops.drawBlockTopCornerUR(g2, block);
            }
        }
        squareMap = scene.getThingMap("BLOCK_CORNERUL");
        for(String tag : squareMap.keySet()) {
            gBlockCornerUL block = (gBlockCornerUL) squareMap.get(tag);
            if(block.contains("wallh")) {
                dBlockShadows.drawShadowBlockCornerUL(g2, block);
                if(block.isZero("frontwall"))
                    drawBlockWallCornerUL(g2, block);
                else {
                    gPlayer userplayer = cClientLogic.getUserPlayer();
                    if(userplayer != null) {
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
                        boolean indepth = block.getInt("coordy") + block.getInt("dimh") - block.getInt("toph")
                                <= userplayer.getInt("coordy");
                        if(ps2.intersectsLine(bs3) || ps4.intersectsLine(bs3) || indepth)
                            drawBlockWallCornerUL(g2, block);
                    }
                    else
                        drawBlockWallCornerUL(g2, block);
                }
            }
            if(block.contains("toph") && block.isOne("backtop")) {
                dBlockTops.drawBlockTopCornerUL(g2, block);
            }
        }
        squareMap = scene.getThingMap("BLOCK_CORNERLR");
        for(String tag : squareMap.keySet()) {
            gBlockCornerLR block = (gBlockCornerLR) squareMap.get(tag);
            if(block.contains("wallh")) {
                dBlockShadows.drawShadowBlockFlat(g2, block);
                if(block.isZero("frontwall"))
                    drawBlockWallCornerLR(g2, block);
                else {
                    gPlayer userplayer = cClientLogic.getUserPlayer();
                    if(userplayer != null) {
                        if(block.getInt("coordy") + block.getInt("dimh") - block.getInt("toph")
                                <= userplayer.getInt("coordy"))
                            drawBlockWallCornerLR(g2, block);
                    }
                    else
                        drawBlockWallCornerLR(g2, block);
                }
            }
            if(block.contains("toph") && block.isOne("backtop")) {
                dBlockTops.drawBlockTopCornerLR(g2, block);
            }
        }
        squareMap = scene.getThingMap("BLOCK_CORNERLL");
        for(String tag : squareMap.keySet()) {
            gBlockCornerLL block = (gBlockCornerLL) squareMap.get(tag);
            if(block.contains("wallh")) {
                dBlockShadows.drawShadowBlockFlat(g2, block);
                if(block.isZero("frontwall"))
                    drawBlockWallCornerLL(g2, block);
                else {
                    gPlayer userplayer = cClientLogic.getUserPlayer();
                    if(userplayer != null) {
                        if(block.getInt("coordy") + block.getInt("dimh") - block.getInt("toph")
                                <= userplayer.getInt("coordy"))
                            drawBlockWallCornerLL(g2, block);
                    }
                    else
                        drawBlockWallCornerLL(g2, block);
                }
            }
            if(block.contains("toph") && block.isOne("backtop")) {
                dBlockTops.drawBlockTopCornerLL(g2, block);
            }
        }
    }

    public static void drawBlockWallCube(Graphics2D g2, gBlockCube block) {
        if (block.contains("wallh")) {
            String[] colorvals = block.get("colorwall").split("\\.");
            g2.setColor(new Color(
                    Integer.parseInt(colorvals[0]),
                    Integer.parseInt(colorvals[1]),
                    Integer.parseInt(colorvals[2]),
                    Integer.parseInt(colorvals[3])
            ));
            g2.fillRect(eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")),
                    eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                            + block.getInt("toph")),
                    eUtils.scaleInt(block.getInt("dimw")),
                    eUtils.scaleInt(block.getInt("wallh"))
            );
            dBlockWallsShading.drawBlockWallsShadingFlat(g2, block);
        }
    }

    public static void drawBlockWallCornerUR(Graphics2D g2, gBlockCornerUR block) {
        String[] colorvals = block.get("colorwall").split("\\.");
        g2.setColor(new Color(
                Integer.parseInt(colorvals[0]),
                Integer.parseInt(colorvals[1]),
                Integer.parseInt(colorvals[2]),
                Integer.parseInt(colorvals[3])
        ));
        Polygon pw = new Polygon(
                new int[]{
                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")),
                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")
                                + block.getInt("dimw")),
                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")
                                + block.getInt("dimw")),
                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx"))
                },
                new int[]{
                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")),
                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                + block.getInt("toph")),
                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                + block.getInt("toph") + block.getInt("wallh")),
                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy") + block.getInt("wallh"))
                },
                4);
        g2.fillPolygon(pw);
        dBlockWallsShading.drawBlockWallsShadingCornerUR(g2, block, pw);
    }

    public static void drawBlockWallCornerLR(Graphics2D g2, gBlockCornerLR block) {
        String[] colorvals = block.get("colorwall").split("\\.");
        g2.setColor(new Color(
                Integer.parseInt(colorvals[0]),
                Integer.parseInt(colorvals[1]),
                Integer.parseInt(colorvals[2]),
                Integer.parseInt(colorvals[3])
        ));
        Polygon pw = new Polygon(
                new int[]{
                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")),
                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")
                                + block.getInt("dimw")),
                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")
                                + block.getInt("dimw")),
                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx"))
                },
                new int[]{
                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                + block.getInt("toph")),
                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                + block.getInt("toph")),
                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                + block.getInt("toph") + block.getInt("wallh")),
                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                + block.getInt("toph") + block.getInt("wallh"))
                },
                4);
        g2.fillPolygon(pw);
        dBlockWallsShading.drawBlockWallsShadingFlat(g2, block);
    }

    public static void drawBlockWallCornerUL(Graphics2D g2, gBlockCornerUL block) {
        String[] colorvals = block.get("colorwall").split("\\.");
        g2.setColor(new Color(
                Integer.parseInt(colorvals[0]),
                Integer.parseInt(colorvals[1]),
                Integer.parseInt(colorvals[2]),
                Integer.parseInt(colorvals[3])
        ));
        Polygon pw = new Polygon(
                new int[]{
                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")),
                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")
                                + block.getInt("dimw")),
                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")
                                + block.getInt("dimw")),
                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx"))
                },
                new int[]{
                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                + block.getInt("toph")),
                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")),
                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                + block.getInt("wallh")),
                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                + block.getInt("toph") + block.getInt("wallh")),
                },
                4);
        g2.fillPolygon(pw);
        dBlockWallsShading.drawBlockWallsShadingCornerUL(g2, block, pw);
    }

    public static void drawBlockWallCornerLL(Graphics2D g2, gBlockCornerLL block) {
        String[] colorvals = block.get("colorwall").split("\\.");
        g2.setColor(new Color(
                Integer.parseInt(colorvals[0]),
                Integer.parseInt(colorvals[1]),
                Integer.parseInt(colorvals[2]),
                Integer.parseInt(colorvals[3])
        ));
        Polygon pw = new Polygon(
                new int[]{
                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")),
                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")
                                + block.getInt("dimw")),
                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")
                                + block.getInt("dimw")),
                        eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx"))
                },
                new int[]{
                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                + block.getInt("toph")),
                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                + block.getInt("toph")),
                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                + block.getInt("toph") + block.getInt("wallh")),
                        eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                + block.getInt("toph") + block.getInt("wallh"))
                },
                4);
        g2.fillPolygon(pw);
        dBlockWallsShading.drawBlockWallsShadingFlat(g2, block);
    }

    public static LinkedHashMap<String, gThing> getWallsAndPlayersSortedByCoordY(gScene scene) {
        LinkedHashMap<String, gThing> sortedWalls = new LinkedHashMap<>();
        HashMap<String, gThing> playerMap = new HashMap<>(scene.getThingMap("THING_PLAYER"));
        HashMap<String, gThing> cornerMapL = new HashMap<>(scene.getThingMap("BLOCK_CORNERUL"));
        HashMap<String, gThing> cornerMapR = new HashMap<>(scene.getThingMap("BLOCK_CORNERUR"));
        HashMap<String, gThing> combinedMap = new HashMap<>(scene.getThingMap("BLOCK_CUBE"));
        for(String id : cornerMapL.keySet()) {
            combinedMap.put(id, cornerMapL.get(id));
        }
        for(String id : cornerMapR.keySet()) {
            combinedMap.put(id, cornerMapR.get(id));
        }
        for(String id : playerMap.keySet()) {
            combinedMap.put(id, playerMap.get(id));
        }
        boolean sorted = false;
        while(!sorted) {
            sorted = true;
            int lowestY = 1000000;
            String lowestId = "";
            for(String id : combinedMap.keySet()) {
                if(((combinedMap.get(id).contains("wallh") && combinedMap.get(id).getInt("wallh") > 0)
                        || (combinedMap.get(id).contains("fv")))
                        && combinedMap.get(id).getInt("coordy") <= lowestY) {
                    sorted = false;
                    lowestId = id;
                    lowestY = combinedMap.get(id).getInt("coordy");
                }
            }
            if(lowestId.length() > 0) {
                sortedWalls.put(lowestId, combinedMap.get(lowestId));
                combinedMap.remove(lowestId);
            }
        }
        return sortedWalls;
    }

    public static HashMap<String, gThing> getFlatWallsSortedByCoordY() {
        HashMap<String, gThing> sortedWalls = new HashMap<>();
        HashMap<String, gThing> wallMap = new HashMap<>(cClientLogic.scene.getThingMap("BLOCK_CUBE"));
        boolean sorted = false;
        while(!sorted) {
            sorted = true;
            int lowestY = 1000000;
            String lowestId = "";
            for(String id : wallMap.keySet()) {
                if(wallMap.get(id).getInt("wallh") > 0
                        && wallMap.get(id).getInt("coordy") <= lowestY) {
                    sorted = false;
                    lowestId = id;
                    lowestY = wallMap.get(id).getInt("coordy");
                }
            }
            if(lowestId.length() > 0) {
                sortedWalls.put(lowestId, wallMap.get(lowestId));
                wallMap.remove(lowestId);
            }
        }
        return sortedWalls;
    }
}
