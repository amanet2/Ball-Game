public class xComDropWeapon extends xCom {
    public String doCommand(String fullCommand) {
        gPlayer p = cGameLogic.getPlayerByIndex(0);
        if(p != null) {
            String doString = String.format("e_putprop %d %d %d %d %d %d %d",
                    gProp.POWERUP, p.getInt("weapon"), cVars.getInt("weaponstock"+p.get("weapon")),
                    p.getInt("coordx") + p.getInt("dimw"), p.getInt("coordy") + p.getInt("dimh") + 50,
                    gWeapons.weapons_selection[p.getInt("weapon")].dims[0],
                    gWeapons.weapons_selection[p.getInt("weapon")].dims[1]);
            cScripts.changeWeapon(gWeapons.weapon_none);
            xCon.ex(doString);
        }
        return "drop weapon";
    }
}
