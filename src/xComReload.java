public class xComReload extends xCom {
    public String doCommand(String fullCommand) {
        if(cVars.isOne("allowweaponreload")
                && cVars.getInt("currentweapon") != gWeapons.type.GLOVES.code()
                && cVars.getInt("currentweapon") != gWeapons.type.NONE.code()
                && cVars.getInt("weaponstock"+cVars.get("currentweapon")) > 0
                && cVars.getInt("weaponstock"+cVars.get("currentweapon"))
                    < gWeapons.fromCode(cVars.getInt("currentweapon")).maxAmmo) {
                xCon.ex("playsound sounds/grenpinpull.wav");
                cVars.put("weaponstock"+cVars.get("currentweapon"), "0");
                cVars.putLong("weapontime"+cVars.get("currentweapon"), System.currentTimeMillis());
                cVars.put("reloading", "1");
        }
        return fullCommand;
    }
}