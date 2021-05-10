public class xComAttack extends xCom {
    public String doCommand(String fullCommand) {
        gPlayer br = gClientLogic.getUserPlayer();
        int playerWeapon = br.getInt("weapon");
        if(playerWeapon == gWeapons.type.NONE.code()
            || playerWeapon == gWeapons.type.GLOVES.code()
            || cVars.getInt("weaponstock"+playerWeapon) > 0) {
            if(br.getLong("cooldown") < System.currentTimeMillis()) {
                String fireString = "fireweapon " + br.get("id") + " " + playerWeapon;
                cGameLogic.doCommand(fireString);
                br.putLong("cooldown", System.currentTimeMillis()
                        + (long)(gWeapons.fromCode(br.getInt("weapon")).refiredelay));
            }
        }
        else {
            cScripts.changeWeapon(0);
        }
        return "attack";
    }

    public String undoCommand(String fullCommand) {
        return "-attack";
    }
}
