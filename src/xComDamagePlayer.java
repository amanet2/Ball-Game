import java.util.HashMap;

public class xComDamagePlayer extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if (toks.length > 2) {
            String id = toks[1];
            int dmg = Integer.parseInt(toks[2]);
            String shooterid = "";
            if(toks.length > 3)
                shooterid = toks[3];
            gPlayer player = gScene.getPlayerById(id);
            if(player != null) {
                if(sSettings.net_server) {
                    player.subtractVal("stockhp", dmg);
                    //store player object's health in outgoing network arg map
                    nServer.instance().clientArgsMap.get(id).put("stockhp", player.get("stockhp"));
                    //handle death
                    if(player.getInt("stockhp") < 1 && !player.contains("respawntime")) {
                        //more server-side stuff
                        String victimname = nServer.instance().clientArgsMap.get(id).get("name");
                        if(shooterid.length() > 0) {
//                            String killername = gScene.getPlayerById(shooterid).get("name");
                            String killername = nServer.instance().clientArgsMap.get(shooterid).get("name");
                            cScoreboard.incrementScoreFieldById(shooterid, "kills");
                            nServer.instance().addNetCmd("echo " + killername + " killed " + victimname);
                            if (cVars.getInt("gamemode") == cGameMode.DEATHMATCH) {
                                xCon.ex("givepoint " + shooterid);
                            }
                        }
                        else {
                            nServer.instance().addNetCmd("echo " + victimname + " died");
                        }
                        //handle flag carrier dying
                        if(cVars.isVal("flagmasterid", player.get("id"))) {
                            cVars.put("flagmasterid", "");
//                            eManager.currentMap.scene.setThingMap("PROP_FLAGRED", new HashMap<>());
                            //this does the same thing as above
                            nServer.instance().addNetCmd("clearthingmap PROP_FLAGRED");
                            nServer.instance().addNetCmd(String.format("putprop PROP_FLAGRED 0 0 %d %d 300 300",
                                    player.getInt("coordx"), player.getInt("coordy")));
                        }
                        //migrate all client death logic here
                        String animString = "spawnanimation " + gAnimations.ANIM_EXPLOSION_REG
                                + " " + (player.getInt("coordx") - 75) + " " + (player.getInt("coordy") - 75);
                        //be sure not to send too much in one go, net comms
                        nServer.instance().addNetCmd(animString);
                        if(!id.contains("bot")) {
//                            nServer.instance().addNetCmd(id, "dropweapon"); //putprop here instead
                            nServer.instance().clientArgsMap.get(id).put("respawntime",
                                    Long.toString(System.currentTimeMillis() + cVars.getInt("respawnwaittime")));
                            xCon.ex("removeplayer " + id);
//                            nServer.instance().addNetCmd(id, "cv_cammode " + gCamera.MODE_FREE + ";cv_cammov0 0;cv_cammov1 0;" +
//                                    "cv_cammov2 0;cv_cammov3 0");
//                            nServer.instance().addNetCmd(id, "userplayer coordx -10000;userplayer coordy -10000");
                        }
                        else {
                            player.putInt("coordx", -100000);
                            player.putInt("coordy", -100000);
                        }
                    }
                }
                player.putLong("hprechargetime", System.currentTimeMillis());
                return id + " took " + dmg + " dmg from " + shooterid;
            }
        }
        return "usage: damageplayer <player_id> <dmg_amount> <optional-shooter_id>";
    }
}
