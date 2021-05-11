public class xComGiveWeapon extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if (toks.length > 2) {
            String id = toks[1];
            int weapon = Integer.parseInt(toks[2]);
            gPlayer player = eManager.getPlayerById(id);
            if(player != null) {
                player.putInt("weapon", weapon);
                xCon.ex("playsound sounds/grenpinpull.wav");
                cClientLogic.getUserPlayer().checkSpriteFlip();
            }
            return "gave weapon" + weapon + " to " + id;
        }
        return "usage: giveweapon <player_id> <weapon_code>";
    }
}
