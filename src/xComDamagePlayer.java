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
                xCon.ex(String.format("exec scripts/damageplayer %s %d %d", id, dmg, gTime.gameTime));
//                gScoreboard.addToScoreField(id, "score", -dmg);
                //handle death
                if(player.getDouble("stockhp") < 1) {
                    //more server-side stuff
                    int dcx = player.getInt("coordx");
                    int dcy = player.getInt("coordy");
                    xCon.ex("exec scripts/deleteplayer " + id);
                    String victimname = nServer.instance().clientArgsMap.get(id).get("name");
                    String vc = nServer.instance().clientArgsMap.get(id).get("color");
                    victimname += ("#"+vc);
                    if(shooterid.length() > 0) {
                        String killername = nServer.instance().clientArgsMap.get(shooterid).get("name");
                        if(gColors.instance().getColorFromName("clrp_" + nServer.instance().clientArgsMap.get(shooterid).get("color")) != null)
                            killername += ("#"+nServer.instance().clientArgsMap.get(shooterid).get("color"));
                        xCon.ex("addcomi server echo " + killername + " rocked " + victimname);
                        if (cGameLogic.isGame(cGameLogic.DEATHMATCH))
                            xCon.ex("givepoint " + shooterid + " 500");
                        else if (cGameLogic.isGame(cGameLogic.VIRUS)) {
                            if(nServer.instance().clientArgsMap.get("server").containsKey("virusids")) {
                                String virusids = nServer.instance().clientArgsMap.get("server").get("virusids");
                                if(!virusids.contains(id)) {
                                    xCon.ex("setnargs server virusids " + virusids + ":" + id);
                                    xCon.ex("addcomi server echo " + victimname + " was infected");
                                }
                            }
                        }
                    }
                    else
                        xCon.ex("addcomi server echo " + victimname + " exploded");
//                        handle flag carrier dying
                    if(nServer.instance().clientArgsMap.get("server").containsKey("flagmasterid")
                    && nServer.instance().clientArgsMap.get("server").get("flagmasterid").equals(id)) {
                        nServer.instance().clientArgsMap.get("server").remove("flagmasterid");
                        int itemId = 0;
                        for(String iid : cServerLogic.scene.getThingMap("THING_ITEM").keySet()) {;
                            if(itemId < Integer.parseInt(iid))
                                itemId = Integer.parseInt(iid);
                        }
                        itemId++; //want to be the _next_ id
                        xCon.ex(String.format("exec scripts/putflag %d %d %d", itemId, dcx, dcy));
                    }
                    //migrate all client death logic here
                    int animInd = gAnimations.ANIM_EXPLOSION_REG;
                    String colorName = nServer.instance().clientArgsMap.get(id).get("color");
                    if(gAnimations.colorNameToExplosionAnimMap.containsKey(colorName))
                        animInd = gAnimations.colorNameToExplosionAnimMap.get(colorName);
                    xCon.ex(String.format("exec scripts/postdeath %s %d %s", id,
                            gTime.gameTime + cServerLogic.respawnwaittime,
                            String.format("cl_spawnanimation %d %d %d", animInd, dcx - 100, dcy - 100)));
                }
                return id + " took " + dmg + " dmg from " + shooterid;
            }
        }
        return "usage: damageplayer <player_id> <dmg_amount> <optional-shooter_id>";
    }
}
