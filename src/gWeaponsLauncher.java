public class gWeaponsLauncher extends gWeapon {
    public gWeaponsLauncher() {
        super("LAUNCHER");
        dims = new int[]{200,100};
        bulletSpritePath = eManager.getPath("objects/misc/firegreen.png");
        soundFilePath = "sounds/bfg.wav";
        spritePath = eManager.getPath("misc/launcher.png");
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
        b.put("srcid", p.get("id"));
        b.putInt("ttl",bulletTtl);
        b.putInt("src", gWeapons.launcher);
        b.putInt("anim", gAnimations.ANIM_SPLASH_GREEN);
        scene.getThingMap("THING_BULLET").put(b.get("id"), b);
    }

    public static void createGrenadeExplosion(gBullet seed) {
        //launcher explosion
        for (int i = 0; i < 8; i++) {
            gBullet g = new gBullet(seed.getInt("coordx"),seed.getInt("coordy"), 300, 300,
                    seed.get("sprite"), 0,
                    gWeapons.fromCode(gWeapons.launcher).damage);
            double randomOffset = (Math.random() * ((Math.PI / 8))) - Math.PI / 16;
            g.putDouble("fv", g.getDouble("fv")+(i * (2.0*Math.PI/8.0) - Math.PI / 16 + randomOffset));
            g.putInt("ttl",75);
            g.put("srcid", seed.get("srcid"));
            g.putInt("anim", gAnimations.ANIM_SPLASH_ORANGE);
            if(sSettings.IS_SERVER && sSettings.IS_CLIENT) {
                xMain.shellLogic.serverScene.getThingMap("THING_BULLET").put(g.get("id"), g);
                cClientLogic.scene.getThingMap("THING_BULLET").put(g.get("id"), g);
            }
            else if(sSettings.IS_SERVER)
                xMain.shellLogic.serverScene.getThingMap("THING_BULLET").put(g.get("id"), g);
            else if(sSettings.IS_CLIENT)
                cClientLogic.scene.getThingMap("THING_BULLET").put(g.get("id"), g);
        }
    }
}
