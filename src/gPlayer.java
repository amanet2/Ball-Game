import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class gPlayer extends gThing {
    gThing collidedPlayer = null;
    String decorationSprite = "null";
    int weapon = gWeapons.none;
    long botThinkTime = 0;
    long botShootTime = 0;

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
            if (willCollideWithPlayerAtCoords(scene.getThingMap("THING_PLAYER").get(id), dx, dy)) {
                collidedPlayer = scene.getThingMap("THING_PLAYER").get(id);
                return false;
            }
        }
        return true;
    }

    public boolean willCollideWithPlayerAtCoords(gThing target, int dx, int dy) {
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
        gPlayer parent = this;
        args.putArg(new gArg("decorationsprite", parent.decorationSprite) {
            public void onChange() {
                parent.decorationSprite = value;
            }
            public String getValue() {
                return parent.decorationSprite;
            }
        });
        args.putArg(new gArg("weapon", Integer.toString(parent.weapon)) {
            public void onChange() {
                parent.weapon = Integer.parseInt(value);
            }
            public String getValue() {
                return Integer.toString(parent.weapon);
            }
        });
    }

    public void draw(Graphics2D g2) {
        Color pc = gColors.getColorFromName("clrp_" + color);
        if (pc != null) {
            int x = coords[0] - dims[0] / 4;
            int y = coords[1] - dims[1] / 4;
            int w = 3 * dims[0] / 2;
            int h = 3 * dims[1] / 2;
            if (sSettings.vfxenableflares)
                dScreenFX.drawFlareFromColor(g2, x, y, w, h, 1, pc, new Color(0, 0, 0, 0));
        }
        //player shadow
        drawRoundShadow(g2);
        //player itself
        g2.drawImage(
                sprite,
                coords[0],
                coords[1],
                null
        );
        if(!decorationSprite.equalsIgnoreCase("null")) {
            g2.drawImage(
                    gTextures.getGScaledImage(eManager.getPath(decorationSprite), 300, 300),
                    coords[0], coords[1] - 2*dims[1]/3,
                    null
            );
        }
        //shading
        if(sSettings.vfxenableshading) {
            GradientPaint df = new GradientPaint(
                    coords[0], coords[1] + 2*dims[1]/3, gColors.getColorFromName("clrw_clear"),
                    coords[0], coords[1] + dims[1], gColors.getColorFromName("clrw_shadow1half")
            );
            g2.setPaint(df);
            g2.fillOval(coords[0], coords[1], dims[0], dims[1]);
        }
        //player weapon
        AffineTransform backup = g2.getTransform();
        AffineTransform a = g2.getTransform();
        a.rotate(fv - Math.PI/2, coords[0] + (float) dims[0] / 2, coords[1] + (float) dims[1] / 2);
        g2.setTransform(a);
        int diff = gWeapons.fromCode(weapon).dims[1] / 2;
        g2.drawImage(
                gWeapons.fromCode(weapon).sprite,
                coords[0] + dims[0]/2,
                coords[1] + dims[1]/2 - diff,
                null
        );
        g2.setTransform(backup);
    }
}
