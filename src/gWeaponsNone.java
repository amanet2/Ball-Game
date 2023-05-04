public class gWeaponsNone extends gWeapon {
    public gWeaponsNone() {
        super("NONE");
        dims = new int[]{225,150};
        soundFilePath = "sounds/splash.wav";
        bulletSpritePath = eManager.getPath("misc/rock.png");
        spritePath = eManager.getPath("");
        sprite = gTextures.getGScaledImage(spritePath, dims[0],dims[1]);
        flipdimr = 225;
        flipdiml = 225;
    }

    public void fireWeapon(gPlayer p, gScene scene) {
        super.fireWeapon(p, scene);
        if(p == null)
            return;
        gBullet b = new gBullet(p.getInt("coordx")+p.getInt("dimw")/2-bulletDims[0]/2, p.getInt("coordy")+p.getInt("dimh")/2-bulletDims[1]/2,
            bulletDims[0], bulletDims[1], bulletSpritePath, p.getDouble("fv"), damage);
        double randomOffset = (Math.random() * ((Math.PI/10))) - Math.PI/20;
        b.putDouble("fv", b.getDouble("fv") + randomOffset);
        b.put("srcid", p.get("id"));
        b.putInt("ttl",bulletTtl);
        scene.getThingMap("THING_BULLET").put(b.get("id"), b);
    }
}
