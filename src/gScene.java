import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * A scene holds the background and objects for a game
 * play scenario.
 */
public class gScene {
    final ConcurrentHashMap<String, ConcurrentHashMap<String, gThing>> objectMaps;

	public gScene() {
        objectMaps = new ConcurrentHashMap<>();
        for(String s : sSettings.object_titles) {
            objectMaps.put(s, new ConcurrentHashMap<>());
        }
    }

    public String[] getThingMapIds(String title) {
        Collection<String> pColl = getThingMap(title).keySet();
        int psize = pColl.size();
        return pColl.toArray(new String[psize]);
    }

    public ConcurrentHashMap<String, gThing> getThingMap(String thing_title) {
        return objectMaps.get(thing_title);
    }

    public gPlayer getPlayerById(String id) {
        return (gPlayer) getThingMap("THING_PLAYER").get(id);
    }

    public Queue<gThing> getWallsAndPlayersSortedByCoordY() {
        Queue<gThing> visualQueue = new LinkedList<>();
        ConcurrentHashMap<String, gThing> playerMap = new ConcurrentHashMap<>(getThingMap("THING_PLAYER"));
        ConcurrentHashMap<String, gThing> combinedMap = new ConcurrentHashMap<>(getThingMap("BLOCK_CUBE"));
        ConcurrentHashMap<String, gThing> itemMap = new ConcurrentHashMap<>(getThingMap("THING_ITEM"));
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
                if(combinedMap.get(id).coords[1] <= lowestY) {
                    sorted = false;
                    lowestId = id;
                    lowestY = combinedMap.get(id).coords[1];
                }
            }
            if(lowestId.length() > 0) {
                visualQueue.add(combinedMap.get(lowestId));
                combinedMap.remove(lowestId);
            }
        }
        return visualQueue;
    }

    public void saveAs(String filename, String foldername) {
        if(foldername == null || foldername.strip().length() < 1)
            foldername="maps";
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(foldername + "/" + filename), StandardCharsets.UTF_8))) {
            //these three are always here
            writer.write(String.format("load\ngamemode %d\n", sSettings.clientGameMode));
            ConcurrentHashMap<String, gThing> blockMap = getThingMap("THING_BLOCK");
            for(String id : blockMap.keySet()) {
                gThing block = blockMap.get(id);
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
            ConcurrentHashMap<String, gThing> itemMap = getThingMap("THING_ITEM");
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
            xMain.shellLogic.console.logException(e);
            e.printStackTrace();
        }
    }

    public void exportasprefab(String filename) {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("prefabs/" + filename), StandardCharsets.UTF_8))) {
            for(String id : getThingMap("THING_BLOCK").keySet()) {
                gThing block = getThingMap("THING_BLOCK").get(id);
                int coordx = block.coords[0];
                int coordy = block.coords[1];
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
            xMain.shellLogic.console.logException(e);
            e.printStackTrace();
        }
    }
}