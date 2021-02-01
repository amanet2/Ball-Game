import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class gMap {
    static final int MAP_TOPVIEW = 0;
    static final int MAP_SIDEVIEW = 1;
    static String[] maptype_selection = new String[] {
            "top-down view",
            "side-scrolling view"
    };
    String mapName;
    ArrayList<String> execLines;
    int wasLoaded;
    gScene scene;
    //the plan for this map is for each string to point to a unique doable that returns a prop configured to match one
    //of the prop types we'd like to feature in the game
    HashMap<String, gDoablePropReturn> propLoadMap;
    HashMap<String, gDoableThingReturn> thingLoadMap;

    private void basicInit() {
        gTextures.clear();
        execLines = new ArrayList<>();
        scene = new gScene();
        wasLoaded = 0;
        propLoadMap = new HashMap<>();
        propLoadMap.put("PROP_TELEPORTER", new gDoablePropReturnTeleporter());
        propLoadMap.put("PROP_BOOSTUP", new gDoablePropReturnBoostup());
        propLoadMap.put("PROP_BALLBOUNCY", new gDoablePropReturnBallBouncy());
        propLoadMap.put("PROP_SCOREPOINT", new gDoablePropReturnScorepoint());
        propLoadMap.put("PROP_FLAGBLUE", new gDoablePropReturnFlagBlue());
        propLoadMap.put("PROP_FLAGRED", new gDoablePropReturnFlagRed());
        propLoadMap.put("PROP_POWERUP", new gDoablePropReturnPowerup());
        thingLoadMap = new HashMap<>();
        thingLoadMap.put("THING_FLARE", new gDoableThingReturnFlare());
    }

	public gMap() {
        basicInit();
        mapName = "new";
        cVars.putInt("maptype", sSettings.create_map_mode);
		cVars.putInt("gamemode", cGameMode.DEATHMATCH);
	}

    public gMap(String s) {
	    xCon.instance().debug("Loading Map: " + s);
        long ct = System.currentTimeMillis();
        try (BufferedReader br = new BufferedReader(new FileReader(s))) {
            basicInit();
            if(s.contains("/"))
                mapName = s.split("/")[1].split("\\.")[0];
            else
                mapName = s.split("\\.")[0];
            String line;
            while ((line = br.readLine()) != null) {
                String[] lineToks = line.split(" ");
                String putTitle = lineToks[0];
                String[] args = Arrays.copyOfRange(lineToks, 1, lineToks.length);
                gDoablePropReturn propReturnFunction = propLoadMap.get(putTitle);
                gDoableThingReturn thingReturnFunction = thingLoadMap.get(putTitle);
                if(thingReturnFunction != null) {
                    gThing thingToLoad = thingReturnFunction.getThing(args);
                    thingToLoad.putInt("native", 1);
                    thingReturnFunction.storeThing(thingToLoad, scene);
                }
                else if(propReturnFunction != null) {
                    gProp propToLoad = propReturnFunction.getProp(args);
                    propToLoad.put("id", cScripts.createID(8));
                    propToLoad.putInt("tag", scene.getThingMap("PROP_TELEPORTER").size());
                    propToLoad.putInt("native", 1);
                    propReturnFunction.storeProp(propToLoad, scene);
                }
                else if(lineToks[0].toLowerCase().equals("cmd")) {
                    if(lineToks.length > 1) {
                        xCon.ex(line.replaceFirst("cmd ", ""));
                        execLines.add(line);
                    }
                }
                else if (lineToks[0].toLowerCase().equals("tile")) {
                    gTile tile = new gTile(
                        Integer.parseInt(lineToks[4]),
                        Integer.parseInt(lineToks[5]),
                        Integer.parseInt(lineToks[6]),
                        Integer.parseInt(lineToks[7]),
                        Integer.parseInt(lineToks[8]),
                        Integer.parseInt(lineToks[9]),
                        Integer.parseInt(lineToks[10]),
                        Integer.parseInt(lineToks[11]),
                        Integer.parseInt(lineToks[12]),
                        Integer.parseInt(lineToks[13]),
                        Integer.parseInt(lineToks[14]),
                        eUtils.getPath(lineToks[1]),
                        eUtils.getPath(lineToks[2]),
                        eUtils.getPath(lineToks[3]),
                        Integer.parseInt(lineToks[15]),
                        Integer.parseInt(lineToks[16])
                    );
                    tile.putInt("id", scene.tiles().size());
                    scene.tiles().add(tile);
                }
                else if (lineToks[0].toLowerCase().equals("prop")) {
                    //only serves the old light fixtures now
                    gProp prop = new gProp(
                        Integer.parseInt(lineToks[1]),
                        Integer.parseInt(lineToks[2]),
                        Integer.parseInt(lineToks[3]),
                        Integer.parseInt(lineToks[4]),
                        Integer.parseInt(lineToks[5]),
                        Integer.parseInt(lineToks[6]),
                        Integer.parseInt(lineToks[7]));
                    prop.putInt("tag", scene.props().size());
                    prop.putInt("native", 1);
                    scene.props().add(prop);
                }
            }
            wasLoaded = 1;
        }
        catch (Exception e) {
            eUtils.echoException(e);
            e.printStackTrace();
            basicInit();
            mapName = "new";
            cVars.putInt("maptype", sSettings.create_map_mode);
            cVars.putInt("gamemode", cGameMode.DEATHMATCH);
            cVars.put("botbehavior", "");
        }
        xCon.instance().debug("Loading time: " + (System.currentTimeMillis() - ct));
    }

	public void save(String filename) {
	    System.out.println("SAVED " + filename);
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(sVars.get("datapath") + "/" + filename), StandardCharsets.UTF_8))) {
		    //these three are always here
            writer.write(String.format("cmd cv_maptype %s\n", cVars.get("maptype")));
            writer.write(String.format("cmd cv_gamemode %s\n", cVars.get("gamemode")));
            writer.write(String.format("cmd cv_botbehavior %s\n", cVars.get("botbehavior")));
            //this one is dynamic
            for(String s : execLines) {
                if(!s.contains("cv_maptype") && !s.contains("cv_gamemode") && !s.contains("cv_botbehavior")) {
                    writer.write(s + "\n");
                }
            }
            for(gTile t : scene.tiles()) {
                String str = String.format("tile %s %s %s %d %d %d %d %d %d %d %d %d %d %d %d %s\n",
                    t.get("sprite0").replace(xCon.ex("datapath")+"/",""),
                    t.get("sprite1").replace(xCon.ex("datapath")+"/",""),
                    t.get("sprite2").replace(xCon.ex("datapath")+"/",""),
                    t.getInt("coordx"), t.getInt("coordy"), t.getInt("dimw"), t.getInt("dimh"), t.getInt("dim0h"),
                    t.getInt("dim1h"), t.getInt("dim2h"), t.getInt("dim3h"), t.getInt("dim4h"),
                    t.getInt("dim5w"), t.getInt("dim6w"), t.getInt("brightness"), t.get("canspawn")
                );
                writer.write(str);
            }
            for(gProp p : scene.props()) {
                String savetitle = gProp.getSaveStringForProp(p);
                String str = String.format("%s %d %d %d %d %d %d\n", savetitle, p.getInt("int0"), p.getInt("int1"),
                    p.getInt("coordx"), p.getInt("coordy"), p.getInt("dimw"), p.getInt("dimh"));
                writer.write(str);
            }
            HashMap flaresMap = eManager.currentMap.scene.getThingMap("THING_FLARE");
            for(Object id : flaresMap.keySet()) {
                gFlare f = (gFlare) flaresMap.get(id);
                int b = f.getInt("flicker");
                String str = String.format("flare %d %d %d %d %d %d %d %d %d %d %d %d %d\n", f.getInt("coordx"),
                        f.getInt("coordy"), f.getInt("dimw"), f.getInt("dimh"), f.getInt("r1"), f.getInt("g1"),
                        f.getInt("b1"), f.getInt("a1"), f.getInt("r2"), f.getInt("g2"), f.getInt("b2"),
                        f.getInt("a2"), b);
                writer.write(str);
            }
            wasLoaded = 1;
		} catch (IOException e) {
            eUtils.echoException(e);
            e.printStackTrace();
		}
	}
}
