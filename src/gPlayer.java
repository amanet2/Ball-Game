import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

public class gPlayer extends gThing {
    Image sprite;

    public boolean wontClipOnMove(int dx, int dy, gScene scene) {
        for(String id : scene.getThingMap("BLOCK_COLLISION").keySet()) {
            gThing coll = scene.getThingMap("BLOCK_COLLISION").get(id);
            Rectangle2D playerRect = new Rectangle(dx, dy, dims[0], dims[1]);
            Rectangle2D collRect = new Rectangle(coll.coords[0], coll.coords[1], coll.dims[0], coll.dims[1]);
            if(playerRect.intersects(collRect))
                return false;
        }
        for(String id : scene.getThingMap("THING_PLAYER").keySet()) {
            if(this.id.equals(id))
                continue;
            if (willCollideWithPlayerAtCoordsTopDown(scene.getThingMap("THING_PLAYER").get(id), dx, dy)) {
                collidedPlayer = scene.getThingMap("THING_PLAYER").get(id);
                return false;
            }
        }
        return true;
    }

    public boolean willCollideWithPlayerAtCoordsTopDown(gThing target, int dx, int dy) {
        if(target != null ) {
            //check null fields
            Shape bounds = new Rectangle(
                    target.coords[0] + target.dims[0]/4,
                    target.coords[1] + target.dims[1]/4,
                    target.dims[0]/2,
                    target.dims[1]/2
            );
            return bounds.intersects(new Rectangle(dx,dy,dims[0],dims[1]));
        }
        return false;
    }

    public void checkSpriteFlip() {
        boolean a00 = (fv >= 7*Math.PI/4 && fv <= 9*Math.PI/4);
        boolean a03 = (fv >= 1*Math.PI/4 && fv <= 3*Math.PI/4);
        boolean a04 = (fv >= 3*Math.PI/4 && fv <= 5*Math.PI/4);
        boolean a05 = (fv >= 5*Math.PI/4 && fv <= 7*Math.PI/4);

        if(a00 && !spritePath.contains("a00")) {
            setSpriteFromPath(eManager.getPath(String.format("animations/player_%s/a00.png", color)));
        }
        else if(a03 && !spritePath.contains("a03")) {
            setSpriteFromPath(eManager.getPath(String.format("animations/player_%s/a03.png", color)));
            gWeapons.fromCode(weapon).dims[1] = gWeapons.fromCode(weapon).flipdims[1];
            gWeapons.fromCode(weapon).setSpriteFromPath(gWeapons.fromCode(weapon).spritePath);
        }
        else if(a04 && !spritePath.contains("a04")) {
            setSpriteFromPath(eManager.getPath(String.format("animations/player_%s/a04.png", color)));
        }
        else if(a05 && !spritePath.contains("a05")) {
            setSpriteFromPath(eManager.getPath(String.format("animations/player_%s/a05.png", color)));
            String flippedSprite = gWeapons.fromCode(weapon).spritePath.replace(".png", "_flip.png");
            if(gWeapons.fromCode(weapon) != null) {
                gWeapons.fromCode(weapon).dims[1] = gWeapons.fromCode(weapon).flipdims[0];
                gWeapons.fromCode(weapon).setSpriteFromPath(flippedSprite);
            }
        }
    }

    public void setSpriteFromPath(String newpath) {
        spritePath = newpath;
        sprite = gTextures.getGScaledImage(spritePath, dims[0], dims[1]);
    }

    public gPlayer(String playerId, int x, int y) {
        super();
        type = "THING_PLAYER";
        id = playerId;
        coords[0] = x;
        coords[1] = y;
        dims[0] = 200;
        dims[1] = 200;
        setSpriteFromPath(eManager.getPath("animations/player_teal/a03.png"));
    }
}
