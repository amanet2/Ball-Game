public class xComAttack extends xCom {
    public String doCommand(String fullCommand) {
        int playerWeapon = cGameLogic.userPlayer().getInt("weapon");
        if(playerWeapon == gWeapons.type.NONE.code()
            || playerWeapon == gWeapons.type.GLOVES.code()
            || cVars.getInt("weaponstock"+playerWeapon) > 0) {
            cVars.putInt("firing", 1);
            gPlayer br = cGameLogic.userPlayer();
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
        cVars.putInt("firing", 0);
        return "-attack";
    }
}
