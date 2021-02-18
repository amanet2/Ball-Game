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
                    System.out.println(player.getInt("stockhp"));
                    player.putInt("stockhp", player.getInt("stockhp") - dmg);
                    nServer.clientArgsMap.get(id).put("stockhp", player.get("stockhp"));
                    //handle death
                    if(player.getInt("stockhp") < 1 && !player.contains("respawntime")) {
                        //more server-side stuff
                        if(shooterid.length() > 0) {
                            String killername = gScene.getPlayerById(shooterid).get("name");
                            cScoreboard.incrementScoreFieldById(shooterid, "kills");
                            nServer.addSendCmd("echo " + killername + " killed " + player.get("name"));
                            xCon.ex("echo " + killername + " killed " + player.get("name"));
                            if (cVars.getInt("gamemode") == cGameMode.DEATHMATCH) {
                                xCon.ex("givepoint " + shooterid);
                            }
                        }
                        if((cVars.isInt("gamemode", cGameMode.CAPTURE_THE_FLAG)
                                || cVars.isInt("gamemode", cGameMode.FLAG_MASTER))
                                && cVars.isVal("flagmasterid", player.get("id"))) {
                            cVars.put("flagmasterid", "");
                        }
                        //migrate all client death logic here
                        nServer.addSendCmd(id, "dropweapon");
                        nServer.addSendCmd(id, "cv_cammode " + gCamera.MODE_FREE + ";cv_cammov0 0;cv_cammov1 0;" +
                                "cv_cammov2 0;cv_cammov3 0");
                        //console solution
                        nServer.addSendCmd(id, "userplayer respawntime "
                                + (System.currentTimeMillis() + cVars.getLong("respawnwaittime"))
                        + ";userplayer stockhp "+cVars.get("maxstockhp")+";userplayer exploded 0;userplayer explodex "
                        + (cGameLogic.userPlayer().getInt("coordx") - 75) + ";userplayer explodey "
                        + (cGameLogic.userPlayer().getInt("coordy") - 75));
                        //be sure not to send too much in one go
                        nServer.addSendCmd( "spawnanimation " + gAnimations.ANIM_EXPLOSION_REG
                        + " " + (player.getInt("coordx") - 75) + " " + (player.getInt("coordy") - 75));
                        nServer.addSendCmd(id, "userplayer coordx -10000;userplayer coordy -10000");
                    }
                }
                player.putLong("hprechargetime", System.currentTimeMillis());
                return id + " took " + dmg + " dmg from " + shooterid;
            }
        }
        return "usage: damageplayer <player_id> <dmg_amount> <optional-shooter_id>";
    }
}
