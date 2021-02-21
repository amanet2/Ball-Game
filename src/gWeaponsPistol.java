public class gWeaponsPistol extends gWeapon {
    public gWeaponsPistol() {
        super();
        name = "PISTOL";
        dims = new int[]{200,100};
        bulletDims = new int[]{75,75};
        bulletSpritePath = eUtils.getPath("objects/misc/firegreen.png");
        soundFilePath = "sounds/laser.wav";
        refiredelay = 300;
        damage = 350;
        maxAmmo = 6;
        sprite = eUtils.getWeaponScaledSpriteForPath(eUtils.getPath("misc/bfg.png"),dims[0],dims[1]);
        flipdimr = 100;
        flipdiml = 100;
        bulletTtl = 560;
        bulletVel = 70;
    }

    public void fireWeapon(gPlayer p) {
        super.fireWeapon(p);
        gBullet b = new gBullet(p.getInt("coordx")+p.getInt("dimw")/2-bulletDims[0]/2,
            p.getInt("coordy")+p.getInt("dimh")/2-bulletDims[1]/2, bulletDims[0], bulletDims[1], bulletSpritePath, p.getDouble("fv"),
            damage);
        b.put("srcid", p.get("id"));
        b.putInt("ttl",bulletTtl);
        b.putInt("src", gWeapons.type.PISTOL.code());
        double randomOffset = (Math.random() * ((Math.PI/10))) - Math.PI/20;
        b.putDouble("fv", b.getDouble("fv") + randomOffset);
        b.putInt("anim", gAnimations.ANIM_SPLASH_GREEN);
        eManager.currentMap.scene.getThingMap("THING_BULLET").put(b.get("id"), b);
        if(p == cGameLogic.userPlayer()) {
            cVars.decrement("weaponstock"+ gWeapons.type.PISTOL.code());
            cVars.putLong("weapontime"+ gWeapons.type.PISTOL.code(), System.currentTimeMillis());
        }
    }
}
