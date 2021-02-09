public class gWeaponsAutorifle extends gWeapon {
    public gWeaponsAutorifle() {
        super();
        name = "AUTORIFLE";
        dims = new int[]{200,100};
        bulletDims = new int[]{50,50};
        bulletSpritePath = eUtils.getPath("objects/misc/fireorange.png");
        soundFilePath = "sounds/30cal.wav";
        refiredelay = 80;
        damage = 200;
        maxAmmo = 40;
        sprite = eUtils.getWeaponScaledSpriteForPath(eUtils.getPath("misc/autorifle.png"),dims[0],dims[1]);
        flipdimr = 100;
        flipdiml = 100;
        bulletTtl = 250;
        bulletVel = 60;
    }

    public void fireWeapon(gPlayer p) {
        super.fireWeapon(p);
        gBullet b = new gBullet(p.getInt("coordx")+p.getInt("dimw")/2-bulletDims[0]/2,
            p.getInt("coordy")+p.getInt("dimh")/2-bulletDims[1]/2, bulletDims[0], bulletDims[1],
                bulletSpritePath, p.getDouble("fv"), damage);
        b.putInt("ttl",bulletTtl);
        b.putInt("src", gWeapons.type.AUTORIFLE.code());
        b.put("srcid", p.get("id"));
        double randomOffset = (Math.random() * Math.PI/8) - Math.PI/16;
        b.putDouble("fv", b.getDouble("fv") + randomOffset);
        b.putInt("anim", gAnimations.ANIM_SPLASH_ORANGE);
        eManager.currentMap.scene.getThingMap("THING_BULLET").put(b.get("id"), b);
        if(p == cGameLogic.userPlayer()) {
            cVars.decrement("weaponstock"+ gWeapons.type.AUTORIFLE.code());
            cVars.putLong("weapontime"+ gWeapons.type.AUTORIFLE.code(), System.currentTimeMillis());
        }
    }
}
