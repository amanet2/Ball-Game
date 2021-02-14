public class xComDamagePlayer extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if (toks.length > 2) {
            String id = toks[1];
            int dmg = Integer.parseInt(toks[2]);
            gPlayer player = gScene.getPlayerById(id);
            if(player != null) {
                if(sSettings.net_server) {
                    player.putInt("stockhp", player.getInt("stockhp") - dmg);
                    nServer.clientArgsMap.get(id).put("stockhp", player.get("stockhp"));
                }
                player.putLong("hprechargetime", System.currentTimeMillis());
                return id + " took " + dmg + " dmg";
            }
        }
        return "usage: damageplayer <player_id> <dmg_amount>";
    }
}
