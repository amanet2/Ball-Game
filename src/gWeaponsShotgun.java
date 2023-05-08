public class gWeaponsShotgun extends gWeapon {
    public gWeaponsShotgun() {
        super("SHOTGUN");
        dims = new int[]{200,100};
        bulletSpritePath = eManager.getPath("objects/misc/fireblue.png");
        soundFilePath = "sounds/shotgun.wav";
        spritePath = eManager.getPath("misc/shotgun.png");
        sprite = gTextures.getGScaledImage(spritePath, dims[0],dims[1]);        flipdimr = 100;
        flipdiml = 100;
    }

    public void fireWeapon(gPlayer p, gScene scene) {
        super.fireWeapon(p, scene);
        if(p == null)
            return;
        int numpellets = 7;
        for (int i = 0; i < numpellets; i++) {
            gBullet b = new gBullet(p.getInt("coordx") + p.getInt("dimw") / 2 - bulletDims[0] / 2,
                p.getInt("coordy") + p.getInt("dimh") / 2 - bulletDims[1] / 2, bulletDims[0], bulletDims[1],
                    eManager.getPath(String.format("objects/misc/fire%s.png", p.get("color"))),
                    p.getDouble("fv"), damage/numpellets);
            b.putInt("ttl",bulletTtl);
            b.put("srcid", p.get("id"));
            b.putInt("src", gWeapons.shotgun);
            double randomOffset = (Math.random() * ((Math.PI / 16)))-Math.PI/32;
            b.putDouble("fv", b.getDouble("fv") + (i*Math.PI/32-(numpellets/2)*Math.PI/32+randomOffset));
            b.putInt("anim", gAnimations.ANIM_SPLASH_BLUE);
            scene.getThingMap("THING_BULLET").put(b.get("id"), b);
        }
    }
}
