public class xComAttack extends xCom {
    public String doCommand(String fullCommand) {
        gPlayer br = cClientLogic.getUserPlayer();
        int brWeap = br.getInt("weapon");
        long currentTime = gTime.gameTime;
        if(br.getLong("cooldown") >= currentTime)
            return "cant attack";
        nClient.instance().addNetCmd("fireweapon " + br.get("id") + " " + brWeap);
        br.putLong("cooldown", currentTime + (long)(gWeapons.fromCode(brWeap).refiredelay));
        return "attack";
    }
}
