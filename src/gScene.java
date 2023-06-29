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
                        block.type, block.id, block.prefabId, Integer.toString(block.coords[0]),
                        Integer.toString(block.coords[1]), Integer.toString(block.dims[0]),
                        Integer.toString(block.dims[1]), Integer.toString(block.toph), Integer.toString(block.wallh)
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
                        item.type, item.id, Integer.toString(item.coords[0]), Integer.toString(item.coords[1])
                };
                StringBuilder str = new StringBuilder("putitem");
                for(String arg : args) {
                    if(arg != null)
                        str.append(" ").append(arg);
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