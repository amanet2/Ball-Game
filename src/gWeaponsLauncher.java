public class gWeaponsLauncher extends gWeapon {
    public gWeaponsLauncher() {
        super();
        name = "LAUNCHER";
        dims = new int[]{200,100};
        bulletDims = new int[]{50,50};
        bulletSpritePath = eUtils.getPath("objects/misc/firegreen.png");
        soundFilePath = "sounds/bfg.wav";
        refiredelay = 1000;
        damage = 6000; //damage will come from the pellets spawned in the explosion
        maxAmmo = 3;
        sprite = eUtils.getWeaponScaledSpriteForPath(eUtils.getPath("misc/launcher.png"),dims[0],dims[1]);
        flipdimr = 100;
        flipdiml = 100;
        bulletTtl = 200;
        bulletVel = 20;
    }

    public void fireWeapon(gPlayer p) {
        if(cVars.getInt("weaponstock"+gWeapons.weapon_launcher) > 0) {
            super.fireWeapon(p);
            gBullet b = new gBullet(p.getInt("coordx")+p.getInt("dimw")/2-bulletDims[0]/2,
                    p.getInt("coordy")+p.getInt("dimh")/2-bulletDims[1]/2, bulletDims[0], bulletDims[1], bulletSpritePath, p.getDouble("fv"),
                    damage);
            b.putInt("tag", p.getInt("tag"));
            b.putInt("ttl",bulletTtl);
            b.putInt("src", gWeapons.weapon_launcher);
            b.putInt("anim", gAnimations.ANIM_SPLASH_GREEN);
            eManager.currentMap.scene.bullets().add(b);
            if(p == cGameLogic.getPlayerByIndex(0)) {
                cVars.decrement("weaponstock"+gWeapons.weapon_launcher);
                cVars.putLong("weapontime"+gWeapons.weapon_launcher, System.currentTimeMillis());
            }
        }
    }
}
