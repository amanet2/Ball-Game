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
                return id + " took " + dmg + " dmg";
            }
        }
        return "usage: damageplayer <player_id> <dmg_amount>";
    }
}
