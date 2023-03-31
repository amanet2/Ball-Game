import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

public class gPlayer extends gThing {
    Image sprite;

    public boolean wontClipOnMove(int dx, int dy, gScene scene) {
        for(String id : scene.getThingMap("BLOCK_COLLISION").keySet()) {
            gThing coll = scene.getThingMap("BLOCK_COLLISION").get(id);
            Rectangle2D playerRect = new Rectangle(dx, dy, getInt("dimw"), getInt("dimh"));
            Rectangle2D collRect = new Rectangle(coll.getX(), coll.getY(), coll.getWidth(), coll.getHeight());
            if(playerRect.intersects(collRect))
                return false;
        }
        for(String id : scene.getThingMap("THING_PLAYER").keySet()) {
            if(get("id").equals(id))
                continue;
            if (willCollideWithPlayerAtCoordsTopDown(
                    (gPlayer) scene.getThingMap("THING_PLAYER").get(id), dx, dy))
                return false;
        }
        return true;
    }

    public boolean willCollideWithPlayerAtCoordsTopDown(gPlayer target, int dx, int dy) {
        if(target != null ) {
            //check null fields
            if(!target.containsFields(new String[]{"coordx", "coordy", "dimw", "dimh"}))
                return false;
            Shape bounds = new Rectangle(
                    target.getInt("coordx") + target.getInt("dimw")/4,
                    target.getInt("coordy") + target.getInt("dimh")/4,
                    target.getInt("dimw")/2,
                    target.getInt("dimh")/2
            );
            return bounds.intersects(new Rectangle(dx,dy,getInt("dimw"),getInt("dimh")));
        }
        return false;
    }

    public void checkSpriteFlip() {
        boolean a00 = (getDouble("fv") >= 7*Math.PI/4 && getDouble("fv") <= 9*Math.PI/4);
        boolean a03 = (getDouble("fv") >= 1*Math.PI/4 && getDouble("fv") <= 3*Math.PI/4);
        boolean a04 = (getDouble("fv") >= 3*Math.PI/4 && getDouble("fv") <= 5*Math.PI/4);
        boolean a05 = (getDouble("fv") >= 5*Math.PI/4 && getDouble("fv") <= 7*Math.PI/4);

        if(a00 && !get("pathsprite").contains("a00")) {
            setSpriteFromPath(eUtils.getPath(String.format("animations/player_%s/a00.png", get("color"))));
        }
        else if(a03 && !get("pathsprite").contains("a03")) {
            setSpriteFromPath(eUtils.getPath(String.format("animations/player_%s/a03.png", get("color"))));
            gWeapons.fromCode(getInt("weapon")).dims[1] = gWeapons.fromCode(getInt("weapon")).flipdimr;
            gWeapons.fromCode(getInt("weapon")).setSpriteFromPath(gWeapons.fromCode(getInt("weapon")).spritePath);
        }
        else if(a04 && !get("pathsprite").contains("a04")) {
            setSpriteFromPath(eUtils.getPath(String.format("animations/player_%s/a04.png",get("color"))));
        }
        else if(a05 && !get("pathsprite").contains("a05")) {
            setSpriteFromPath(eUtils.getPath(String.format("animations/player_%s/a05.png",get("color"))));
            String flippedSprite = gWeapons.fromCode(getInt("weapon")).spritePath.replace(".png", "_flip.png");
            if(gWeapons.fromCode(getInt("weapon")) != null) {
                gWeapons.fromCode(getInt("weapon")).dims[1] = gWeapons.fromCode(getInt("weapon")).flipdiml;
                gWeapons.fromCode(getInt("weapon")).setSpriteFromPath(flippedSprite);
            }
        }
    }

    public void setSpriteFromPath(String newpath) {
        put("pathsprite", newpath);
        sprite = gTextures.getGScaledImage(get("pathsprite"), getInt("dimw"), getInt("dimh"));
    }

    public gPlayer(String id, int x, int y) {
        super();
        putInt("coordx", x);
        putInt("coordy", y);
        putInt("dimw", 200);
        putInt("dimh", 200);
        put("id", id);
        put("type", "THING_PLAYER");
        put("acceldelay", "100");
        put("accelrate", "2");
        put("decelrate", "1");
        put("pathsprite", "");
        put("weapon", "0");
        put("decorationsprite", "null");
        put("cooldown", "0");
        put("acceltick", "0");
        put("fv", "0.0");
        put("vel0", "0");
        put("vel1", "0");
        put("vel2", "0");
        put("vel3", "0");
        put("mov0", "0");
        put("mov1", "0");
        put("mov2", "0");
        put("mov3", "0");
        putInt("stockhp", cClientLogic.maxhp);
        setSpriteFromPath(eUtils.getPath("animations/player_red/a03.png"));
    }
}
