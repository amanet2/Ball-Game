public class gPropPowerup extends gProp {
    public void propEffect(gPlayer p) {
        int int0 = getInt("int0");
        if(sSettings.net_server && getInt("int0") > 0) {
            if(p.get("id").contains("bot") && p.getLong("powerupsusetime") < System.currentTimeMillis()) {
                //do powerup effect
                p.putLong("powerupsusetime",
                        System.currentTimeMillis()+sVars.getLong("powerupsusetimemax"));
                cScripts.changeBotWeapon(p, getInt("int0"), true);
                put("int0", "0");
            }
        }
//        if(cGameLogic.isUserPlayer(p)) {
//            if (int0 > 0) {
//                if (p.isZero("weapon")) {
//                    xCon.ex("giveweapon " + p.get("id") + " " + int0);
//                    cPowerups.takepowerupammo(this);
//                } else if (p.isInt("weapon", int0)
//                        && cVars.getInt("weaponstock" + int0) < gWeapons.fromCode(int0).maxAmmo) {
//                    cPowerups.takepowerupammo(this);
//                }
//            }
//        }
    }

    public gPropPowerup(int ux, int uy, int x, int y, int w, int h) {
        super(gProps.POWERUP, ux, uy, x, y, w, h);
    }
}
