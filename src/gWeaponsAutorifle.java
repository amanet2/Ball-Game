public class gWeaponsAutorifle extends gWeapon {
    public gWeaponsAutorifle() {
        super("AUTORFILE");
        dims = new int[]{200,100};
        bulletSpritePath = eManager.getPath("objects/misc/fireorange.png");
        soundFilePath = "sounds/30cal.wav";
        spritePath = eManager.getPath("misc/autorifle.png");
        sprite = gTextures.getGScaledImage(spritePath, dims[0],dims[1]);
        flipdimr = 100;
        flipdiml = 100;
    }

    public void fireWeapon(gPlayer p, gScene scene) {
        super.fireWeapon(p, scene);
        if(p == null)
            return;
        gBullet b = new gBullet(p.getInt("coordx")+p.getInt("dimw")/2-bulletDims[0]/2,
            p.getInt("coordy")+p.getInt("dimh")/2-bulletDims[1]/2, bulletDims[0], bulletDims[1],
                eManager.getPath(String.format("objects/misc/fire%s.png", p.get("color"))), p.getDouble("fv"), damage);
        b.putInt("ttl",bulletTtl);
        b.putInt("src", gWeapons.autorifle);
        b.put("srcid", p.get("id"));
        double randomOffset = (Math.random() * Math.PI/8) - Math.PI/16;
        b.putDouble("fv", b.getDouble("fv") + randomOffset);
        b.putInt("anim", gAnimations.ANIM_SPLASH_ORANGE);
        scene.getThingMap("THING_BULLET").put(b.get("id"), b);
    }
}
