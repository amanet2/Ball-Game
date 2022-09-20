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
                //handle death
                if(player.getDouble("stockhp") < 1) {
                    //more server-side stuff
                    int dcx = player.getInt("coordx");
                    int dcy = player.getInt("coordy");
                    xCon.ex("exec scripts/deleteplayer " + id);
                    nStateMap masterState = nServer.instance().masterStateMap;
                    nState victimState = masterState.get(id);
                    String victimname = victimState.get("name");
                    String vc = victimState.get("color");
                    victimname += ("#"+vc);
                    if(shooterid.length() > 0) {
                        xCon.ex("setvar sv_gamemode " + cClientLogic.gamemode);
                        xCon.ex("exec scripts/handlekill " + id + " " + shooterid);
                    }
                    else
                        xCon.ex("addcomi server echo " + victimname + " exploded");
//                        handle flag carrier dying
                    if(nServer.instance().masterStateMap.get(id).get("flag").equalsIgnoreCase("1")) {
                        nServer.instance().masterStateMap.get(id).put("flag", "0");
                        xCon.ex(String.format("exec scripts/putflag %d %d %d", cServerLogic.getNewItemId(), dcx, dcy));
                    }
                    //migrate all client death logic here
                    cServerLogic.timedEvents.put(Long.toString(gTime.gameTime + cServerLogic.respawnwaittime),
                        new gTimeEvent() {
                            public void doCommand() {
                                xCon.ex(String.format("exec scripts/respawnnetplayer %s", id));
                            }
                        }
                    );
                    int animInd = gAnimations.ANIM_EXPLOSION_REG;
                    String colorName = nServer.instance().masterStateMap.get(id).get("color");
                    if(gAnimations.colorNameToExplosionAnimMap.containsKey(colorName))
                        animInd = gAnimations.colorNameToExplosionAnimMap.get(colorName);
                    xCon.ex(String.format("addcomi server cl_spawnanimation %d %d %d", animInd, dcx, dcy));
                }
                return id + " took " + dmg + " dmg from " + shooterid;
            }
        }
        return "usage: damageplayer <player_id> <dmg_amount> <optional-shooter_id>";
    }
}
