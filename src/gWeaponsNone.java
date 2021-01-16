public class gWeaponsNone extends gWeapon {
    public gWeaponsNone() {
        super();
        name = "ROCK";
        dims = new int[]{225,150};
        bulletDims = new int[]{150,150};
        soundFilePath = "sounds/splash.wav";
        refiredelay = 500;
        damage = 800;
        maxAmmo = 0;
        bulletSpritePath = eUtils.getPath("misc/rock.png");
//        sprite = eUtils.getWeaponScaledSpriteForPath(eUtils.getPath("misc/glove.png"),dims[0],dims[1]);
        sprite = eUtils.getWeaponScaledSpriteForPath(eUtils.getPath(""),dims[0],dims[1]);
        flipdimr = 225;
        flipdiml = 225;
        bulletTtl = 50;
        bulletVel = 20;
    }

    public void fireWeapon(gPlayer p){
        super.fireWeapon(p);
        gBullet b = new gBullet(p.getInt("coordx")+p.getInt("dimw")/2-bulletDims[0]/2, p.getInt("coordy")+p.getInt("dimh")/2-bulletDims[1]/2,
            bulletDims[0], bulletDims[1], bulletSpritePath, p.getDouble("fv"), damage);
        b.putInt("tag", p.getInt("tag"));
        b.put("srcid", p.get("id"));
        b.putInt("ttl",bulletTtl);
        eManager.currentMap.scene.bullets().add(b);
    }
}
