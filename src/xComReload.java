public class xComReload extends xCom {
    public String doCommand(String fullCommand) {
        if(cVars.isOne("allowweaponreload")
                && cVars.getInt("currentweapon") != gWeapons.Type.GLOVES.code()
                && cVars.getInt("currentweapon") != gWeapons.Type.NONE.code()
                && cVars.getInt("weaponstock"+cVars.get("currentweapon")) > 0
            && cVars.getInt("weaponstock"+cVars.get("currentweapon"))
                    < gWeapons.weapons_selection[cVars.getInt("currentweapon")].maxAmmo) {
                xCon.ex("playsound sounds/grenpinpull.wav");
                cVars.put("weaponstock"+cVars.get("currentweapon"), "0");
                cVars.putLong("weapontime"+cVars.get("currentweapon"), System.currentTimeMillis());
                cVars.put("reloading", "1");
        }
        return fullCommand;
    }
}