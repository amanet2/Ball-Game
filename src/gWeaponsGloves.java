public class gWeaponsGloves extends gWeapon {
    public gWeaponsGloves() {
        super();
        dims = new int[]{225,150};
        bulletDims = new int[]{300,300};
        soundFilePath = "sounds/splash.wav";
        refiredelay = 450;
        damage = 550;
        bulletSpritePath = eUtils.getPath("misc/glove.png");
        spritePath = eUtils.getPath("misc/glove.png");
        sprite = gTextures.getGScaledImage(spritePath, dims[0],dims[1]);
        flipdimr = 225;
        flipdiml = 225;
        bulletTtl = 50;
        bulletVel = 30;
    }

    public void fireWeapon(gPlayer p, gScene scene) {
        super.fireWeapon(p, scene);
        if(p == null)
            return;
        gBullet b = new gBullet(p.getInt("coordx")+p.getInt("dimw")/2-bulletDims[0]/2, p.getInt("coordy")+p.getInt("dimh")/2-bulletDims[1]/2,
            bulletDims[0], bulletDims[1], bulletSpritePath, p.getDouble("fv"), damage);
        b.put("srcid", p.get("id"));
        b.putInt("ttl",bulletTtl);
        b.putInt("src", gWeapons.gloves);
        scene.getThingMap("THING_BULLET").put(b.get("id"), b);
    }
}
