import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class eManager {
	static int mapSelectionIndex = -1;
	static gMap currentMap = null;
	static String[] mapsSelection;
	static String[] winClipSelection;
	static String[] prefabSelection;


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
                    if(cClientLogic.isUserPlayer(obj)) {
                        if (obj.getInt("mov"+i) > 0) {
                            obj.putInt("vel" + i, (Math.min(cVars.getInt("velocityplayer"),
                                    obj.getInt("vel" + i) + 1)));
                        }
                        else
                            obj.putInt("vel"+i,Math.max(0, obj.getInt("vel"+i) - 1));
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

        HashMap bulletsMap = currentMap.scene.getThingMap("THING_BULLET");
        for(Object id : bulletsMap.keySet()) {
            gBullet obj = (gBullet) bulletsMap.get(id);
            obj.putInt("coordx", obj.getInt("coordx")
                - (int) (gWeapons.fromCode(obj.getInt("src")).bulletVel*Math.cos(obj.getDouble("fv")+Math.PI/2)));
            obj.putInt("coordy", obj.getInt("coordy")
                - (int) (gWeapons.fromCode(obj.getInt("src")).bulletVel*Math.sin(obj.getDouble("fv")+Math.PI/2)));
        }
        HashMap popupsMap = currentMap.scene.getThingMap("THING_POPUP");
        for(Object id : popupsMap.keySet()) {
            gPopup obj = (gPopup) popupsMap.get(id);
            obj.put("coordx", Integer.toString(obj.getInt("coordx")
                    - (int) (cVars.getInt("velocitypopup")*Math.cos(obj.getDouble("fv")+Math.PI/2))));
            obj.put("coordy", Integer.toString(obj.getInt("coordy")
                    - (int) (cVars.getInt("velocitypopup")*Math.sin(obj.getDouble("fv")+Math.PI/2))));
        }
        checkBulletSplashes();
	}

    public static void checkBulletSplashes() {
        ArrayList bulletsToRemoveIds = new ArrayList<>();
        HashMap<gPlayer, gBullet> bulletsToRemovePlayerMap = new HashMap<>();
        ArrayList<gBullet> pseeds = new ArrayList<>();
        HashMap bulletsMap = currentMap.scene.getThingMap("THING_BULLET");
        for(Object id : bulletsMap.keySet()) {
            gBullet b = (gBullet) bulletsMap.get(id);
            if(System.currentTimeMillis()-b.getLong("timestamp") > b.getInt("ttl")){
                bulletsToRemoveIds.add(id);
//                if (sVars.isOne("vfxenableanimations") && b.getInt("anim") > -1) {
//                    currentMap.scene.getThingMap("THING_ANIMATION").put(
//                            eManager.createId(), new gAnimationEmitter(b.getInt("anim"),
//                                    b.getInt("coordx"), b.getInt("coordy")));
//                }
                //grenade explosion
                if(b.isInt("src", gWeapons.type.LAUNCHER.code())) {
                    pseeds.add(b);
                }
                continue;
            }
            for(String playerId : gScene.getPlayerIds()) {
                gPlayer t = gScene.getPlayerById(playerId);
                if(t != null && t.containsFields(new String[]{"coordx", "coordy"})
                        && b.doesCollideWithPlayer(t) && !b.get("srcid").equals(playerId)) {
                    bulletsToRemovePlayerMap.put(t, b);
                    if(b.isInt("src", gWeapons.type.LAUNCHER.code()))
                        pseeds.add(b);
                }
            }
        }
        if(pseeds.size() > 0) {
            for(gBullet pseed : pseeds)
                gWeaponsLauncher.createGrenadeExplosion(pseed);
        }
        for(Object bulletId : bulletsToRemoveIds) {
            currentMap.scene.getThingMap("THING_BULLET").remove(bulletId);
        }
        for(gPlayer p : bulletsToRemovePlayerMap.keySet()) {
            cClientLogic.playPlayerDeathSound();
            createDamagePopup(p, bulletsToRemovePlayerMap.get(p));
        }
    }

    public static String createId() {
        int min = 11111111;
        int max = 99999999;
        int idInt = new Random().nextInt(max - min + 1) + min;
        return Integer.toString(idInt);
    }

    public static String createBotId() {
        int min = 11111;
        int max = 99999;
        int idInt = new Random().nextInt(max - min + 1) + min;
        return Integer.toString(idInt);
    }

    //call this everytime a bullet intersects a player
    public static void createDamagePopup(gPlayer dmgvictim, gBullet bullet) {
        //get shooter details
        String killerid = bullet.get("srcid");
        //calculate dmg
        int adjusteddmg = bullet.getInt("dmg") - (int)((double)bullet.getInt("dmg")/2
                *((Math.abs(System.currentTimeMillis() - bullet.getLong("timestamp")
        )/(double)bullet.getInt("ttl"))));
        //play animations on all clients
//        if(sVars.isOne("vfxenableanimations") && bullet.getInt("anim") > -1)
//            eManager.currentMap.scene.getThingMap("THING_ANIMATION").put(
//                    createId(), new gAnimationEmitter(gAnimations.ANIM_SPLASH_RED,
//                            bullet.getInt("coordx"), bullet.getInt("coordy")));
        eManager.currentMap.scene.getThingMap("THING_BULLET").remove(bullet.get("id"));
        //handle damage serverside
        if(sSettings.IS_SERVER) {
            String cmdString = "damageplayer " + dmgvictim.get("id") + " " + adjusteddmg + " " + killerid;
            nServer.instance().addNetCmd("server", cmdString);
            nServer.instance().addExcludingNetCmd("server",
                    "spawnpopup " + dmgvictim.get("id") + " -" + adjusteddmg);
        }
    }
}
