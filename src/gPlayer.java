import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class gPlayer extends gThing {
    Image spriteHat;
    Image sprite;

    public void fireWeapon() {
        gWeapons.fromCode(getInt("weapon")).fireWeapon(this);
    }

    public boolean canJump() {
        if(cVars.isInt("mapview", gMap.MAP_SIDEVIEW)) {
            for(gTile t : eManager.currentMap.scene.tiles()) {
                if((!(getInt("coordx")+getInt("dimw") < t.getInt("coordx"))
                        && !(getInt("coordx") > t.getInt("coordx")+t.getInt("dimw"))
                        && (Math.abs((getInt("coordy")+getInt("dimh"))-t.getInt("coordy")) < 10)
                        || (Math.abs((getInt("coordy")+getInt("dimh"))-t.getInt("coordy")) < 85)
                        && (t.getInt("dim0h") > 0 || t.getInt("dim3h")>0))
                        && (t.getInt("dim0h") > 0
                        || t.getInt("dim3h") > 0
                        || t.getInt("dim5w") > 0
                        || t.getInt("dim6w") > 0)
                ) {
                    return true;
                }
                if(new Rectangle2D.Double(getInt("coordx")-10,getInt("coordy")-10,getInt("dimw")+20,getInt("dimh")+20).intersects(
                        new Rectangle2D.Double(t.getInt("coordx"),t.getInt("coordy"),t.getInt("dim5w"),t.getInt("dim5h"))
                ) || (new Rectangle2D.Double(getInt("coordx")-10,getInt("coordy")-10,getInt("dimw")+20,getInt("dimh")+20).intersects(
                        new Rectangle2D.Double(t.getInt("coordx"),t.getInt("coordy"),t.getInt("dim6w"),t.getInt("dim6h")))
                )){
                    return true;
                }
            }
            for (gPlayer target : eManager.currentMap.scene.players()) {
                if(!(getInt("coordx")+getInt("dimw") < target.getInt("coordx"))
                        && !(getInt("coordx") > target.getInt("coordx")+target.getInt("dimw"))
                        && Math.abs((getInt("coordy")+getInt("dimh"))-target.getInt("coordy")) < 10) {
                    return true;
                }
            }
        }
        else {
            return true;
        }
        return false;
    }


    public boolean wontClipOnMove(int coord, int coord2) {
        int dx = coord == 0 ? coord2 : getInt("coordx");
        int dy = coord == 1 ? coord2 : getInt("coordy");
        for (gTile target : eManager.currentMap.scene.tiles()) {
            if(willCollideWithinTileAtCoords(target, dx, dy) || willCollideWithinCornerTileAtCoords(target, dx, dy)) {
                if(cVars.getLong("knocksoundtime") < uiInterface.gameTime && !cVars.isOne("suppressknocksound")) {
                    cVars.putLong("knocksoundtime", uiInterface.gameTime + cVars.getInt("knocksoundtimegap"));
                    xCon.ex(String.format("playsound sounds/knock.wav 1 %d %d",
                        getInt("coordx"),getInt("coordy")));
                }
                return false;
            }
        }
        if(cVars.getInt("mapview") == gMap.MAP_SIDEVIEW) {
            for (gPlayer target : eManager.currentMap.scene.players()) {
                if (!(target.isInt("tag", getInt("tag"))) && willCollideWithPlayerAtCoords(target, dx, dy)) {
                    return false;
                }
            }
        }
        if(cVars.getInt("mapview") == gMap.MAP_TOPVIEW && cVars.isOne("collideplayers")) {
            for (gPlayer target : eManager.currentMap.scene.players()) {
                if (!(target.isInt("tag", getInt("tag"))) && willCollideWithPlayerAtCoordsTopDown(target, dx, dy)) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean willCollideWithPropAtCoords(gProp target, int dx, int dy) {
            Shape bounds = new Ellipse2D.Double(
                target.getInt("coordx"),
                target.getInt("coordy"),
                target.getInt("dimw"),
                target.getInt("dimh")
            );
            return bounds.intersects(new Rectangle(dx,dy,getInt("dimw"),getInt("dimh")));
    }

    public boolean willCollideWithPlayerAtCoords(gPlayer target, int dx, int dy) {
        if(getInt("clip") == 1 && cVars.isOne("clipplayer")) {
            Shape bounds = new Rectangle(
                target.getInt("coordx"),
                target.getInt("coordy"),
                target.getInt("dimw"),
                target.getInt("dimh")
            );
            return bounds.intersects(new Rectangle(dx,dy,getInt("dimw"),getInt("dimh")));
        }
        return false;
    }

    public boolean willCollideWithPlayerAtCoordsTopDown(gPlayer target, int dx, int dy) {
        if(getInt("clip") == 1 && cVars.isOne("clipplayer")) {
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

    public void cancelJump() {
        put("mov0", "0");
        xCon.ex("cv_jumping 0");
    }

    public boolean checkBump(Shape bounds, int ystart) {
        if(cVars.getInt("mapview") == gMap.MAP_SIDEVIEW) {
            if(bounds.getBounds().getY() + bounds.getBounds().getHeight() - ystart
                    < bounds.getBounds().getHeight()/2+10) {
                putInt("coordy", (int) (getInt("coordy")
                        - (bounds.getBounds().getY() + bounds.getBounds().getHeight() - ystart)));
                return false;
            }
        }
        return true;
    }

    public boolean bounceWithinBounds(Shape bounds, Rectangle targetbounds,
                                   int xstart, int ystart, int xend, int yend) {
        //bouncing
        //from top left, CL 0-11
        //12 situations: \|/
        //              -> <-
        //               /|\
        // 6 colliding tile components
        int situation = -1;
        if(bounds.intersects(targetbounds)) {
            if(getInt("vel1") > getInt("vel0") && getInt("vel3") > getInt("vel2")) {
                if(getInt("coordy") + getInt("dimh") > ystart && getInt("coordx") < xstart)
                    situation = 11;
                else if(ystart > getInt("coordy") && getInt("coordx") < xend)
                    situation = 0;
            }
            else if(getInt("vel1") > getInt("vel0") && getInt("vel2") > getInt("vel3")) {
                if(getInt("coordy") + getInt("dimh") > ystart && getInt("coordx") > xstart)
                    situation = 3;
                else if(ystart > getInt("coordy") + getInt("dimh"))
                    situation = 2;
            }
            else if(getInt("vel0") > getInt("vel1") && getInt("vel2") > getInt("vel3")) {
                if(getInt("coordy") < yend && getInt("coordx") + getInt("dimw") > xend)
                    situation = 5;
                else if(xend > getInt("coordx") && getInt("coordy")+getInt("dimh") > yend) {
                    situation = 6;
                    if(cVars.getInt("mapview") == gMap.MAP_SIDEVIEW) {
                        cancelJump();
                    }
                }
            }
            else if(getInt("vel0") > getInt("vel1") && getInt("vel3") > getInt("vel2")) {
                if(getInt("coordy")+getInt("dimh") > yend && getInt("coordx")+getInt("dimw") > xstart) {
                    situation = 8;
                    if(cVars.getInt("mapview") == gMap.MAP_SIDEVIEW) {
                        cancelJump();
                    }
                }
                else if(yend > getInt("coordy")+getInt("dimh") && getInt("coordx") < xstart)
                    situation = 9;
            }
            if(getInt("vel1") > getInt("vel0") && getInt("vel3") == 0 && getInt("vel2") == 0)
                situation = 1;
            if((getInt("vel2") > getInt("vel3") && getInt("vel0") == 0 && getInt("vel1") == 0)
                    && getInt("coordx") > xstart)
                situation = 4;
            if(getInt("vel0") > getInt("vel1") && getInt("vel3") == 0 && getInt("vel2") == 0) {
                situation = 7;
                if(cVars.getInt("mapview") == gMap.MAP_SIDEVIEW) {
                    cancelJump();
                }
            }
            if((getInt("vel3") > getInt("vel2") && getInt("vel0") == 0 && getInt("vel1") == 0)
                    && getInt("coordx") < xstart) {
                situation = 10;
            }
        }
        if(situation > -1) {
            switch (situation) {
                case 0:
                case 1:
                case 2:
                    if(cVars.getInt("mapview") != gMap.MAP_SIDEVIEW) {
//                        System.out.println(getInt("vel1"));
//                        if(getInt("vel1") < 3)
                            cVars.put("suppressknocksound", "1");

                        putInt("vel0", getInt("vel1"));
                        put("vel1", "0");
                    }
                    else {
                        cVars.put("suppressknocksound", "1");
                    }
                    break;
                case 3:
                case 5:
                case 4:
                    //check for bump
                    if(!checkBump(bounds, ystart))
                        return false;
                    putInt("vel3", getInt("vel2"));
                    put("vel2", "0");
                    break;
                case 6:
                case 7:
                case 8:
                    //special case for modifier of upward movement
                    putInt("vel1", 2*getInt("vel0")/3);
                    put("vel0", "0");
                    break;
                case 9:
                case 10:
                case 11:
                    //check for bump
                    if(!checkBump(bounds, ystart))
                        return false;
                    putInt("vel2", getInt("vel3"));
                    put("vel3", "0");
                    break;
                default:
                    break;
            }
        }
        return true;
    }

    public boolean bouncesBounds(Shape bounds, gTile target) {
        return bounds.intersects(new Rectangle(target.getInt("coordx"), target.getInt("coordy")+75,
                target.getInt("dim0w"), target.getInt("dim0h")-75))
                || bounds.intersects(new Rectangle(target.getInt("coordx"), target.getInt("coordy")+75+target.getInt("dimh")
                - target.getInt("dim3h")-target.getInt("dim4h"),
                target.getInt("dim3w"), target.getInt("dim3h")-75))
                || bounds.intersects(new Rectangle(target.getInt("coordx"), target.getInt("coordy")
                + target.getInt("dim0h"), target.getInt("dim1w"), target.getInt("dim1h")-100))
                || bounds.intersects(new Rectangle(target.getInt("coordx"),
                target.getInt("coordy")+target.getInt("dimh")-target.getInt("dim4h"),
                target.getInt("dim4w"), target.getInt("dim4h")-100))
                || bounds.intersects(new Rectangle(target.getInt("coordx"), target.getInt("coordy")
                + target.getInt("dim0h"), target.getInt("dim5w"), target.getInt("dim5h")
                + target.getInt("dim3h")))
                || bounds.intersects(new Rectangle(target.getInt("coordx")+target.getInt("dimw")-target.getInt("dim6w"),
                target.getInt("coordy")+target.getInt("dim0h"), target.getInt("dim6w"),
                target.getInt("dim6h")+target.getInt("dim3h")));
    }

    public boolean willCollideWithinCornerTileAtCoords(gTile t, int dx, int dy) {
        Shape bounds = new Ellipse2D.Double(dx, dy, getInt("dimw"), getInt("dimh"));
        if(t.getInt("dim6w") == -1) {  //cornerUR
            Polygon cornerbounds = new Polygon(
                    new int[]{
                            t.getInt("coordx"),
                            t.getInt("coordx") + t.getInt("dimw"),
                            t.getInt("coordx") + t.getInt("dimw")
                    },
                    new int[]{
                            t.getInt("coordy"),
                            t.getInt("coordy"),
                            t.getInt("coordy") + t.getInt("dimh")
                    },
                    3);
            Rectangle pb = bounds.getBounds();
            if(cornerbounds.intersects(pb)) {
                if(getInt("coordy") + getInt("dimh") < cornerbounds.getBounds().getY() + 5) {
                    if(getInt("vel1") > 0) {
                        putInt("vel0", getInt("vel1") - 1);
                        putInt("vel1", 0);
                    }
                }
                else if(getInt("coordx") > cornerbounds.getBounds().getX() + cornerbounds.getBounds().getWidth() - 5) {
                    if(getInt("vel2") > 0) {
                        putInt("vel3", getInt("vel2") - 1);
                        putInt("vel2", 0);
                    }
                }
                else {
                    if (getInt("vel3") > 0) {
                        putInt("vel1", getInt("vel3") - 1);
                        put("vel3", "0");
                    }
                    if (getInt("vel0") > 0) {
                        double mod = 1.5;
                        putInt("vel2", getInt("vel0") - 1);
                        put("vel0", "0");
                    }
                }
                return true;
            }
        }
        if(t.getInt("dim6w") == -2) {  //cornerBR
            Polygon cornerbounds = new Polygon(
                    new int[]{
                            t.getInt("coordx") + t.getInt("dimw"),
                            t.getInt("coordx") + t.getInt("dimw"),
                            t.getInt("coordx")
                    },
                    new int[]{
                            t.getInt("coordy") + 75,
                            t.getInt("coordy") + t.getInt("dimh"),
                            t.getInt("coordy") + t.getInt("dimh")
                    },
                    3);
            Rectangle pb = bounds.getBounds();
            Rectangle cwb = new Rectangle(t.getInt("coordx"), t.getInt("coordy") + t.getInt("dimh"),
                    t.getInt("dimw"),50);
            if(pb.intersects(cwb)) {
                if(getInt("coordy") > cornerbounds.getBounds().getY() + cornerbounds.getBounds().getHeight() + 45) {
                    if(getInt("vel0") > 0) {
                        double mod = 1.5;
                        putInt("vel1", getInt("vel0") - 1);
                        putInt("vel0", 0);
                    }
                }
                return true;
            }
            if(cornerbounds.intersects(pb)) {
                if(getInt("coordx") > cornerbounds.getBounds().getX() + cornerbounds.getBounds().getWidth() - 5) {
                    if(getInt("vel2") > 0) {
                        putInt("vel3", getInt("vel2") - 1);
                        putInt("vel2", 0);
                    }
                }
                else {
                    if (getInt("vel3") > 0) {
                        putInt("vel0", getInt("vel3") - 1);
                        put("vel3", "0");
                    }
                    if (getInt("vel1") > 0) {
                        putInt("vel2", getInt("vel1") - 1);
                        put("vel1", "0");
                    }
                }
                return true;
            }
        }
        if(t.getInt("dim6w") == -3) {  //cornerBL
            Polygon cornerbounds = new Polygon(
                    new int[]{
                            t.getInt("coordx"),
                            t.getInt("coordx") + t.getInt("dimw"),
                            t.getInt("coordx")
                    },
                    new int[]{
                            t.getInt("coordy") + 75,
                            t.getInt("coordy") + t.getInt("dimh"),
                            t.getInt("coordy") + t.getInt("dimh")
                    },
                    3);
            Rectangle pb = bounds.getBounds();
            Rectangle cwb = new Rectangle(t.getInt("coordx"), t.getInt("coordy") + t.getInt("dimh"),
                    t.getInt("dimw"),50);
            if(pb.intersects(cwb)) {
                if(getInt("coordy") > cornerbounds.getBounds().getY() + cornerbounds.getBounds().getHeight() + 45) {
                    if(getInt("vel0") > 0) {
                        double mod = 1.5;
                        putInt("vel1", getInt("vel0") - 1);
                        putInt("vel0", 0);
                    }
                }
                return true;
            }
            if(cornerbounds.intersects(pb)) {
                if(getInt("coordx") < cornerbounds.getBounds().getX() - 5) {
                    if(getInt("vel3") > 0) {
                        putInt("vel2", getInt("vel3") - 1);
                        putInt("vel3", 0);
                    }
                }
                else {
                    if (getInt("vel2") > 0) {
                        putInt("vel0", getInt("vel2") - 1);
                        put("vel2", "0");
                    }
                    if (getInt("vel1") > 0) {
                        putInt("vel3", getInt("vel1") - 1);
                        put("vel1", "0");
                    }
                }
                return true;
            }
        }
        if(t.getInt("dim6w") == -4) {  //cornerUL
            Polygon cornerbounds = new Polygon(
                    new int[]{
                            t.getInt("coordx"),
                            t.getInt("coordx") + t.getInt("dimw"),
                            t.getInt("coordx")
                    },
                    new int[]{
                            t.getInt("coordy"),
                            t.getInt("coordy"),
                            t.getInt("coordy") + t.getInt("dimh")
                    },
                    3);
            Rectangle pb = bounds.getBounds();
            if(cornerbounds.intersects(pb)) {
                if(getInt("coordy") + getInt("dimh") < cornerbounds.getBounds().getY() + 5) {
                    if(getInt("vel1") > 0) {
                        putInt("vel0", getInt("vel1") - 1);
                        putInt("vel1", 0);
                    }
                }
                else if(getInt("coordx") < cornerbounds.getBounds().getX() - 5) {
                    if(getInt("vel3") > 0) {
                        putInt("vel2", getInt("vel3") - 1);
                        putInt("vel3", 0);
                    }
                }
                else{
                    if (getInt("vel0") > 0) {
                        double mod = 1.5;
                        putInt("vel3", getInt("vel0") - 1);
                        put("vel0", "0");
                    }
                    if (getInt("vel2") > 0) {
                        putInt("vel1", getInt("vel2") - 1);
                        put("vel2", "0");
                    }
                }
                return true;
            }
        }
        return false;
    }

    public boolean willCollideWithinTileAtCoords(gTile target, int dx, int dy) {
        int xstart;
        int ystart;
        int xend;
        int yend;
        boolean bounceOverSafe = true;
        if(getInt("clip") == 1 && cVars.isOne("clipplayer")) {
            cVars.put("suppressknocksound", "0");
            Shape bounds = new Ellipse2D.Double(dx, dy, getInt("dimw"), getInt("dimh"));
            Rectangle targetbounds = new Rectangle(target.getInt("coordx"), target.getInt("coordy") + 75,
                    target.getInt("dim0w"), target.getInt("dim0h") - 75);
            xstart = target.getInt("coordx");
            ystart = target.getInt("coordy")+75;
            xend = target.getInt("coordx")+target.getInt("dim0w");
            yend = target.getInt("coordy") + target.getInt("dim0h");
            bounceWithinBounds(bounds, targetbounds, xstart, ystart, xend, yend); //0 (the tile component)
            targetbounds = new Rectangle(target.getInt("coordx"),
                    target.getInt("coordy") + target.getInt("dim0h"),
                    target.getInt("dim1w"), target.getInt("dim1h") - 100);
            xstart = target.getInt("coordx");
            ystart = target.getInt("coordy")+target.getInt("dim0h");
            xend = target.getInt("coordx")+target.getInt("dim1w");
            yend = target.getInt("coordy") + target.getInt("dim0h") + target.getInt("dim1h")-100;
            bounceWithinBounds(bounds, targetbounds, xstart, ystart, xend, yend); // 1
            targetbounds = new Rectangle(target.getInt("coordx"),
                    target.getInt("coordy") + target.getInt("dimh")-target.getInt("dim4h") - target.getInt("dim3h") + 75,
                    target.getInt("dim3w"), target.getInt("dim3h") - 75);
            xstart = target.getInt("coordx");
            ystart = target.getInt("coordy") + target.getInt("dimh") - target.getInt("dim4h") - target.getInt("dim3h") + 75;
            xend = target.getInt("coordx")+target.getInt("dim3w");
            yend = target.getInt("coordy") + target.getInt("dimh") - target.getInt("dim4h");
            bounceWithinBounds(bounds, targetbounds, xstart, ystart, xend, yend); // 3
            targetbounds = new Rectangle(target.getInt("coordx"),
                    target.getInt("coordy") + target.getInt("dimh")-target.getInt("dim4h"),
                    target.getInt("dim4w"), target.getInt("dim4h") - 100);
            xstart = target.getInt("coordx");
            ystart = target.getInt("coordy") + target.getInt("dimh") - target.getInt("dim4h");
            xend = target.getInt("coordx")+target.getInt("dim4w");
            yend = target.getInt("coordy") + target.getInt("dimh") - 100;
            bounceWithinBounds(bounds, targetbounds, xstart, ystart, xend, yend); // 4
            targetbounds = new Rectangle(target.getInt("coordx"),
                    target.getInt("coordy") + target.getInt("dim0h"),
                    target.getInt("dim5w"), target.getInt("dim5h"));
            xstart = target.getInt("coordx");
            ystart = target.getInt("coordy") + target.getInt("dim0h");
            xend = target.getInt("coordx")+target.getInt("dim5w");
            yend = target.getInt("coordy") + target.getInt("dim0h") + target.getInt("dim5h");
            if(!bounceWithinBounds(bounds, targetbounds, xstart, ystart, xend, yend))// 5
                bounceOverSafe = false;
            targetbounds = new Rectangle(target.getInt("coordx") + target.getInt("dimw") - target.getInt("dim6w"),
                    target.getInt("coordy") + target.getInt("dim0h"),
                    target.getInt("dim6w"), target.getInt("dim6h"));
            xstart = target.getInt("coordx") + target.getInt("dimw") - target.getInt("dim6w");
            ystart = target.getInt("coordy") + target.getInt("dim0h");
            xend = target.getInt("coordx") + target.getInt("dimw");
            yend = target.getInt("coordy") + target.getInt("dim0h") + target.getInt("dim6h");
            if(!bounceWithinBounds(bounds, targetbounds, xstart, ystart, xend, yend)) // 6
                bounceOverSafe = false;

            return bounceOverSafe && bouncesBounds(bounds, target);
        }
        return false;
    }

    public void setHatSpriteFromPath(String newpath) {
        put("pathspritehat", newpath);
        spriteHat = gTextures.getScaledImage(get("pathspritehat"), 150, 300);
    }

    public void setSpriteFromPath(String newpath) {
        put("pathsprite", newpath);
        sprite = gTextures.getScaledImage(get("pathsprite"), getInt("dimw"), getInt("dimh"));
    }

    public void dropWeapon() {
        xCon.ex("echo THING_PLAYER.dropweapon is deprecated.  Use global 'dropweapon' command");
    }

    public gPlayer(int x, int y, int w, int h, String tt) {
        super();
        putInt("coordx", x);
        putInt("coordy", y);
        putInt("dimw", w);
        putInt("dimh", h);
        put("id", "");
        put("name", "player");
        put("color", "blue");
        put("accelrate", "100");
        put("clip", "1");
        put("firing", "0");
        put("flashlight", "0");
        put("exitteleportertag", "-1");
        put("pathspritehat", "");
        put("pathsprite", "");
        put("crouch", "0");
        put("weapon", "0");
        put("cooldown", "0");
        put("acceltick", "0");
        put("sicknessfast", "0");
        put("sicknessslow", "0");
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
        put("stockhp", cVars.get("maxstockhp"));
        put("botthinktime", "0");
        put("powerupsusetime", "0");
        put("sendshot", "0");
        put("alive", "0");
        setSpriteFromPath(tt);
        setHatSpriteFromPath(eUtils.getPath("none"));
        registerDoable("dropweapon", new gDoableThing(){
            public void doItem(gThing thing) {
                dropWeapon();
            }
        });
    }
}