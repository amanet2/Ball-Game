public class gWeaponsShotgun extends gWeapon {
    public gWeaponsShotgun() {
        super();
        name = "SHOTGUN";
        dims = new int[]{200,100};
        bulletDims = new int[]{50,50};
        bulletSpritePath = eUtils.getPath("objects/misc/fireblue.png");
        soundFilePath = "sounds/shotgun.wav";
        refiredelay = 750;
        damage = 9600;
        maxAmmo = 8;
        sprite = eUtils.getWeaponScaledSpriteForPath(eUtils.getPath("misc/shotgun.png"),dims[0],dims[1]);
        flipdimr = 100;
        flipdiml = 100;
        bulletTtl = 150;
        bulletVel = 50;
    }

    public void fireWeapon(gPlayer p) {
        if(cVars.getInt("weaponstock"+gWeapons.weapon_shotgun) > 0) {
            super.fireWeapon(p);
            int numpellets = 7;
            for (int i = 0; i < numpellets; i++) {
                gBullet b = new gBullet(p.getInt("coordx") + p.getInt("dimw") / 2 - bulletDims[0] / 2,
                    p.getInt("coordy") + p.getInt("dimh") / 2 - bulletDims[1] / 2, bulletDims[0], bulletDims[1],
                        bulletSpritePath, p.getDouble("fv"), damage/numpellets);
                b.putInt("ttl",bulletTtl);
                b.putInt("tag", p.getInt("tag"));
                b.putInt("src", gWeapons.weapon_shotgun);
                double randomOffset = (Math.random() * ((Math.PI / 16)))-Math.PI/32;
                b.putDouble("fv", b.getDouble("fv") + (i*Math.PI/32-(numpellets/2)*Math.PI/32+randomOffset));
                b.putInt("anim", gAnimations.ANIM_SPLASH_BLUE);
                eManager.currentMap.scene.bullets().add(b);
            }
            if(p == cGameLogic.getPlayerByIndex(0)) {
                cVars.decrement("weaponstock"+gWeapons.weapon_shotgun);
                cVars.putLong("weapontime"+gWeapons.weapon_shotgun, System.currentTimeMillis());
            }
        }
    }
}
