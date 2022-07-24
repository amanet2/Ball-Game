public class xComDamagePlayer extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if (toks.length > 2) {
            String id = toks[1];
            int dmg = Integer.parseInt(toks[2]);
            String shooterid = "";
            if(toks.length > 3)
                shooterid = toks[3];
            gPlayer player = cServerLogic.getPlayerById(id);
            if(player != null) {
                player.subtractVal("stockhp", dmg);
                player.putLong("hprechargetime", gTime.gameTime);
                //store player object's health in outgoing network arg map
                nServer.instance().clientArgsMap.get(id).put("hp", player.get("stockhp"));
                //handle death
                if(player.getInt("stockhp") < 1) {
                    //more server-side stuff
                    int dcx = player.getInt("coordx");
                    int dcy = player.getInt("coordy");
                    xCon.ex("deleteplayer " + id);
                    nServer.instance().addExcludingNetCmd("server", "cl_deleteplayer " + id);
                    String victimname = nServer.instance().clientArgsMap.get(id).get("name");
                    if(shooterid.length() > 0) {
                        String killername = nServer.instance().clientArgsMap.get(shooterid).get("name");
//                        gScoreboard.incrementScoreFieldById(shooterid, "kills");
                        nServer.instance().addExcludingNetCmd("server",
                                "echo " + killername + " rocked " + victimname);
                        if (cGameLogic.isDeathmatch())
                            xCon.ex("givepoint " + shooterid);
                        else if (cGameLogic.isVirus()) {
                            if(nServer.instance().clientArgsMap.get("server").containsKey("virusids")) {
                                String virusids = nServer.instance().clientArgsMap.get("server").get("virusids");
                                if(!virusids.contains(id)) {
                                    nServer.instance().clientArgsMap.get("server").put("virusids", virusids + ":" + id);
                                    nServer.instance().addExcludingNetCmd("server",
                                            String.format("echo %s was infected", victimname));
                                }
                            }
                        }
                    }
                    else
                        nServer.instance().addExcludingNetCmd("server", "echo " + victimname + " died");
//                        handle flag carrier dying
                    if(nServer.instance().clientArgsMap.get("server").containsKey("flagmasterid")
                    && nServer.instance().clientArgsMap.get("server").get("flagmasterid").equals(id)) {
                        nServer.instance().clientArgsMap.get("server").remove("flagmasterid");
                        xCon.ex(String.format("putitem ITEM_FLAG %d %d", dcx, dcy));
                        nServer.instance().addExcludingNetCmd("server",
                                String.format("cl_putitem ITEM_FLAG %d %d", dcx, dcy));
                    }
                    //migrate all client death logic here
                    int animInd = gAnimations.ANIM_EXPLOSION_REG;
                    String colorName = nServer.instance().clientArgsMap.get(id).get("color");
                    if(gAnimations.colorNameToExplosionAnimMap.containsKey(colorName))
                        animInd = gAnimations.colorNameToExplosionAnimMap.get(colorName);
                    String animString = "cl_spawnanimation " + animInd + " " + (dcx - 100) + " " + (dcy - 100);
                    //be sure not to send too much in one go, net comms
                    nServer.instance().addExcludingNetCmd("server", animString);
                    nServer.instance().clientArgsMap.get(id).put("respawntime",
                            Long.toString(gTime.gameTime + cServerLogic.respawnwaittime));
                    nServer.instance().addNetCmd(id, "freecam");
                }
                return id + " took " + dmg + " dmg from " + shooterid;
            }
        }
        return "usage: damageplayer <player_id> <dmg_amount> <optional-shooter_id>";
    }
}
