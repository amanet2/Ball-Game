import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * A scene holds the background and objects for a game
 * play scenario.
 */
public class gScene {
    public static final String[] object_titles = new String[]{
        "THING_PLAYER","THING_BULLET","THING_POPUP","THING_FLARE","THING_ANIMATION", "THING_BOTPLAYER", "THING_BLOCK",
        "BLOCK_CUBE", "BLOCK_FLOOR", "BLOCK_CORNERUR", "BLOCK_CORNERLR", "BLOCK_CORNERLL", "BLOCK_CORNERUL",
        "THING_COLLISION", "THING_ITEM", "ITEM_SPAWNPOINT", "ITEM_FLAGRED", "ITEM_FLAGBLUE", "ITEM_SHOTGUN",
        "ITEM_TELEPORTER_RED", "ITEM_TELEPORTER_BLUE", "ITEM_FLAG"
    };

	HashMap<String, LinkedHashMap> objectMaps;
	int blockIdCtr;
	int collisionIdCtr;
	int itemIdCtr;
	int flareIdCtr;

	public gScene() {
        objectMaps = new HashMap<>();
        for(String s : object_titles) {
            objectMaps.put(s, new LinkedHashMap());
        }
        blockIdCtr = 0;
        collisionIdCtr = 0;
        itemIdCtr = 0;
        flareIdCtr = 0;
    }

    public LinkedHashMap<String, gThing> getThingMap(String thing_title) {
	    return objectMaps.get(thing_title);
    }

    public gPlayer getPlayerById(String id) {
        return (gPlayer) getThingMap("THING_PLAYER").get(id);
    }

    public int getHighestPrefabId() {
        int idctr = -1;
        for(String id : getThingMap("THING_BLOCK").keySet()) {
            gThing block = getThingMap("THING_BLOCK").get(id);
            if(block.contains("prefabid") && block.getInt("prefabid") >= idctr)
                idctr = block.getInt("prefabid");
        }
        for(String id : getThingMap("THING_COLLISION").keySet()) {
            gThing collision = getThingMap("THING_COLLISION").get(id);
            if(collision.contains("prefabid") && collision.getInt("prefabid") >= idctr)
                idctr = collision.getInt("prefabid");
        }
        return idctr;
    }


    public int getHighestItemId() {
        int idctr = -1;
        for(String id : getThingMap("THING_ITEM").keySet()) {
            gThing item = getThingMap("THING_ITEM").get(id);
            if(item.contains("itemid") && item.getInt("itemid") >= idctr)
                idctr = item.getInt("itemid");
        }
        return idctr;
    }

    public LinkedHashMap<String, gThing> getWallsAndPlayersSortedByCoordY() {
        LinkedHashMap<String, gThing> sortedMapPreCorners = new LinkedHashMap<>();
        HashMap<String, gThing> playerMap = new HashMap<>(getThingMap("THING_PLAYER"));
//        HashMap<String, gThing> cornerMapL = new HashMap<>(getThingMap("BLOCK_CORNERUL"));
//        HashMap<String, gThing> cornerMapR = new HashMap<>(getThingMap("BLOCK_CORNERUR"));
//        HashMap<String, gThing> cornerMapLL = new HashMap<>(getThingMap("BLOCK_CORNERLL"));
//        HashMap<String, gThing> cornerMapLR = new HashMap<>(getThingMap("BLOCK_CORNERLR"));
        HashMap<String, gThing> combinedMap = new HashMap<>(getThingMap("BLOCK_CUBE"));
//        for(String id : cornerMapL.keySet()) {
//            combinedMap.put(id, cornerMapL.get(id));
//        }
//        for(String id : cornerMapR.keySet()) {
//            combinedMap.put(id, cornerMapR.get(id));
//        }
//        for(String id : cornerMapLL.keySet()) {
//            combinedMap.put(id, cornerMapLL.get(id));
//        }
//        for(String id : cornerMapLR.keySet()) {
//            combinedMap.put(id, cornerMapLR.get(id));
//        }
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
                sortedMapPreCorners.put(lowestId, combinedMap.get(lowestId));
                combinedMap.remove(lowestId);
            }
        }
        return sortedMapPreCorners;
