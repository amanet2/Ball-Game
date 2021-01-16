public class gWeaponsPistol extends gWeapon {
    public gWeaponsPistol() {
        super();
        name = "PISTOL";
        dims = new int[]{200,100};
        bulletDims = new int[]{75,75};
        bulletSpritePath = eUtils.getPath("objects/misc/firegreen.png");
        soundFilePath = "sounds/laser.wav";
        refiredelay = 300;
        damage = 1400;
        maxAmmo = 18;
        sprite = eUtils.getWeaponScaledSpriteForPath(eUtils.getPath("misc/bfg.png"),dims[0],dims[1]);
        flipdimr = 100;
        flipdiml = 100;
        bulletTtl = 600;
        bulletVel = 60;
    }

    public void fireWeapon(gPlayer p) {
        super.fireWeapon(p);
        gBullet b = new gBullet(p.getInt("coordx")+p.getInt("dimw")/2-bulletDims[0]/2,
            p.getInt("coordy")+p.getInt("dimh")/2-bulletDims[1]/2, bulletDims[0], bulletDims[1], bulletSpritePath, p.getDouble("fv"),
            damage);
        b.putInt("tag", p.getInt("tag"));
        b.put("srcid", p.get("id"));
        b.putInt("ttl",bulletTtl);
        b.putInt("src", gWeapons.weapon_pistol);
        double randomOffset = (Math.random() * ((Math.PI/12))) - Math.PI/24;
        b.putDouble("fv", b.getDouble("fv") + randomOffset);
        b.putInt("anim", gAnimations.ANIM_SPLASH_GREEN);
        eManager.currentMap.scene.bullets().add(b);
        if(p == cGameLogic.getUserPlayer()) {
            cVars.decrement("weaponstock"+gWeapons.weapon_pistol);
            cVars.putLong("weapontime"+gWeapons.weapon_pistol, System.currentTimeMillis());
        }
    }
}
