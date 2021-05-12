public class xComDamagePlayer extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if (toks.length > 2) {
            String id = toks[1];
            int dmg = Integer.parseInt(toks[2]);
            String shooterid = "";
            if(toks.length > 3)
                shooterid = toks[3];
            gPlayer player = eManager.getPlayerById(id);
            if(player != null) {
                player.subtractVal("stockhp", dmg);
                player.putLong("hprechargetime", System.currentTimeMillis());
                //store player object's health in outgoing network arg map
                nServer.instance().clientArgsMap.get(id).put("stockhp", player.get("stockhp"));
                //handle death
                if(player.getInt("stockhp") < 1) {
                    //more server-side stuff
                    int dcx = player.getInt("coordx");
                    int dcy = player.getInt("coordy");
                    xCon.ex("removeplayer " + id);
                    eManager.currentMap.scene.getThingMap("THING_PLAYER").remove(id);
                    String victimname = nServer.instance().clientArgsMap.get(id).get("name");
                    if(shooterid.length() > 0) {
                        String killername = nServer.instance().clientArgsMap.get(shooterid).get("name");
                        gScoreboard.incrementScoreFieldById(shooterid, "kills");
                        nServer.instance().addExcludingNetCmd(uiInterface.uuid,
                                "echo " + killername + " killed " + victimname);
                        if (cVars.getInt("gamemode") == cGameLogic.DEATHMATCH)
                            xCon.ex("givepoint " + shooterid);
                    }
                    else
                        nServer.instance().addNetCmd("echo " + victimname + " died");
//                        handle flag carrier dying
                    if(nServer.instance().clientArgsMap.get("server").get("state").equals(id)) {
                        nServer.instance().clientArgsMap.get("server").put("state", "");
                        //this does the same thing as above
                        nServer.instance().addNetCmd(String.format("putitem ITEM_FLAG %d %d", dcx, dcy));
                    }
                    //migrate all client death logic here
                    String animString = "cl_spawnanimation " + gAnimations.ANIM_EXPLOSION_REG
                            + " " + (dcx - 75) + " " + (dcy - 75);
                    //be sure not to send too much in one go, net comms
                    nServer.instance().addExcludingNetCmd("server", animString);
                    nServer.instance().clientArgsMap.get(id).put("respawntime",
                            Long.toString(System.currentTimeMillis() + cVars.getInt("respawnwaittime")));
                    nServer.instance().addNetCmd(id, "cv_cammode " + gCamera.MODE_FREE);
                }
                return id + " took " + dmg + " dmg from " + shooterid;
            }
        }
        return "usage: damageplayer <player_id> <dmg_amount> <optional-shooter_id>";
    }
}
