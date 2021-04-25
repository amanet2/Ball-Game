import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;

public class eManager {
	static int mapSelectionIndex = -1;
	static gMap currentMap = new gMap();
	static String[] mapsSelection;
	static String[] winClipSelection;
	static String[] prefabSelection;

	public static void loadMap(String mapPath) {
        eManager.currentMap = new gMap();
        cGameLogic.setUserPlayer(null);
        xCon.instance().debug("Loading: " + mapPath);
        long ct = System.currentTimeMillis();
        try (BufferedReader br = new BufferedReader(new FileReader(mapPath))) {
            if(mapPath.contains("/"))
                eManager.currentMap.mapName = mapPath.split("/")[1].split("\\.")[0];
            else
                eManager.currentMap.mapName = mapPath.split("\\.")[0];
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


	public static String[] getFilesSelection(String dirPath) {
	    String[] selectionArray = new String[]{};
        File fp = new File(dirPath);
        File[] fpContents = fp.listFiles();
        for(File ffp : fpContents) {
            if(ffp.isFile()) {
                selectionArray = Arrays.copyOf(selectionArray,selectionArray.length+1);
                selectionArray[selectionArray.length-1] = ffp.getName();
            }
        }
        return selectionArray;
    }

    public static String[] getFilesSelection(String dirPath, String extension) {
        String[] selectionArray = new String[]{};
        File fp = new File(dirPath);
        File[] fpContents = fp.listFiles();
        for(File ffp : fpContents) {
            if(ffp.isFile() && ffp.getName().split("\\.")[1].equalsIgnoreCase(
                    extension.replace(".",""))) {
                selectionArray = Arrays.copyOf(selectionArray,selectionArray.length+1);
                selectionArray[selectionArray.length-1] = ffp.getName();
            }
        }
        return selectionArray;
    }

	public static void updateEntityPositions() {
        for(String id : gScene.getPlayerIds()) {
            gPlayer obj = gScene.getPlayerById(id);
            String[] requiredFields = new String[]{
                    "coordx", "coordy", "vel0", "vel1", "vel2", "vel3", "acceltick", "accelrate", "mov0", "mov1",
                    "mov2", "mov3"};
            //check null fields
            if(!obj.containsFields(requiredFields))
                break;
            int dx = obj.getInt("coordx") + obj.getInt("vel3") - obj.getInt("vel2");
            int dy = obj.getInt("coordy") + obj.getInt("vel1") - obj.getInt("vel0");
            if(obj.getLong("acceltick") < System.currentTimeMillis()) {
                obj.putLong("acceltick", System.currentTimeMillis()+obj.getInt("accelrate"));
                for (int i = 0; i < 4; i++) {
                    //user player
                    if(cGameLogic.isUserPlayer(obj)) {
                        if (obj.getInt("mov"+i) > 0) {
                            obj.putInt("vel" + i, (Math.min((int)(cVars.getInt("velocityplayer")),
                                    obj.getInt("vel" + i) + 1)));
                        }
                        else
                            obj.putInt("vel"+i,Math.max(0, obj.getInt("vel"+i) - 1));
                    }
                    else if(nServer.instance().clientArgsMap.get(obj.get("id")).containsKey("vels")){
                        obj.putInt("vel"+i,
                                Integer.parseInt(nServer.instance().clientArgsMap.get(obj.get("id")).get("vels").split("-")[i]));
                    }
                }
            }

            if(dx != obj.getInt("coordx") && obj.wontClipOnMove(0,dx)) {
                obj.putInt("coordx", dx);
            }

            if(dy != obj.getInt("coordy") && obj.wontClipOnMove(1,dy)) {
                obj.putInt("coordy", dy);
            }
        }

        HashMap bulletsMap = eManager.currentMap.scene.getThingMap("THING_BULLET");
        for(Object id : bulletsMap.keySet()) {
            gBullet obj = (gBullet) bulletsMap.get(id);
            obj.putInt("coordx", obj.getInt("coordx")
                - (int) (gWeapons.fromCode(obj.getInt("src")).bulletVel*Math.cos(obj.getDouble("fv")+Math.PI/2)));
            obj.putInt("coordy", obj.getInt("coordy")
                - (int) (gWeapons.fromCode(obj.getInt("src")).bulletVel*Math.sin(obj.getDouble("fv")+Math.PI/2)));
        }
        HashMap popupsMap = eManager.currentMap.scene.getThingMap("THING_POPUP");
        for(Object id : popupsMap.keySet()) {
            gPopup obj = (gPopup) popupsMap.get(id);
            obj.put("coordx", Integer.toString(obj.getInt("coordx")
                    - (int) (cVars.getInt("velocitypopup")*Math.cos(obj.getDouble("fv")+Math.PI/2))));
            obj.put("coordy", Integer.toString(obj.getInt("coordy")
                    - (int) (cVars.getInt("velocitypopup")*Math.sin(obj.getDouble("fv")+Math.PI/2))));
        }
	}
}
