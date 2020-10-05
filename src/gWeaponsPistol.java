public class gWeaponsPistol extends gWeapon {
    public gWeaponsPistol() {
        super();
        name = "PISTOL";
        dims = new int[]{200,100};
        bulletDims = new int[]{50,50};
        bulletSpritePath = eUtils.getPath("objects/misc/firegreen.png");
        soundFilePath = "sounds/laser.wav";
        firerate = 600;
        damage = 2800;
        maxAmmo = 6;
        sprite = eUtils.getWeaponScaledSpriteForPath(eUtils.getPath("misc/bfg.png"),dims[0],dims[1]);
        flipdimr = 100;
        flipdiml = 100;
        bulletTtl = 600;
    }

    public void fireWeapon(gPlayer p) {
        if(cVars.getInt("weaponstock"+gWeapons.weapon_pistol) > 0) {
            super.fireWeapon(p);
            gBullet b = new gBullet(p.getInt("coordx")+p.getInt("dimw")/2-bulletDims[0]/2,
                p.getInt("coordy")+p.getInt("dimh")/2-bulletDims[1]/2, bulletDims[0], bulletDims[1], bulletSpritePath, p.getDouble("fv"),
                damage);
            b.putInt("tag", p.getInt("tag"));
            b.putInt("ttl",bulletTtl);
            double randomOffset = (Math.random() * ((Math.PI/8))) - Math.PI/16;
            b.putDouble("fv", b.getDouble("fv") + randomOffset);
            b.putInt("anim", gAnimations.ANIM_SPLASH_GREEN);
            eManager.currentMap.scene.bullets().add(b);
            if(p == cGameLogic.getPlayerByIndex(0)) {
                cVars.decrement("weaponstock"+gWeapons.weapon_pistol);
                cVars.putLong("weapontime"+gWeapons.weapon_pistol, System.currentTimeMillis());
            }
        }
    }
}
