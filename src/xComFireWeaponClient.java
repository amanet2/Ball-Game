public class xComFireWeaponClient extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if (toks.length > 2) {
            String id = toks[1];
            int weapon = Integer.parseInt(toks[2]);
            gWeapons.fromCode(weapon).fireWeapon(cClientLogic.getPlayerById(id), cClientLogic.scene);
            return id + " fired weapon " + weapon;
        }
        return "usage: fireweapon <player_id> <weapon_code>";
    }
}
