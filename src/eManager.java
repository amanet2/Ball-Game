import java.io.File;
import java.util.Arrays;
import java.util.HashMap;

public class eManager {
	static int mapSelectionIndex = -1;
	static gMap currentMap;
	static String[] mapsSelection = new String[]{};
	static String[] winClipSelection = new String[]{};

	public static void getMapsSelection() {
        File fp = new File(eUtils.getPath(""));
        File[] fpContents = fp.listFiles();
        for(File ffp : fpContents) {
            if(ffp.isFile() && !ffp.getName().toLowerCase().contains(sVars.get("defaultmap").toLowerCase())
                    && ffp.getName().split("\\.")[1].equalsIgnoreCase("map")) {
                mapsSelection = Arrays.copyOf(mapsSelection,mapsSelection.length+1);
                mapsSelection[mapsSelection.length-1] = ffp.getName();
            }
        }
        uiMenus.menuSelection[uiMenus.MENU_MAP].setupMenuItems();
        fp = new File(eUtils.getPath("sounds/win"));
        fpContents = fp.listFiles();
        for(File ffp : fpContents) {
            if(ffp.isFile()) {
                winClipSelection = Arrays.copyOf(winClipSelection,winClipSelection.length+1);
                winClipSelection[winClipSelection.length-1] = ffp.getName();
            }
        }
    }

	public static void updateEntityPositions() {
        for(gPlayer obj : eManager.currentMap.scene.players()) {
            int dx = obj.getInt("coordx") + obj.getInt("vel3") - obj.getInt("vel2");
            int dy = obj.getInt("coordy") + obj.getInt("vel1") - obj.getInt("vel0");

            if(obj.getLong("acceltick") < System.currentTimeMillis()) {
                obj.putLong("acceltick", System.currentTimeMillis()+obj.getInt("accelrate"));
                for (int i = 0; i < 4; i++) {
                    if(obj.isZero("tag")) {
                        if (obj.getInt("mov"+i) > 0)
                            obj.putInt("vel"+i,(Math.min(cVars.getInt("velocityplayer")
                                            + cVars.getInt("speedbonus"),
                                    obj.getInt("vel"+i) + 1 + cVars.getInt("speedbonus"))));
                        if (obj.getInt("mov"+i) < 1)
                            obj.putInt("vel"+i,Math.max(0, obj.getInt("vel"+i) - 1));
                    }
                    else {
                        obj.putInt("vel"+i,
                                Integer.parseInt(nServer.clientArgsMap.get(obj.get("id")).get("vels").split("-")[i]));
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

//        for(gProp obj : eManager.currentMap.scene.ballbouncys()) {
//            int dx = obj.getInt("coordx") + obj.getInt("vel3") - obj.getInt("vel2");
//            int dy = obj.getInt("coordy") + obj.getInt("vel1") - obj.getInt("vel0");
//
//            if(obj.getLong("acceltick") < System.currentTimeMillis()) {
//                obj.putLong("acceltick", System.currentTimeMillis()+obj.getInt("accelrate"));
//                for (int i = 0; i < 4; i++) {
//                    if(obj.isZero("tag")) {
//                        if (obj.getInt("mov"+i) > 0)
//                            obj.putInt("vel"+i,(Math.min(cVars.getInt("velocityplayer")
//                                            + cVars.getInt("speedbonus"),
//                                    obj.getInt("vel"+i) + 1 + cVars.getInt("speedbonus"))));
//                        if (obj.getInt("mov"+i) < 1)
//                            obj.putInt("vel"+i,Math.max(0, obj.getInt("vel"+i) - 1));
//                    }
//                    else {
//                        obj.putInt("vel"+i,
//                                Integer.parseInt(nServer.clientArgsMap.get(obj.get("id")).get("vels").split("-")[i]));
//                    }
//                }
//            }
//
//            if(dx != obj.getInt("coordx") && obj.wontClipOnMove(0,dx)) {
//                obj.putInt("coordx", dx);
//            }
//
//            if(dy != obj.getInt("coordy") && obj.wontClipOnMove(1,dy)) {
//                obj.putInt("coordy", dy);
//            }
//            obj.put("mov0", "0");
//            obj.put("mov1", "0");
//            obj.put("mov2", "0");
//            obj.put("mov3", "0");
//        }

        for(gBullet obj : eManager.currentMap.scene.bullets()) {
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

	public static void setScene() {
		if (cVars.isInt("cammode", gCamera.MODE_TRACKING) && currentMap.scene.players().size() > 0)
            xCon.ex("centercamera");
		else {
		    double rr = Math.random();
		    if(currentMap.scene.flares().size() > 0) {
		        gFlare r = currentMap.scene.flares().get((int)(Math.random() * (currentMap.scene.flares().size()-1)));
		        cVars.putInt("camx", r.getInt("coordx")-sSettings.width/4);
		        cVars.putInt("camy", r.getInt("coordy")-sSettings.height/2);
		    }
            if((rr > 0.5 && currentMap.scene.props().size() > 0)){
                gProp r = eManager.currentMap.scene.props().get((int)(Math.random() * (currentMap.scene.props().size()-1)));
                cVars.putInt("camx", r.getInt("coordx")-sSettings.width/4);
                cVars.putInt("camy", r.getInt("coordy")-sSettings.height/2);
            }
            if((rr > 0.90 || (currentMap.scene.props().size() < 1 && currentMap.scene.flares().size() < 1))
                && currentMap.scene.tiles().size() > 0){
                gTile r = eManager.currentMap.scene.tiles().get((int)(Math.random() * (currentMap.scene.tiles().size()-1)));
                cVars.putInt("camx", r.getInt("coordx")-sSettings.width/4);
                cVars.putInt("camy", r.getInt("coordy")-sSettings.height/2);
            }
            cVars.putInt("cammode", gCamera.MODE_FREE);
        }
	}
}
