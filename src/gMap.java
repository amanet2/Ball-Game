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

	public gMap() {
		gTextures.clear();
        mapName = "new";
        execLines = new ArrayList<>();
		scene = new gScene();
        wasLoaded = 0;
        propLoadMap = new HashMap<>();
        propLoadMap.put("PROP_TELEPORTER", new gDoablePropReturnTeleporter());
        propLoadMap.put("PROP_BOOSTUP", new gDoablePropReturnBoostup());
        cVars.putInt("maptype", sSettings.create_map_mode);
		cVars.putInt("gamemode", cGameMode.DEATHMATCH);
	}

    public gMap(String s) {
	    xCon.instance().debug("Loading Map: " + s);
        long ct = System.currentTimeMillis();
        try (BufferedReader br = new BufferedReader(new FileReader(s))) {
            gTextures.clear();
            if(s.contains("/"))
                mapName = s.split("/")[1].split("\\.")[0];
            else
                mapName = s.split("\\.")[0];
            execLines = new ArrayList<>();
            scene = new gScene();
            propLoadMap = new HashMap<>();
            propLoadMap.put("PROP_TELEPORTER", new gDoablePropReturnTeleporter());
            propLoadMap.put("PROP_BOOSTUP", new gDoablePropReturnBoostup());
            String line;
            while ((line = br.readLine()) != null) {
                String[] lineToks = line.split(" ");
                String putTitle = lineToks[0];
                String[] args = Arrays.copyOfRange(lineToks, 1, lineToks.length);
                gDoablePropReturn propReturnFunction = propLoadMap.get(putTitle);
                if(propReturnFunction != null) {
                    gProp propToLoad = propReturnFunction.getProp(args);
                    propToLoad.put("id", cScripts.createID(8));
                    propToLoad.putInt("tag", scene.teleportersMap().size());
                    propToLoad.putInt("native", 1);
                    scene.props().add(propToLoad);
                    propReturnFunction.storeProp(propToLoad, scene);
//                    scene.teleporters().add((gPropTeleporter) propToLoad);
//                    scene.teleportersMap().put(propToLoad.get("id"), propToLoad);
                }
                if(lineToks[0].toLowerCase().equals("cmd")) {
                    if(lineToks.length > 1) {
                        xCon.ex(line.replaceFirst("cmd ", ""));
                        execLines.add(line);
                    }
                }
                else if (lineToks[0].toLowerCase().equals("tile")) {
                    gTile tile = new gTile(
                        Integer.valueOf(lineToks[4]),
                        Integer.valueOf(lineToks[5]),
                        Integer.valueOf(lineToks[6]),
                        Integer.valueOf(lineToks[7]),
                        Integer.valueOf(lineToks[8]),
                        Integer.valueOf(lineToks[9]),
                        Integer.valueOf(lineToks[10]),
                        Integer.valueOf(lineToks[11]),
                        Integer.valueOf(lineToks[12]),
                        Integer.valueOf(lineToks[13]),
                        Integer.valueOf(lineToks[14]),
                        eUtils.getPath(lineToks[1]),
                        eUtils.getPath(lineToks[2]),
                        eUtils.getPath(lineToks[3]),
                        Integer.valueOf(lineToks[15]),
                        Integer.valueOf(lineToks[16])
                    );
                    tile.putInt("id", scene.tiles().size());
                    scene.tiles().add(tile);
                }
                else if (lineToks[0].toLowerCase().equals("prop")) {
                    gProp prop = new gProp(
                        Integer.valueOf(lineToks[1]),
                        Integer.valueOf(lineToks[2]),
                        Integer.valueOf(lineToks[3]),
                        Integer.valueOf(lineToks[4]),
                        Integer.valueOf(lineToks[5]),
                        Integer.valueOf(lineToks[6]),
                        Integer.valueOf(lineToks[7]));
                    prop.putInt("tag", scene.props().size());
                    prop.putInt("native", 1);
                    scene.props().add(prop);
                }
                else if (lineToks[0].toLowerCase().equals("scorepoint")) {
                    gPropScorepoint prop = new gPropScorepoint(
                            Integer.valueOf(lineToks[1]),
                            Integer.valueOf(lineToks[2]),
                            Integer.valueOf(lineToks[3]),
                            Integer.valueOf(lineToks[4]),
                            Integer.valueOf(lineToks[5]),
                            Integer.valueOf(lineToks[6]));
                    prop.putInt("tag", scene.scorepoints().size());
                    prop.putInt("native", 1);
                    scene.props().add(prop);
                    scene.scorepoints().add(prop);
                }
                else if (lineToks[0].toLowerCase().equals("ballbouncy")) {
                    gPropBallBouncy prop = new gPropBallBouncy(
                            Integer.valueOf(lineToks[1]),
                            Integer.valueOf(lineToks[2]),
                            Integer.valueOf(lineToks[3]),
                            Integer.valueOf(lineToks[4]),
                            Integer.valueOf(lineToks[5]),
                            Integer.valueOf(lineToks[6]));
                    prop.putInt("tag", scene.ballbouncys().size());
                    prop.putInt("native", 1);
                    scene.props().add(prop);
                    scene.ballbouncys().add(prop);
                }
                else if (lineToks[0].toLowerCase().equals("flagblue")) {
                    gPropFlagBlue prop = new gPropFlagBlue(
                            Integer.valueOf(lineToks[1]),
                            Integer.valueOf(lineToks[2]),
                            Integer.valueOf(lineToks[3]),
                            Integer.valueOf(lineToks[4]),
                            Integer.valueOf(lineToks[5]),
                            Integer.valueOf(lineToks[6]));
                    prop.putInt("tag", scene.flagsblue().size());
                    prop.putInt("native", 1);
                    scene.props().add(prop);
                    scene.flagsblue().add(prop);
                }
                else if (lineToks[0].toLowerCase().equals("flagred")) {
                    gPropFlagRed prop = new gPropFlagRed(
                            Integer.valueOf(lineToks[1]),
                            Integer.valueOf(lineToks[2]),
                            Integer.valueOf(lineToks[3]),
                            Integer.valueOf(lineToks[4]),
                            Integer.valueOf(lineToks[5]),
                            Integer.valueOf(lineToks[6]));
                    prop.putInt("tag", scene.flagsred().size());
                    prop.putInt("native", 1);
                    scene.props().add(prop);
                    scene.flagsred().add(prop);
                }
                else if (lineToks[0].toLowerCase().equals("powerup")) {
                    gPropPowerup prop = new gPropPowerup(
                            Integer.valueOf(lineToks[1]),
                            Integer.valueOf(lineToks[2]),
                            Integer.valueOf(lineToks[3]),
                            Integer.valueOf(lineToks[4]),
                            Integer.valueOf(lineToks[5]),
                            Integer.valueOf(lineToks[6]));
                    prop.putInt("tag", scene.powerups().size());
                    prop.putInt("native", 1);
                    scene.props().add(prop);
                    scene.powerups().add(prop);
                }
                else if (lineToks[0].toLowerCase().equals("flare")) {
                    gFlare flare = new gFlare(
                        Integer.valueOf(lineToks[1]),
                        Integer.valueOf(lineToks[2]),
                        Integer.valueOf(lineToks[3]),
                        Integer.valueOf(lineToks[4]),
                        Integer.valueOf(lineToks[5]),
                        Integer.valueOf(lineToks[6]),
                        Integer.valueOf(lineToks[7]),
                        Integer.valueOf(lineToks[8]),
                        Integer.valueOf(lineToks[9]),
                        Integer.valueOf(lineToks[10]),
                        Integer.valueOf(lineToks[11]),
                        Integer.valueOf(lineToks[12])
                    );
                    if(lineToks.length > 13)
                        flare.put("flicker", lineToks[13]);
                    flare.put("tag", Integer.toString(scene.flares().size()));
                    scene.flares().add(flare);
                }
            }
            wasLoaded = 1;
        }
        catch (Exception e) {
            eUtils.echoException(e);
            e.printStackTrace();
            gTextures.clear();
            mapName = "new";
            scene = new gScene();
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
            for(gFlare f : scene.flares()) {
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
