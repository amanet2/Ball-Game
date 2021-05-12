public class xComFireWeapon extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if (toks.length > 2) {
            String id = toks[1];
            int weapon = Integer.parseInt(toks[2]);
            gPlayer player = null;
            if(sSettings.IS_SERVER)
                player = eManager.getPlayerById(id);
            if(sSettings.IS_CLIENT)
                player = cClientLogic.getPlayerById(id);
            if (player != null)
                gWeapons.fromCode(weapon).fireWeapon(player);
            return id + " fired weapon " + weapon;
        }
        return "usage: fireweapon <player_id> <weapon_code>";
    }
}
