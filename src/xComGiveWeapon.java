public class xComGiveWeapon extends xCom {
    public String doCommand(String fullCommand) {
        if(!sSettings.IS_SERVER)
            return "only the server can give weapons";
        String[] toks = fullCommand.split(" ");
        if (toks.length > 2) {
            String id = toks[1];
            int weap = Integer.parseInt(toks[2]);
            if(nServer.instance().hasClient(id)) {
                xCon.ex(String.format("exec scripts/giveweapon %s %d", id, weap));
//                nServer.instance().addNetCmd(id, String.format("cl_setthing THING_PLAYER %s weapon %d", id, weap));
                return "gave weapon" + weap + " to " + id;
            }
        }
        return "usage: giveweapon <player_id> <weapon_code>";
    }
}
