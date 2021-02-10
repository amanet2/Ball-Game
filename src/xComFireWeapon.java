public class xComFireWeapon extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if (toks.length > 2) {
            String id = toks[1];
            int weapon = Integer.parseInt(toks[2]);
            gPlayer player = gScene.getPlayerById(id);
            if(player != null) {
                gWeapons.fromCode(weapon).fireWeapon(player);
                cVars.put("sendcmd", "fireweapon " + id + " " + weapon);
            }
            return id + " fired weapon " + weapon;
        }
        return "usage: fireweapon <player_id> <weapon_code>";
    }
}
