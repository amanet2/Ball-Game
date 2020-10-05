import java.util.concurrent.ThreadLocalRandom;

public class xComRespawn extends xCom {
    public String doCommand(String fullCommand) {
        int canspawn = cScripts.canSpawnPlayer() ? 1 : 0;
        cVars.put("onladder", "0");
        cVars.put("inboost", "0");
        if(canspawn != 0) {
            while(true) {
                int randomNum = ThreadLocalRandom.current().nextInt(0,
                    eManager.currentMap.scene.tiles().size());
                gTile t = eManager.currentMap.scene.tiles().get(randomNum);
                if(t.isOne("canspawn") && !cGameLogic.getPlayerByIndex(0).willCollideWithinTileAtCoords(t,
                    t.getInt("coordx") + t.getInt("dimw")/2 - cGameLogic.getPlayerByIndex(0).getInt("dimw")/2,
                    t.getInt("coordy") + t.getInt("dimh")/2 - cGameLogic.getPlayerByIndex(0).getInt("dimh")/2)) {
                    boolean pass = true;
                    for (gPlayer target : eManager.currentMap.scene.players()) {
                        if (target.getInt("tag") != cGameLogic.getPlayerByIndex(0).getInt("tag") &&
                                cGameLogic.getPlayerByIndex(0).willCollideWithPlayerAtCoords(target, t.getInt("coordx"),
                                        t.getInt("coordy"))) {
                            pass = false;
                            break;
                        }
                    }
                    if(pass) {
                        xCon.ex("THING_PLAYER.0.coordx " + (t.getInt("coordx") + t.getInt("dimw") / 2
                                - cGameLogic.getPlayerByIndex(0).getInt("dimw") / 2));
                        xCon.ex("THING_PLAYER.0.coordy "+ (t.getInt("coordy") + t.getInt("dimh") / 2
                                - cGameLogic.getPlayerByIndex(0).getInt("dimh") / 2));
                        cVars.putInt("weaponstock" + gWeapons.weapon_pistol,
                                gWeapons.weapons_selection[gWeapons.weapon_pistol].maxAmmo);
                        cVars.putInt("weaponstock" + gWeapons.weapon_shotgun,
                                gWeapons.weapons_selection[gWeapons.weapon_shotgun].maxAmmo);
                        cVars.putInt("weaponstock" + gWeapons.weapon_autorifle,
                                gWeapons.weapons_selection[gWeapons.weapon_autorifle].maxAmmo);
                        cVars.putInt("weaponstock" + gWeapons.weapon_launcher,
                                gWeapons.weapons_selection[gWeapons.weapon_launcher].maxAmmo);
                        cVars.put("stockhp", cVars.get("maxstockhp"));
                        xCon.ex("cv_flashlight 0");
                        xCon.ex("cv_sprint 0");
                        xCon.ex("cv_stockspeed cv_maxstockspeed");
                        xCon.ex("centercamera");
                        if(cVars.contains("spawnprotectionmaxtime") && cVars.getInt("spawnprotectionmaxtime") > 0) {
                            cVars.putLong("spawnprotectiontime",
                                    System.currentTimeMillis() + cVars.getInt("spawnprotectionmaxtime"));
                        }
                        break;
                    }
                }
            }
        }
        return "respawned";
    }
}
