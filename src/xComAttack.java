public class xComAttack extends xCom {
    public String doCommand(String fullCommand) {
        if(cVars.getInt("currentweapon") == gWeapons.Type.NONE.code()
            || cVars.getInt("currentweapon") == gWeapons.weapon_gloves
            || cVars.getInt("weaponstock"+cVars.getInt("currentweapon")) > 0) {
            xCon.ex("cv_firing 1");
            gPlayer br = eManager.currentMap.scene.players().get(0);
            if(br.getLong("cooldown") < System.currentTimeMillis()) {
                br.fireWeapon();
                br.putLong("cooldown",
                        (System.currentTimeMillis()
                                + (long)(gWeapons.weapons_selection[br.getInt("weapon")].refiredelay *(
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
