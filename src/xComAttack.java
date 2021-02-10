public class xComAttack extends xCom {
    public String doCommand(String fullCommand) {
        gPlayer br = cGameLogic.userPlayer();
        int playerWeapon = br.getInt("weapon");
        if(playerWeapon == gWeapons.type.NONE.code()
            || playerWeapon == gWeapons.type.GLOVES.code()
            || cVars.getInt("weaponstock"+playerWeapon) > 0) {
            if(br.getLong("cooldown") < System.currentTimeMillis()) {
                br.fireWeapon();
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
