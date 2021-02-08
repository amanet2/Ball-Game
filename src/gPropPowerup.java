public class gPropPowerup extends gProp {
    public void propEffect(gPlayer p) {
        int int0 = getInt("int0");
        if(int0 > 0) {
            if (cVars.isZero("gamespawnarmed")) {
                if(cVars.isZero("currentweapon")) {
                    xCon.ex("THING_PLAYER.0.weapon " + int0);
                    cVars.putInt("currentweapon", int0);
                    cPowerups.takepowerupammo(this);
                    xCon.ex("playsound sounds/grenpinpull.wav");
                    cScripts.checkPlayerSpriteFlip(cGameLogic.userPlayer());
                }
                else if(cVars.isInt("currentweapon", int0)
                        && cVars.getInt("weaponstock"+int0) < gWeapons.fromCode(int0).maxAmmo) {
                    cPowerups.takepowerupammo(this);
                }
            }
            else if(cVars.getInt("weaponstock"+int0) < gWeapons.fromCode(int0).maxAmmo){
                cPowerups.takepowerupammo(this);
            }
        }
    }

    public gPropPowerup(int ux, int uy, int x, int y, int w, int h) {
        super(gProp.POWERUP, ux, uy, x, y, w, h);
    }
}
