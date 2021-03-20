import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class gMap {
    static final int MAP_TOPVIEW = 0;
    static final int MAP_SIDEVIEW = 1;
    static String[] mapview_selection = new String[] {
            "top-down view",
            "side-scrolling view"
    };
    String mapName;
    ArrayList<String> execLines;
    ArrayList<String> mapLines;
    int wasLoaded;
    gScene scene;

    private void basicInit() {
        gTextures.clear();
        execLines = new ArrayList<>();
        mapLines = new ArrayList<>();
        scene = new gScene();
        wasLoaded = 0;
        cVars.put("maploaded", "0");
    }

	public gMap() {
        basicInit();
        mapName = "new";
        cVars.putInt("mapview", sSettings.create_map_mode);
		cVars.putInt("gamemode", cGameMode.DEATHMATCH);
	}

	public static void load(String s) {
        eManager.currentMap = new gMap();
        xCon.instance().debug("Loading Map: " + s);
        long ct = System.currentTimeMillis();
        try (BufferedReader br = new BufferedReader(new FileReader(s))) {
            if(s.contains("/"))
                eManager.currentMap.mapName = s.split("/")[1].split("\\.")[0];
            else
                eManager.currentMap.mapName = s.split("\\.")[0];
            String line;
            while ((line = br.readLine()) != null) {
                xCon.ex(line);
                eManager.currentMap.mapLines.add(line);
            }
            eManager.currentMap.wasLoaded = 1;
            cVars.put("maploaded", "1");
        }
        catch (Exception e) {
            eUtils.echoException(e);
            e.printStackTrace();
        }
        xCon.instance().debug("Loading time: " + (System.currentTimeMillis() - ct));
    }

	public void save(String filename) {
	    System.out.println("SAVED " + filename);
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(sVars.get("datapath") + "/" + filename), StandardCharsets.UTF_8))) {
		    //these three are always here
            writer.write(String.format("cv_mapview %s\n", cVars.get("mapview")));
            writer.write(String.format("cv_gamemode %s\n", cVars.get("gamemode")));
            writer.write(String.format("cv_botbehavior %s\n", cVars.get("botbehavior")));
            //this one is dynamic
            for(String s : execLines) {
                if(!s.contains("cv_mapview") && !s.contains("cv_gamemode") && !s.contains("cv_botbehavior")) {
                    writer.write(s + "\n");
                }
            }
            for(gTile t : scene.tiles()) {
                String str = String.format("puttile %s %s %s %d %d %d %d %d %d %d %d %d %d %d %d\n",
                    t.get("sprite0").replace(xCon.ex("datapath")+"/",""),
                    t.get("sprite1").replace(xCon.ex("datapath")+"/",""),
                    t.get("sprite2").replace(xCon.ex("datapath")+"/",""),
                    t.getInt("coordx"), t.getInt("coordy"), t.getInt("dimw"), t.getInt("dimh"), t.getInt("dim0h"),
                    t.getInt("dim1h"), t.getInt("dim2h"), t.getInt("dim3h"), t.getInt("dim4h"),
                    t.getInt("dim5w"), t.getInt("dim6w"), t.getInt("brightness"));
                writer.write(str);
            }
            for(gProp p : scene.props()) {
                String savetitle = gProps.getTitleForProp(p);
                String str = String.format("putprop %s %d %d %d %d %d %d\n", savetitle, p.getInt("int0"), p.getInt("int1"),
                    p.getInt("coordx"), p.getInt("coordy"), p.getInt("dimw"), p.getInt("dimh"));
                writer.write(str);
            }
            for(gFlare f : scene.flares()) {
                int b = f.getInt("flicker");
                String str = String.format("putflare %d %d %d %d %d %d %d %d %d %d %d %d %d\n", f.getInt("coordx"),
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
