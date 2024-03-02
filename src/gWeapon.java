import java.awt.Image;

public class gWeapon {
    gArgSet argSet;
    String spritePath;
    Image sprite;
    int[] dims;
    int[] bulletDims;
    int damage;
    int refiredelay;
    String bulletSpritePath;
    String soundFilePath;
    int[] flipdims;
    int bulletTtl;
    int bulletVel;

    public gWeapon(String title, int[] dims, String bulletSpritePath, String soundFilePath, String spritePath, int[] flipdims) {
        argSet = new gArgSet();
        argSet.putArg(new gArg("dmg", "0") {
            public void onChange() {
                damage = Integer.parseInt(value);
            }
        });
        argSet.putArg(new gArg("delay", "500") {
            public void onChange() {
                refiredelay = Integer.parseInt(value);
            }
        });
        argSet.putArg(new gArg("vel", "30") {
            public void onChange() {
                bulletVel = Integer.parseInt(value);
            }
        });
        argSet.putArg(new gArg("ttl", "45") {
            public void onChange() {
                bulletTtl = Integer.parseInt(value);
            }
        });
        argSet.putArg(new gArg("rad", "150") {
            public void onChange() {
                int d = Integer.parseInt(value);
                bulletDims = new int[]{d, d};
            }
        });
        for(String s : new String[]{"dmg", "delay", "vel", "ttl", "rad"}) {
            String vk = String.format("setvar WEAPON_%s_%s", title, s);
            String cfg = xMain.shellLogic.console.ex(vk);
            if(!cfg.equalsIgnoreCase("null"))
                argSet.put(s, cfg);
            else
                System.out.println("VALUE IS NULL: " + vk);
        }
        this.dims = dims;
        this.bulletSpritePath =  eManager.getPath(bulletSpritePath);
        this.soundFilePath = soundFilePath;
        this.spritePath = eManager.getPath(spritePath);
        this.setSpriteFromPath(this.spritePath);
        this.flipdims = flipdims;
    }

    public void setSpriteFromPath(String path) {
        sprite = gTextures.getGScaledImage(path, dims[0], dims[1]);
    }

    public void fireWeapon(gPlayer p, gScene scene) {
        if(p != null && scene != null) {
            xMain.shellLogic.console.ex(String.format("playsound %s 0 %d %d", soundFilePath, p.coords[0], p.coords[1]));
            if(p.ammo-- <= 0 && p.weapon != 5) //special case for boxing gloves
                xMain.shellLogic.console.ex(String.format("setnplayer %s weapon 0", p.id));
        }
    }
}