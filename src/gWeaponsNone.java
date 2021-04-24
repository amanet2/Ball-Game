public class gWeaponsNone extends gWeapon {
    public gWeaponsNone() {
        super();
        name = "ROCK";
        dims = new int[]{225,150};
        bulletDims = new int[]{150,150};
        soundFilePath = "sounds/splash.wav";
        refiredelay = 500;
        damage = 200;
        maxAmmo = 0;
        bulletSpritePath = eUtils.getPath("misc/rock.png");
//        sprite = eUtils.getWeaponScaledSpriteForPath(eUtils.getPath("misc/glove.png"),dims[0],dims[1]);
        sprite = eUtils.getWeaponScaledSpriteForPath(eUtils.getPath(""),dims[0],dims[1]);
        flipdimr = 225;
        flipdiml = 225;
        bulletTtl = 45;
        bulletVel = 30;
    }

    public void fireWeapon(gPlayer p){
        super.fireWeapon(p);
        gBullet b = new gBullet(p.getInt("coordx")+p.getInt("dimw")/2-bulletDims[0]/2, p.getInt("coordy")+p.getInt("dimh")/2-bulletDims[1]/2,
            bulletDims[0], bulletDims[1], bulletSpritePath, p.getDouble("fv"), damage);
        double randomOffset = (Math.random() * ((Math.PI/10))) - Math.PI/20;
        b.putDouble("fv", b.getDouble("fv") + randomOffset);
        b.put("srcid", p.get("id"));
        b.putInt("ttl",bulletTtl);
        eManager.currentMap.scene.getThingMap("THING_BULLET").put(b.get("id"), b);
    }
}
