public class xComAttack extends xCom {
    public String doCommand(String fullCommand) {
        int playerWeapon = cGameLogic.userPlayer().getInt("weapon");
        if(playerWeapon == gWeapons.type.NONE.code()
            || playerWeapon == gWeapons.type.GLOVES.code()
            || cVars.getInt("weaponstock"+playerWeapon) > 0) {
            xCon.ex("cv_firing 1");
            gPlayer br = cGameLogic.userPlayer();
            if(br.getLong("cooldown") < System.currentTimeMillis()) {
                br.fireWeapon();
                br.putLong("cooldown",
                        (System.currentTimeMillis()
                                + (long)(gWeapons.fromCode(br.getInt("weapon")).refiredelay * (
                                cVars.isOne("sicknessfast") ? 0.5 : cVars.isOne("sicknessslow") ? 2 : 1))));
            }
        }
        else if(cVars.isZero("allowweaponreload") && cVars.isZero("gamespawnarmed")){
            cScripts.changeWeapon(0);
        }
        return "attack";
    }

    public String undoCommand(String fullCommand) {
        xCon.ex("cv_firing 0");
        return "-attack";
    }
}
