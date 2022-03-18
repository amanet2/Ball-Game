import java.awt.*;

public class gWeapon {
    String name;
    Image sprite;
    int maxAmmo;
    int[] dims;
    int[] bulletDims;
    int damage;
    int refiredelay;
    String bulletSpritePath;
    String soundFilePath;
    int flipdimr;
    int flipdiml;
    int bulletTtl;
    int bulletVel;

    public gWeapon() {
    }

    public void setSpriteFromPath(String path) {
        // TODO: make more optimized
        sprite = gTextures.getGScaledImage(path, dims[0], dims[1]);
    }

    public void fireWeapon(gPlayer p, gScene scene) {
        if(p != null && scene != null)
            xCon.ex(String.format("playsound %s 1 %d %d", soundFilePath,p.getInt("coordx"),p.getInt("coordy")));
    }
}