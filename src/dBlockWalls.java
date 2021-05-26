import java.awt.*;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class dBlockWalls {
    public static void drawBlockWallsAndPlayers(Graphics2D g2, gScene scene) {
        LinkedHashMap<String, gThing> combinedMap = getWallsAndPlayersSortedByCoordY(scene);
        for(String tag : combinedMap.keySet()) {
            gThing thing = combinedMap.get(tag);
            if(thing.contains("fv")) {
                gPlayer player = (gPlayer) thing;
                dPlayer.drawPlayer(g2, player);
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
                else if(thing.get("type").contains("CORNERUR")) {
                    if (thing.contains("wallh")) {
                        dBlockShadows.drawShadowBlockCornerUR(g2, (gBlockCornerUR) thing);
                        drawBlockWallCornerUR(g2, (gBlockCornerUR) thing);
                    }
                    if (thing.contains("toph") && thing.isOne("backtop")) {
                        dBlockTops.drawBlockTopCornerUR(g2, (gBlockCornerUR) thing);
                    }
                }
                else if(thing.get("type").contains("CORNERUL")) {
                    if (thing.contains("wallh")) {
                        dBlockShadows.drawShadowBlockCornerUL(g2, (gBlockCornerUL) thing);
                        drawBlockWallCornerUL(g2, (gBlockCornerUL) thing);
                    }
                    if (thing.contains("toph") && thing.isOne("backtop")) {
                        dBlockTops.drawBlockTopCornerUL(g2, (gBlockCornerUL) thing);
                    }
                }
                else if(thing.get("type").contains("CORNERLL")) {
                    if (thing.contains("wallh")) {
                        dBlockShadows.drawShadowBlockFlat(g2, (gBlock) thing);
                        drawBlockWallCornerLL(g2, (gBlockCornerLL) thing);
                    }
                    if (thing.contains("toph") && thing.isOne("backtop")) {
                        dBlockTops.drawBlockTopCornerLL(g2, (gBlockCornerLL) thing);
                    }
                }
                else if(thing.get("type").contains("CORNERLR")) {
                    if (thing.contains("wallh")) {
                        dBlockShadows.drawShadowBlockFlat(g2, (gBlock) thing);
                        drawBlockWallCornerLR(g2, (gBlockCornerLR) thing);
                    }
                    if (thing.contains("toph") && thing.isOne("backtop")) {
                        dBlockTops.drawBlockTopCornerLR(g2, (gBlockCornerLR) thing);
                    }
                }
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
        HashMap<String, gThing> cornerMapLL = new HashMap<>(scene.getThingMap("BLOCK_CORNERLL"));
        HashMap<String, gThing> cornerMapLR = new HashMap<>(scene.getThingMap("BLOCK_CORNERLR"));
        HashMap<String, gThing> combinedMap = new HashMap<>(scene.getThingMap("BLOCK_CUBE"));
        for(String id : cornerMapL.keySet()) {
            combinedMap.put(id, cornerMapL.get(id));
        }
        for(String id : cornerMapR.keySet()) {
            combinedMap.put(id, cornerMapR.get(id));
        }
        for(String id : cornerMapLL.keySet()) {
            combinedMap.put(id, cornerMapLL.get(id));
        }
        for(String id : cornerMapLR.keySet()) {
            combinedMap.put(id, cornerMapLR.get(id));
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
}
