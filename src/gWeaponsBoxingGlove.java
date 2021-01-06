public class gWeaponsBoxingGlove extends gWeapon {
    public gWeaponsBoxingGlove() {
        super();
        name = "BOXINGGLOVE";
        dims = new int[]{225,150};
        bulletDims = new int[]{225,150};
        soundFilePath = "sounds/splash.wav";
        firerate = 450;
        damage = 2200;
        maxAmmo = 0;
        bulletSpritePath = eUtils.getPath("misc/glove.png");
        sprite = eUtils.getWeaponScaledSpriteForPath(eUtils.getPath("misc/glove.png"),dims[0],dims[1]);
        flipdimr = 225;
        flipdiml = 225;
        bulletTtl = 75;
    }

    public void fireWeapon(gPlayer p){
        super.fireWeapon(p);
        gBullet b = new gBullet(p.getInt("coordx")+p.getInt("dimw")/2-bulletDims[0]/2, p.getInt("coordy")+p.getInt("dimh")/2-bulletDims[1]/2,
            bulletDims[0], bulletDims[1], bulletSpritePath, p.getDouble("fv"), damage);
        b.putInt("tag", p.getInt("tag"));
        b.putInt("ttl",bulletTtl);
        eManager.currentMap.scene.bullets().add(b);
    }
}
