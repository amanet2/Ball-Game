public class xComGiveWeapon extends xCom {
    public String doCommand(String fullCommand) {
        if(!sSettings.IS_SERVER)
            return "only the server can give weapons";
        String[] toks = fullCommand.split(" ");
        if (toks.length > 2) {
            String id = toks[1];
            int weapon = Integer.parseInt(toks[2]);
            if(nServer.instance().hasClient(id)) {
                nServer.instance().addNetCmd(id, "userplayer weapon " + weapon);
                return "gave weapon" + weapon + " to " + id;
            }
        }
        return "usage: giveweapon <player_id> <weapon_code>";
    }
}
