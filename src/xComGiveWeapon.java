public class xComGiveWeapon extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if (toks.length > 2) {
            String id = toks[1];
            int weapon = Integer.parseInt(toks[2]);
            nServer.instance().addNetCmd(id, "userplayer weapon " + weapon + ";cv_weaponstock" + weapon + " 30");
            return "gave weapon" + weapon + " to " + id;
        }
        return "usage: giveweapon <player_id> <weapon_code>";
    }
}
