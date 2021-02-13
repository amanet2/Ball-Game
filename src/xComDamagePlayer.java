public class xComDamagePlayer extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if (toks.length > 2) {
            String id = toks[1];
            int dmg = Integer.parseInt(toks[2]);
            gPlayer player = gScene.getPlayerById(id);
            if(player != null) {
                player.putInt("stockhp", player.getInt("stockhp") - dmg);
                player.putLong("hprechargetime", System.currentTimeMillis());
                //shake camera
                if(cGameLogic.isUserPlayer(player)) {
                    cVars.putLong("shaketime", System.currentTimeMillis()+cVars.getInt("shaketimemax"));
                    int shakeintensity = Math.min(cVars.getInt("camshakemax"),
                            cVars.getInt("camshakemax")*(int)((double)dmg/(double)player.getInt("stockhp")));
                    cVars.addIntVal("camx", cVars.getInt("velocitycam")+shakeintensity);
                    cVars.addIntVal("camy", cVars.getInt("velocitycam")+shakeintensity);
                }
                //handle death
                if(player.getInt("stockhp") < 1 && !player.contains("respawntime")) {
                    //user player
                    if(cGameLogic.isUserPlayer(player)) {
                        xCon.ex("dropweapon");
                        cVars.remove("shaketime");
                        cVars.putInt("cammode", gCamera.MODE_FREE);
                        cVars.put("cammov0", "0");
                        cVars.put("cammov1", "0");
                        cVars.put("cammov2", "0");
                        cVars.put("cammov3", "0");
                        player.putInt("alive", 0);
                        player.putLong("respawntime",
                                System.currentTimeMillis() + cVars.getLong("respawnwaittime"));
                        player.put("stockhp", cVars.get("maxstockhp"));
                        player.put("exploded", "0");
                        player.putInt("explodex", cGameLogic.userPlayer().getInt("coordx") - 75);
                        player.putInt("explodey", cGameLogic.userPlayer().getInt("coordy") - 75);
                    }
                    //everyone does this
                    cScripts.playPlayerDeathSound();
                    if(sVars.isOne("vfxenableanimations")) {
                        eManager.currentMap.scene.getThingMap("THING_ANIMATION").put(
                                cScripts.createID(8),
                                new gAnimationEmitter(gAnimations.ANIM_EXPLOSION_REG,
                                        player.getInt("coordx") - 75, player.getInt("coordy") - 75));
                    }
                    player.put("coordx", "-10000");
                    player.put("coordy", "-10000");
                }
                return id + " took " + dmg + " dmg";
            }
        }
        return "usage: damageplayer <player_id> <dmg_amount>";
    }
}
