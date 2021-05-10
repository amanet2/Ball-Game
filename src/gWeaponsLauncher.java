public class gWeaponsLauncher extends gWeapon {
    public gWeaponsLauncher() {
        super();
        name = "LAUNCHER";
        dims = new int[]{200,100};
        bulletDims = new int[]{50,50};
        bulletSpritePath = eUtils.getPath("objects/misc/firegreen.png");
        soundFilePath = "sounds/bfg.wav";
        refiredelay = 1000;
        damage = 1500; //damage will come from the pellets spawned in the explosion
        maxAmmo = 1;
        sprite = eUtils.getWeaponScaledSpriteForPath(eUtils.getPath("misc/launcher.png"),dims[0],dims[1]);
        flipdimr = 100;
        flipdiml = 100;
        bulletTtl = 180;
        bulletVel = 30;
    }

    public void fireWeapon(gPlayer p) {
        super.fireWeapon(p);
        gBullet b = new gBullet(p.getInt("coordx")+p.getInt("dimw")/2-bulletDims[0]/2,
                p.getInt("coordy")+p.getInt("dimh")/2-bulletDims[1]/2, bulletDims[0], bulletDims[1],
                bulletSpritePath, p.getDouble("fv"), damage);
        b.put("srcid", p.get("id"));
        b.putInt("ttl",bulletTtl);
        b.putInt("src", gWeapons.type.LAUNCHER.code());
        b.putInt("anim", gAnimations.ANIM_SPLASH_GREEN);
        eManager.currentMap.scene.getThingMap("THING_BULLET").put(b.get("id"), b);
        if(p == gClientLogic.getUserPlayer()) {
            cVars.decrement("weaponstock"+ gWeapons.type.LAUNCHER.code());
            cVars.putLong("weapontime"+ gWeapons.type.LAUNCHER.code(), System.currentTimeMillis());
        }
    }

    public static void createGrenadeExplosion(gBullet seed) {
        //launcher explosion
        for (int i = 0; i < 8; i++) {
            gBullet g = new gBullet(seed.getInt("coordx"),seed.getInt("coordy"), 300, 300,
                    eUtils.getPath("objects/misc/fireorange.png"), 0,
                    gWeapons.get(gWeapons.type.LAUNCHER).damage);
            double randomOffset = (Math.random() * ((Math.PI / 8))) - Math.PI / 16;
            g.putDouble("fv", g.getDouble("fv")+(i * (2.0*Math.PI/8.0) - Math.PI / 16 + randomOffset));
            g.putInt("ttl",75);
            g.put("srcid", seed.get("srcid"));
            g.putInt("anim", gAnimations.ANIM_SPLASH_ORANGE);
            g.putInt("isexplosionpart",1);
            eManager.currentMap.scene.getThingMap("THING_BULLET").put(g.get("id"), g);
        }
    }
}
