public class gPropPowerup extends gProp {
    public void propEffect(gPlayer p) {
        int int0 = getInt("int0");
        if(int0 > 0) {
            if(cVars.isZero("currentweapon")) {
                xCon.ex("giveweapon " + p.get("id") + " " + int0);
                cPowerups.takepowerupammo(this);
            }
            else if(cVars.isInt("currentweapon", int0)
                    && cVars.getInt("weaponstock"+int0) < gWeapons.fromCode(int0).maxAmmo) {
                cPowerups.takepowerupammo(this);
            }
        }
    }

    public gPropPowerup(int ux, int uy, int x, int y, int w, int h) {
        super(gProps.POWERUP, ux, uy, x, y, w, h);
    }
}
