import java.awt.*;
import java.awt.geom.Rectangle2D;

public class gPlayer extends gThing {
    Image sprite;

    public boolean wontClipOnMove(int coord, int coord2, gScene scene) {
        int dx = coord == 0 ? coord2 : getInt("coordx");
        int dy = coord == 1 ? coord2 : getInt("coordy");
        for(String id : scene.getThingMap("BLOCK_COLLISION").keySet()) {
            gBlockCollision coll = (gBlockCollision) scene.getThingMap("BLOCK_COLLISION").get(id);
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

    public boolean willCollideWithThingAtCoords(gThing target, int dx, int dy) {
        Shape bounds = new Rectangle(
                target.getInt("coordx"),
                target.getInt("coordy"),
                target.getInt("dimw"),
                target.getInt("dimh")
        );
        return bounds.intersects(new Rectangle(dx,dy,getInt("dimw"),getInt("dimh")));
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

    public void pointAtCoords(int x, int y) {
        double dx = x - getInt("coordx") + (float) getInt("dimw")/2;
        double dy = y - getInt("coordy") + (float) getInt("dimh")/2;
        double angle = Math.atan2(dy, dx);
        if (angle < 0)
            angle += 2*Math.PI;
        angle += Math.PI/2;
        putDouble("fv", angle);
        checkSpriteFlip();
    }

    public void checkSpriteFlip() {
        if((getDouble("fv") >= 7*Math.PI/4 && getDouble("fv") <= 9*Math.PI/4)) {
            if(!get("pathsprite").contains("a00")) {
                setSpriteFromPath(eUtils.getPath(String.format("animations/player_%s/a00.png", get("color"))));
            }
        }
        else if((getDouble("fv") <= 3*Math.PI/4)
                || (getDouble("fv") >= 2*Math.PI || getDouble("fv") <= 3*Math.PI/4)) {
            if(!get("pathsprite").contains("a03")) {
                setSpriteFromPath(eUtils.getPath(String.format("animations/player_%s/a03.png",get("color"))));
                String sprite = isInt("weapon", gWeapons.type.AUTORIFLE.code()) ? "misc/autorifle.png" :
                        isInt("weapon", gWeapons.type.SHOTGUN.code()) ? "misc/shotgun.png" :
                                isInt("weapon", gWeapons.type.GLOVES.code()) ? "misc/glove.png" :
                                        isInt("weapon", gWeapons.type.NONE.code()) ? "" :
                                                isInt("weapon", gWeapons.type.LAUNCHER.code()) ? "misc/launcher.png" :
                                                        "misc/bfg.png";
                gWeapons.fromCode(getInt("weapon")).dims[1] =
                        gWeapons.fromCode(getInt("weapon")).flipdimr;
                gWeapons.fromCode(getInt("weapon")).setSpriteFromPath(eUtils.getPath(sprite));
            }
        }
        else if(getDouble("fv") <= 5*Math.PI/4) {
            if(!get("pathsprite").contains("a04")) {
                setSpriteFromPath(eUtils.getPath(String.format("animations/player_%s/a04.png",get("color"))));
            }
        }
        else {
            if(!get("pathsprite").contains("a05")) {
                setSpriteFromPath(eUtils.getPath(String.format("animations/player_%s/a05.png",get("color"))));
                String sprite = isInt("weapon", gWeapons.type.AUTORIFLE.code()) ? "misc/autorifle_flip.png" :
                        isInt("weapon", gWeapons.type.SHOTGUN.code()) ? "misc/shotgun_flip.png" :
                                isInt("weapon", gWeapons.type.GLOVES.code()) ? "misc/glove_flip.png" :
                                        isInt("weapon", gWeapons.type.NONE.code()) ? "" :
                                                isInt("weapon", gWeapons.type.LAUNCHER.code()) ? "misc/launcher_flip.png" :
                                                        "misc/bfg_flip.png";
                if(gWeapons.fromCode(getInt("weapon")) != null) {
                    gWeapons.fromCode(getInt("weapon")).dims[1] =
                            gWeapons.fromCode(getInt("weapon")).flipdiml;
                    gWeapons.fromCode(getInt("weapon")).setSpriteFromPath(eUtils.getPath(sprite));
                }
            }
        }
    }

    public void setSpriteFromPath(String newpath) {
        put("pathsprite", newpath);
        sprite = gTextures.getGScaledImage(get("pathsprite"), getInt("dimw"), getInt("dimh"));
    }

    public gPlayer(String id, int x, int y, int health, String tt) {
        super();
        putInt("coordx", x);
        putInt("coordy", y);
        putInt("dimw", 200);
        putInt("dimh", 200);
        put("id", id);
        put("type", "THING_PLAYER");
        put("inteleporter", "0");
        put("accelrate", "100");
        put("pathsprite", "");
        put("weapon", "0");
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
        put("hprechargetime", "0");
        putInt("stockhp", health);
        put("botthinktime", "0");
        setSpriteFromPath(tt);
    }
}
