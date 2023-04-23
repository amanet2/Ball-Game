import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * A scene holds the background and objects for a game
 * play scenario.
 */
public class gScene {
    HashMap<String, LinkedHashMap<String, gThing>> objectMaps;

	public gScene() {
        objectMaps = new HashMap<>();
        for(String s : sSettings.object_titles) {
            objectMaps.put(s, new LinkedHashMap<>());
        }
    }

    public String[] getThingMapIds(String title) {
        Collection<String> pColl = getThingMap(title).keySet();
        int psize = pColl.size();
        return pColl.toArray(new String[psize]);
    }

    public LinkedHashMap<String, gThing> getThingMap(String thing_title) {
	    return objectMaps.get(thing_title);
    }

    public gPlayer getPlayerById(String id) {
        return (gPlayer) getThingMap("THING_PLAYER").get(id);
    }

    public Queue<gThing> getWallsAndPlayersSortedByCoordY() {
        Queue<gThing> visualQueue = new LinkedList<>();
        HashMap<String, gThing> playerMap = new HashMap<>(getThingMap("THING_PLAYER"));
        HashMap<String, gThing> combinedMap = new HashMap<>(getThingMap("BLOCK_CUBE"));
        HashMap<String, gThing> itemMap = new HashMap<>(getThingMap("THING_ITEM"));
        for(String id : playerMap.keySet()) {
            combinedMap.put(id, playerMap.get(id));
        }
        for(String id : itemMap.keySet()) {
            combinedMap.put(id+"_1", itemMap.get(id)); //avoid overlap with any tiles
        }
        boolean sorted = false;
        while(!sorted) {
            sorted = true;
            int lowestY = 1000000000;
            String lowestId = "";
            for(String id : combinedMap.keySet()) {
                if(combinedMap.get(id).getInt("coordy") <= lowestY) {
                    sorted = false;
                    lowestId = id;
                    lowestY = combinedMap.get(id).getInt("coordy");
                }
            }
            if(lowestId.length() > 0) {
                visualQueue.add(combinedMap.get(lowestId));
                combinedMap.remove(lowestId);
            }
        }
        return visualQueue;
    }

    public void clearThingMap(String thing_title) {
        ArrayList<String> toRemoveIds = new ArrayList<>();
        if(thing_title.contains("ITEM_")) {
            if(objectMaps.containsKey(thing_title))
                toRemoveIds.addAll(getThingMap(thing_title).keySet());
            for(String id : toRemoveIds) {
                getThingMap("THING_ITEM").remove(id);
            }
        }
        if(objectMaps.containsKey(thing_title))
            objectMaps.put(thing_title, new LinkedHashMap<>());
    }

    public void saveAs(String filename, String foldername) {
        if(foldername == null || foldername.strip().length() < 1)
            foldername="maps";
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(foldername + "/" + filename), StandardCharsets.UTF_8))) {
            //these three are always here
            writer.write(String.format("load\ncl_setvar cv_gamemode %d\n", cClientLogic.gamemode));
            HashMap<String, gThing> blockMap = getThingMap("THING_BLOCK");
            for(String id : blockMap.keySet()) {
                gBlock block = (gBlock) blockMap.get(id);
                String[] args = new String[]{
                        block.get("type"), block.get("id"), block.get("prefabid"), block.get("coordx"),
                        block.get("coordy"), block.get("dimw"), block.get("dimh"), block.get("toph"), block.get("wallh")
                };
                StringBuilder blockString = new StringBuilder("putblock");
                for(String arg : args) {
                    if(arg != null) {
                        blockString.append(" ").append(arg);
                    }
                }
                blockString.append('\n');
                writer.write(blockString.toString());
            }
            HashMap<String, gThing> itemMap = getThingMap("THING_ITEM");
            for(String id : itemMap.keySet()) {
                gItem item = (gItem) itemMap.get(id);
                String[] args = new String[]{
                        item.get("type"),
                        item.get("id"),
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
        } catch (IOException e) {
            eLogging.logException(e);
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
                        block.get("wallh")
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
        } catch (IOException e) {
            eLogging.logException(e);
            e.printStackTrace();
        }
    }
}