//        //make another pass and move all corners to the max of their respective Y
//        LinkedHashMap<String, gThing> sortedMapPostCorners = new LinkedHashMap<>();
//        HashMap<String, gThing> cornerStagingMap = new HashMap<>();
//        int cornerStagingY = -1000000;
//        for(String id : sortedMapPreCorners.keySet()) {
//            gThing thing = sortedMapPreCorners.get(id);
//            int thingY = thing.getInt("coordy");
//            if(cornerStagingMap.size() > 0 && cornerStagingY < thingY) {
//                cornerStagingY = thingY;
//                for(String cid : cornerStagingMap.keySet()) {
//                    sortedMapPostCorners.put(cid, cornerStagingMap.get(cid));
//                }
//                cornerStagingMap = new HashMap<>();
//            }
//            if(thing.contains("type") && thing.get("type").contains("CORNER")) {
//                cornerStagingMap.put(id, thing);
//                cornerStagingY = thingY;
//            }
//            else {
//                sortedMapPostCorners.put(id, thing);
//            }
//        }
//        if(cornerStagingMap.size() > 0) {
//            for(String id : cornerStagingMap.keySet()) {
//                sortedMapPostCorners.put(id, cornerStagingMap.get(id));
//            }
//        }
//        return sortedMapPostCorners;
    }

    public void saveAs(String filename, String foldername) {
        if(foldername == null || foldername.strip().length() < 1)
            foldername="maps";
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(foldername + "/" + filename), StandardCharsets.UTF_8))) {
            //these three are always here
            writer.write(String.format("load\ncv_maploaded 0\ncv_gamemode %s\n", cVars.get("gamemode")));
            HashMap<String, gThing> blockMap = getThingMap("THING_BLOCK");
            for(String id : blockMap.keySet()) {
                gBlock block = (gBlock) blockMap.get(id);
                String[] args = new String[]{
                        block.get("type"),
                        block.get("coordx"),
                        block.get("coordy"),
                        block.get("dimw"),
                        block.get("dimh"),
                        block.get("toph"),
                        block.get("wallh"),
                        block.get("color"),
                        block.get("colorwall"),
                        block.get("frontwall")
                };
                if(block.contains("prefabid"))
                    writer.write("cv_prefabid " + block.get("prefabid") + '\n');
                StringBuilder blockString = new StringBuilder("putblock");
                for(String arg : args) {
                    if(arg != null) {
                        blockString.append(" ").append(arg);
                    }
                }
                blockString.append('\n');
                writer.write(blockString.toString());
            }
            HashMap<String, gThing> collisionMap = getThingMap("THING_COLLISION");
            for(String id : collisionMap.keySet()) {
                gCollision collision = (gCollision) collisionMap.get(id);
                StringBuilder xString = new StringBuilder();
                StringBuilder yString = new StringBuilder();
                for(int i = 0; i < collision.xarr.length; i++) {
                    int coordx = collision.xarr[i];
                    xString.append(coordx).append(".");
                }
                xString = new StringBuilder(xString.substring(0, xString.lastIndexOf(".")));
                for(int i = 0; i < collision.yarr.length; i++) {
                    int coordy = collision.yarr[i];
                    yString.append(coordy).append(".");
                }
                yString = new StringBuilder(yString.substring(0, yString.lastIndexOf(".")));
                String[] args = new String[]{
                        xString.toString(),
                        yString.toString(),
                        Integer.toString(collision.npoints)
                };
                if(collision.contains("prefabid"))
                    writer.write("cv_prefabid " + collision.get("prefabid") + '\n');
                StringBuilder str = new StringBuilder("putcollision");
                for(String arg : args) {
                    if(arg != null) {
                        str.append(" ").append(arg);
                    }
                }
                str.append('\n');
                writer.write(str.toString());
            }
            HashMap<String, gThing> itemMap = getThingMap("THING_ITEM");
            for(String id : itemMap.keySet()) {
                gItem item = (gItem) itemMap.get(id);
                String[] args = new String[]{
                        item.get("type"),
                        item.get("coordx"),
                        item.get("coordy")
                };
                StringBuilder str = new StringBuilder("putitem");
                for(String arg : args) {
                    if(arg != null) {
                        str.append(" ").append(arg);
                    }
                }
                str.append('\n');
                writer.write(str.toString());
            }
            HashMap<String, gThing> flareMap = getThingMap("THING_FLARE");
            for(String id : flareMap.keySet()) {
                gFlare flare = (gFlare) flareMap.get(id);
                String[] args = new String[]{
                        flare.get("coordx"),
                        flare.get("coordy"),
                        flare.get("dimw"),
                        flare.get("dimh"),
                        flare.get("r1"),
                        flare.get("g1"),
                        flare.get("b1"),
                        flare.get("a1"),
                        flare.get("r2"),
                        flare.get("g2"),
                        flare.get("b2"),
                        flare.get("a2")
                };
                StringBuilder str = new StringBuilder("putflare");
                for(String arg : args) {
                    if(arg != null) {
                        str.append(" ").append(arg);
                    }
                }
                str.append('\n');
                writer.write(str.toString());
            }
            writer.write("cv_maploaded 1\n");
        } catch (IOException e) {
            eUtils.echoException(e);
            e.printStackTrace();
        }
    }

    public void exportasprefab(String filename) {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("prefabs/" + filename), StandardCharsets.UTF_8))) {
            for(String id : getThingMap("THING_BLOCK").keySet()) {
                gBlock block = (gBlock) getThingMap("THING_BLOCK").get(id);
                int coordx = block.getInt("coordx");
                int coordy = block.getInt("coordy");
                String xString = "$1";
                String yString = "$2";

                if(coordx < 0) {
                    xString += Integer.toString(coordx);
                }
                else if(coordx > 0) {
                    xString += "+";
                    xString += Integer.toString(coordx);
                }

                if(coordy < 0) {
                    yString += Integer.toString(coordy);
                }
                else if(coordy > 0) {
                    yString += "+";
                    yString += Integer.toString(coordy);
                }

                String[] args = new String[]{
                        block.get("type"),
                        xString,
                        yString,
                        block.get("dimw"),
                        block.get("dimh"),
                        block.get("toph"),
                        block.get("wallh"),
                        block.get("color"),
                        block.get("colorwall"),
                        block.get("frontwall")
                };
                StringBuilder str = new StringBuilder("putblock");
                for(String arg : args) {
                    if(arg != null) {
                        str.append(" ").append(arg);
                    }
                }
                str.append('\n');
                writer.write(str.toString());
            }
            for(String id : getThingMap("THING_COLLISION").keySet()) {
                gCollision collision = (gCollision) getThingMap("THING_COLLISION").get(id);
                String xString = "";
                String yString = "";
                for(int i = 0; i < collision.xarr.length; i++) {
                    String ws = "$1";
                    int coordx = collision.xarr[i];
                    if(coordx < 0) {
                        ws += Integer.toString(coordx);
                    }
                    else if(coordx > 0) {
                        ws += "+";
                        ws += Integer.toString(coordx);
                    }
                    ws += ".";
                    xString += ws;
                }
                xString = xString.substring(0, xString.lastIndexOf("."));
                for(int i = 0; i < collision.yarr.length; i++) {
                    String ws = "$2";
                    int coordy = collision.yarr[i];
                    if(coordy < 0) {
                        ws += Integer.toString(coordy);
                    }
                    else if(coordy > 0) {
                        ws += "+";
                        ws += Integer.toString(coordy);
                    }
                    ws += ".";
                    yString += ws;
                }
                yString = yString.substring(0, yString.lastIndexOf("."));

                String[] args = new String[]{
                        xString,
                        yString,
                        Integer.toString(collision.npoints)
                };
                StringBuilder str = new StringBuilder("putcollision");
                for(String arg : args) {
                    if(arg != null) {
                        str.append(" ").append(arg);
                    }
                }
                str.append('\n');
                writer.write(str.toString());
            }
        } catch (IOException e) {
            eUtils.echoException(e);
            e.printStackTrace();
        }
    }
}