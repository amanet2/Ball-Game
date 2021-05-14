public class gWeaponsShotgun extends gWeapon {
    public gWeaponsShotgun() {
        super();
        name = "SHOTGUN";
        dims = new int[]{200,100};
        bulletDims = new int[]{50,50};
        bulletSpritePath = eUtils.getPath("objects/misc/fireblue.png");
        soundFilePath = "sounds/shotgun.wav";
        refiredelay = 750;
        damage = 2100;
        maxAmmo = 2;
        sprite = eUtils.getWeaponScaledSpriteForPath(eUtils.getPath("misc/shotgun.png"),dims[0],dims[1]);
        flipdimr = 100;
        flipdiml = 100;
        bulletTtl = 140;
        bulletVel = 60;
    }

    public void fireWeapon(gPlayer p, gScene scene) {
        super.fireWeapon(p, scene);
        int numpellets = 7;
        for (int i = 0; i < numpellets; i++) {
            gBullet b = new gBullet(p.getInt("coordx") + p.getInt("dimw") / 2 - bulletDims[0] / 2,
                p.getInt("coordy") + p.getInt("dimh") / 2 - bulletDims[1] / 2, bulletDims[0], bulletDims[1],
                    bulletSpritePath, p.getDouble("fv"), damage/numpellets);
            b.putInt("ttl",bulletTtl);
            b.put("srcid", p.get("id"));
            b.putInt("src", gWeapons.type.SHOTGUN.code());
            double randomOffset = (Math.random() * ((Math.PI / 16)))-Math.PI/32;
            b.putDouble("fv", b.getDouble("fv") + (i*Math.PI/32-(numpellets/2)*Math.PI/32+randomOffset));
            b.putInt("anim", gAnimations.ANIM_SPLASH_BLUE);
            scene.getThingMap("THING_BULLET").put(b.get("id"), b);
        }
        if(p == cClientLogic.getUserPlayer()) {
            cVars.decrement("weaponstock"+ gWeapons.type.SHOTGUN.code());
            cVars.putLong("weapontime"+ gWeapons.type.SHOTGUN.code(), System.currentTimeMillis());
        }
    }
}
