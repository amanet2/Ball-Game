import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class gMap {
    String mapName;
    int wasLoaded;
    gScene scene;

    private void basicInit() {
        eManager.currentMap = null;
        gTextures.clear();
        scene = new gScene();
        wasLoaded = 0;
        cVars.put("maploaded", "0");
    }

	public gMap() {
        basicInit();
        mapName = "new";
		cVars.putInt("gamemode", cGameMode.DEATHMATCH);
	}

	public void saveAs(String filename) {
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream("maps/" + filename), StandardCharsets.UTF_8))) {
		    //these three are always here
            writer.write(String.format("cv_gamemode %s\n", cVars.get("gamemode")));
            HashMap<String, gThing> blockMap = scene.getThingMap("THING_BLOCK");
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
                        block.get("frontwall"),
                        block.get("backtop")
                };
                String prefabString = "";
                if(block.contains("prefabid")) {
                    prefabString = "cv_prefabid " + block.get("prefabid");
                    writer.write(prefabString + '\n');
                }
                StringBuilder blockString = new StringBuilder("putblock");
                for(String arg : args) {
                    if(arg != null) {
                        blockString.append(" ").append(arg);
                    }
                }
                blockString.append('\n');
                writer.write(blockString.toString());
            }
            HashMap<String, gThing> collisionMap = scene.getThingMap("THING_COLLISION");
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
                String prefabString = "";
                if(collision.contains("prefabid")) {
                    prefabString = "cv_prefabid " + collision.get("prefabid");
                    writer.write(prefabString + '\n');
                }
                StringBuilder str = new StringBuilder("putcollision");
                for(String arg : args) {
                    if(arg != null) {
                        str.append(" ").append(arg);
                    }
                }
                str.append('\n');
                writer.write(str.toString());
            }
            HashMap<String, gThing> itemMap = scene.getThingMap("THING_ITEM");
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
            HashMap<String, gThing> flareMap = scene.getThingMap("THING_FLARE");
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
            System.out.println("SAVED " + filename);
            wasLoaded = 1;
		} catch (IOException e) {
            eUtils.echoException(e);
            e.printStackTrace();
		}
	}

    public void exportasprefab(String filename) {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(sVars.get("datapath") + "/prefabs/" + filename), StandardCharsets.UTF_8))) {
            for(String id : scene.getThingMap("THING_BLOCK").keySet()) {
                gBlock block = (gBlock) scene.getThingMap("THING_BLOCK").get(id);
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
                        block.get("frontwall"),
                        block.get("backtop")
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
            for(String id : scene.getThingMap("THING_COLLISION").keySet()) {
                gCollision collision = (gCollision) scene.getThingMap("THING_COLLISION").get(id);
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
            System.out.println("EXPORTED AS PREFAB " + filename);
        } catch (IOException e) {
            eUtils.echoException(e);
            e.printStackTrace();
        }
    }
}
