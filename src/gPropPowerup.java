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
//            else if(powerup_selection[int0].equals("slow") && cVars.isZero("sicknessslow")) {
//                cVars.putInt("velocityplayer", cVars.getInt("velocityplayerbase")/2);
//                xCon.ex("THING_PLAYER.0.sicknessslow 1");
//                xCon.ex("cv_sicknessslow 1");
//            }
//            else if(powerup_selection[int0].equals("fast") && cVars.isZero("sicknessfast")) {
//                cVars.putInt("velocityplayer", cVars.getInt("velocityplayerbase")*2);
//                xCon.ex("THING_PLAYER.0.sicknessfast 1");
//                xCon.ex("sicknessfast 1");
//            }
        }
    }

    public gPropPowerup(int ux, int uy, int x, int y, int w, int h) {
        super(gProp.POWERUP, ux, uy, x, y, w, h);
    }
}
