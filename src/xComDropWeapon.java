public class xComDropWeapon extends xCom {
    public String doCommand(String fullCommand) {
        gPlayer p = cGameLogic.getUserPlayer();
        if(p != null) {
            String doString = String.format("e_putprop %d %d %d %d %d %d %d",
                    gProp.POWERUP, p.getInt("weapon"), cVars.getInt("weaponstock"+p.get("weapon")),
                    p.getInt("coordx") + p.getInt("dimw"), p.getInt("coordy") + p.getInt("dimh") + 50,
                    gWeapons.weapons_selection[p.getInt("weapon")].dims[0],
                    gWeapons.weapons_selection[p.getInt("weapon")].dims[1]);
            cVars.putInt("weaponstock"+p.get("weapon"), 0);
            cScripts.changeWeapon(gWeapons.Type.NONE.code());
            xCon.ex(doString);
            cVars.put("sendcmd", doString);
        }
        return "drop weapon";
    }
}
