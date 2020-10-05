import java.awt.*;

public class gWeapon {
    String name;
    Image sprite;
    int maxAmmo;
    int[] dims;
    int[] bulletDims;
    int damage;
    int firerate;
    String bulletSpritePath;
    String soundFilePath;
    int flipdimr;
    int flipdiml;
    int bulletTtl;

    public gWeapon() {
    }

    public void setSpriteFromPath(String path) {
        sprite = gTextures.getScaledImage(path, dims[0], dims[1]);
    }

    public void fireWeapon(gPlayer p) {
        xCon.ex(String.format("playsound %s 1 %d %d", soundFilePath,p.getInt("coordx"),p.getInt("coordy")));
        p.put("sendshot", "1");
    }
}