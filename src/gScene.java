import javax.sound.sampled.Clip;
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
    final ConcurrentLinkedQueue<Clip> soundClips;
    int brightnessLevel = 100;



	public gScene() {
        objectMaps = new ConcurrentHashMap<>();
        for(String s : sSettings.object_titles) {
            objectMaps.put(s, new ConcurrentHashMap<>());
        }
        soundClips = new ConcurrentLinkedQueue<>();
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
        ConcurrentHashMap<String, gThing> blockMap = new ConcurrentHashMap<>(getThingMap("BLOCK_CUBE"));
        ConcurrentHashMap<String, gThing> combinedMap = new ConcurrentHashMap<>();
        ConcurrentHashMap<String, gThing> itemMap = new ConcurrentHashMap<>(getThingMap("THING_ITEM"));
        for(String id : blockMap.keySet()) {
            if(blockMap.get(id).isOnScreen())
                combinedMap.put(id, blockMap.get(id));
        }
        for(String id : playerMap.keySet()) {
            if(playerMap.get(id).isOnScreen())
                combinedMap.put(id, playerMap.get(id));
        }
        for(String id : itemMap.keySet()) {
            if(itemMap.get(id).isOnScreen())
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

    public void saveAsPrefab(String filename, String foldername) {
        if(foldername == null || foldername.strip().length() < 1)
            foldername="prefabs";
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(foldername + "/" + filename), StandardCharsets.UTF_8))) {
            ArrayList<String> buildStrings = new ArrayList<>();
            ConcurrentHashMap<String, gThing> floorMap = getThingMap("BLOCK_FLOOR");
            ConcurrentHashMap<String, gThing> cubeMap = getThingMap("BLOCK_CUBE");
            ConcurrentHashMap<String, gThing> collisionMap = getThingMap("BLOCK_COLLISION");
            int idctr = 0;
            int modxctr = 0;
            int modyctr = 0;

            for(String id : floorMap.keySet()) {
                gThing block = floorMap.get(id);

                String modxstr = "$3";
                int modx = block.coords[0];
                if(modx != 0) {
                    modxctr++;
                    modxstr = "$xmod"+modxctr;
                    buildStrings.add(String.format("getres xmod%d sumint $3 %d", modxctr, modx));
                }

                String modystr = "$4";
                int mody = block.coords[1];
                if(mody != 0) {
                    modyctr++;
                    modystr = "$ymod"+modyctr;
                    buildStrings.add(String.format("getres ymod%d sumint $4 %d", modyctr, mody));
                }

                String modidstr = "$1";
                if(idctr > 0) {
                    modidstr = "$idmod" + idctr;
                    buildStrings.add(String.format("getres idmod%d sumint $1 %d", idctr, idctr));
                }
                buildStrings.add(String.format("putfloor %s $2 %s %s", modidstr, modxstr, modystr));

                idctr++;
            }

            for(String id : cubeMap.keySet()) {
                gBlockCube block = (gBlockCube) cubeMap.get(id);

                String modxstr = "$3";
                int modx = block.coords[0];
                if(modx != 0) {
                    modxctr++;
                    modxstr = "$xmod"+modxctr;
                    buildStrings.add(String.format("getres xmod%d sumint $3 %d", modxctr, modx));
                }

                String modystr = "$4";
                int mody = block.coords[1];
                if(mody != 0) {
                    modyctr++;
                    modystr = "$ymod"+modyctr;
                    buildStrings.add(String.format("getres ymod%d sumint $4 %d", modyctr, mody));
                }

                String modidstr = "$1";
                if(idctr > 0) {
                    modidstr = "$idmod" + idctr;
                    buildStrings.add(String.format("getres idmod%d sumint $1 %d", idctr, idctr));
                }
                buildStrings.add(String.format("putcube %s $2 %s %s %d %d %d %d", modidstr, modxstr, modystr, block.dims[0], block.dims[1], block.toph, block.wallh));

                idctr++;
            }

            for(String id : collisionMap.keySet()) {
                gThing block = collisionMap.get(id);

                String modxstr = "$3";
                int modx = block.coords[0];
                if(modx != 0) {
                    modxctr++;
                    modxstr = "$xmod"+modxctr;
                    buildStrings.add(String.format("getres xmod%d sumint $3 %d", modxctr, modx));
                }

                String modystr = "$4";
                int mody = block.coords[1];
                if(mody != 0) {
                    modyctr++;
                    modystr = "$ymod"+modyctr;
                    buildStrings.add(String.format("getres ymod%d sumint $4 %d", modyctr, mody));
                }

                String modidstr = "$1";
                if(idctr > 0) {
                    modidstr = "$idmod" + idctr;
                    buildStrings.add(String.format("getres idmod%d sumint $1 %d", idctr, idctr));
                }
                buildStrings.add(String.format("putcollision %s $2 %s %s %s %s", modidstr, modxstr, modystr, block.dims[0], block.dims[1]));

                idctr++;
            }

            for(String buildString : buildStrings) {
                writer.write(buildString + "\n");
            }
        } catch (IOException e) {
            xMain.shellLogic.console.logException(e);
            e.printStackTrace();
        }
    }

    public void saveAs(String filename, String foldername) {
        if(foldername == null || foldername.strip().length() < 1)
            foldername="maps";
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(foldername + "/" + filename), StandardCharsets.UTF_8))) {
            //these three are always here
            writer.write(String.format("load\ngamemode %d\n", sSettings.clientGameMode));
            ConcurrentHashMap<String, gThing> floorMap = getThingMap("BLOCK_FLOOR");
            ConcurrentHashMap<String, gThing> cubeMap = getThingMap("BLOCK_CUBE");
            ConcurrentHashMap<String, gThing> collisionMap = getThingMap("BLOCK_COLLISION");
            for(String id : floorMap.keySet()) {
                gBlockFloor floor = (gBlockFloor) floorMap.get(id);
                String[] args = new String[] {
                        floor.id, floor.prefabId,
                        Integer.toString(floor.coords[0]), Integer.toString(floor.coords[1])
                };
                StringBuilder floorString = new StringBuilder("putfloor");
                for(String arg : args) {
                    if(arg != null)
                        floorString.append(" ").append(arg);
                }
                floorString.append('\n');
                writer.write(floorString.toString());
            }
            for(String id : cubeMap.keySet()) {
                gBlockCube cube = (gBlockCube) cubeMap.get(id);
                String[] args = new String[] {
                        cube.id, cube.prefabId,
                        Integer.toString(cube.coords[0]), Integer.toString(cube.coords[1]),
                        Integer.toString(cube.dims[0]), Integer.toString(cube.dims[1]),
                        Integer.toString(cube.toph), Integer.toString(cube.wallh),
                };
                StringBuilder cubeString = new StringBuilder("putcube");
                for(String arg : args) {
                    if(arg != null)
                        cubeString.append(" ").append(arg);
                }
                cubeString.append('\n');
                writer.write(cubeString.toString());
            }
            for(String id : collisionMap.keySet()) {
                gBlockCollision collision = (gBlockCollision) collisionMap.get(id);
                String[] args = new String[] {
                        collision.id, collision.prefabId,
                        Integer.toString(collision.coords[0]), Integer.toString(collision.coords[1]),
                        Integer.toString(collision.dims[0]), Integer.toString(collision.dims[1]),
                };
                StringBuilder collisionString = new StringBuilder("putcollision");
                for(String arg : args) {
                    if(arg != null)
                        collisionString.append(" ").append(arg);
                }
                collisionString.append('\n');
                writer.write(collisionString.toString());
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