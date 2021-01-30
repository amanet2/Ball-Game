import java.awt.*;
import java.awt.geom.Ellipse2D;

public class gProp extends gThing {
    Image sprite;

    static final int TELEPORTER = 0;
    static final int LADDER = 2;
    static final int SCOREPOINT = 3;
    static final int FLAGRED = 5;
    static final int FLAGBLUE = 6;
    static final int POWERUP = 7;
    static final int BOOSTUP = 8;
    static final int BALLBOUNCY = 9;

    public boolean isType(int code) {
        return isInt("code", code);
    }

    public static String getSaveStringForProp(gProp prop) {
        switch (prop.getInt("code")) {
            case TELEPORTER:
                return "teleporter";
            case LADDER:
                return "ladder";
            case SCOREPOINT:
                return "scorepoint";
            case FLAGRED:
                return "flagred";
            case FLAGBLUE:
                return "flagblue";
            case POWERUP:
                return "powerup";
            case BOOSTUP:
                return "boostup";
            case BALLBOUNCY:
                return "ballbouncy";
            default:
                return "prop 0";
        }
    }

    public gProp load(String[] args) {
        return null;
    }

    public void propEffect(gPlayer p) {
        xCon.ex("echo Unspecified prop effect for prop tag/code: "+get("tag")+"/"+get("code"));
    }

    private void bouncePlayerBounds(int velA, int velB, String velP) {
        //bounce away from a player colliding into prop
        //velB and velA come from player and compare Up/Down and Left/Right in pairs
        if(velA > velB)
            putInt(velP, velA + 1);
    }
    public void bounceOffPlayerBounds(gPlayer p) {
        bouncePlayerBounds(p.getInt("vel3"), p.getInt("vel2"), "vel3");
        bouncePlayerBounds(p.getInt("vel2"), p.getInt("vel3"), "vel2");
        bouncePlayerBounds(p.getInt("vel1"), p.getInt("vel0"), "vel1");
        bouncePlayerBounds(p.getInt("vel0"), p.getInt("vel1"), "vel0");
    }

    public boolean checkBump(Shape bounds, int ystart) {
        if(cVars.getInt("maptype") == gMap.MAP_SIDEVIEW) {
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
                }
            }
            else if(getInt("vel0") > getInt("vel1") && getInt("vel3") > getInt("vel2")) {
                if(getInt("coordy")+getInt("dimh") > yend && getInt("coordx")+getInt("dimw") > xstart) {
                    situation = 8;
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
                    if(cVars.getInt("maptype") != gMap.MAP_SIDEVIEW) {
                        putInt("vel0", Math.min(cVars.getInt("velocityplayer") + cVars.getInt("speedbonus"),
                                getInt("vel0") + getInt("vel1") - 1));
                        put("vel1", "0");
                        xCon.ex(String.format("playsound sounds/knock.wav 1 %d %d",
                                getInt("coordx"),getInt("coordy")));
                    }
                    break;
                case 3:
                case 5:
                case 4:
                    //check for bump
                    if(!checkBump(bounds, ystart))
                        return false;
                    putInt("vel3", Math.min(cVars.getInt("velocityplayer") + cVars.getInt("speedbonus"),
                            getInt("vel2") + getInt("vel3") - 1));
                    put("vel2", "0");
                    xCon.ex(String.format("playsound sounds/knock.wav 1 %d %d",
                            getInt("coordx"),getInt("coordy")));
                    break;
                case 6:
                case 7:
                case 8:
                    putInt("vel1", Math.min(cVars.getInt("velocityplayer") + cVars.getInt("speedbonus"),
                            getInt("vel0") + getInt("vel1") - 1));
                    put("vel0", "0");
                    xCon.ex(String.format("playsound sounds/knock.wav 1 %d %d",
                            getInt("coordx"),getInt("coordy")));
                    break;
                case 9:
                case 10:
                case 11:
                    //check for bump
                    if(!checkBump(bounds, ystart))
                        return false;
                    putInt("vel2", Math.min(cVars.getInt("velocityplayer") + cVars.getInt("speedbonus"),
                            getInt("vel2") + getInt("vel3") - 1));
                    put("vel3", "0");
                    xCon.ex(String.format("playsound sounds/knock.wav 1 %d %d",
                            getInt("coordx"),getInt("coordy")));
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


    public boolean wontClipOnMove(int coord, int coord2) {
        int dx = coord == 0 ? coord2 : getInt("coordx");
        int dy = coord == 1 ? coord2 : getInt("coordy");
        for (gTile target : eManager.currentMap.scene.tiles()) {
            if(willCollideWithinTileAtCoords(target, dx, dy)) {
                return false;
            }
        }
        return true;
    }

    public boolean willCollideWithinTileAtCoords(gTile target, int dx, int dy) {
        if(getInt("clip") == 1 && cVars.isOne("clipplayer")) {
            boolean bounceSafe = true;
            Shape bounds = new Ellipse2D.Double(dx, dy, getInt("dimw"), getInt("dimh"));
            Rectangle targetbounds = new Rectangle(target.getInt("coordx"), target.getInt("coordy") + 75,
                    target.getInt("dim0w"), target.getInt("dim0h") - 75);
            int xstart = target.getInt("coordx");
            int ystart = target.getInt("coordy")+75;
            int xend = target.getInt("coordx")+target.getInt("dim0w");
            int yend = target.getInt("coordy") + target.getInt("dim0h");
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
                bounceSafe = false;
            targetbounds = new Rectangle(target.getInt("coordx") + target.getInt("dimw") - target.getInt("dim6w"),
                    target.getInt("coordy") + target.getInt("dim0h"),
                    target.getInt("dim6w"), target.getInt("dim6h"));
            xstart = target.getInt("coordx") + target.getInt("dimw") - target.getInt("dim6w");
            ystart = target.getInt("coordy") + target.getInt("dim0h");
            xend = target.getInt("coordx") + target.getInt("dimw");
            yend = target.getInt("coordy") + target.getInt("dim0h") + target.getInt("dim6h");
            if(!bounceWithinBounds(bounds, targetbounds, xstart, ystart, xend, yend)) // 6
                bounceSafe = false;

            return bounceSafe && bouncesBounds(bounds, target);
        }
        return false;
    }

    static int getCodeForTitle(String title) {
        for(int i = 0; i < propSelection.length;i++) {
            if(title.equalsIgnoreCase(propSelection[i]))
                return i;
        }
        return -1;
    }

    public void setSpriteFromPath(String newpath) {
        put("sprite", newpath);
        sprite = gTextures.getScaledImage(get("sprite"), getInt("dimw"), getInt("dimh"));
    }

    static String[] propSelection = new String[]{
        "Teleporter",
        "Light1",
        "Ladder",
        "Score Point",
        "Safe Point",
        "Flag-Red",
        "Flag-Blue",
        "Powerup",
        "Boostup",
        "Ball (Bouncy)"
    };

    static String[] propSpriteSelection = new String[]{
        "misc/misc_energy_ball_trans_purple.png",
        "misc/light1.png",
        "misc/ladder.png",
        "notexture",
        "notexture",
        "misc/flag_red.png",
        "misc/flag_blue.png",
        "misc/powerup.png",
        "misc/boostup.png",
        "misc/misc_energy_ball_fire.png"
    };

    public gProp(int t, int ux, int uy, int x, int y, int w, int h) {
        super();
        put("type", "THING_PROP");
        putInt("coordx", x);
        putInt("coordy", y);
        putInt("dimw", w);
        putInt("dimh", h);
        putInt("code", t);
        put("sprite", eUtils.getPath(propSpriteSelection[getInt("code")]));
        put("fv", "0.0");
        putInt("int0", ux);
        putInt("int1", uy);
        put("str0","null");
        putInt("botint0", ux);
        put("racebotidcheckins", "");
        put("accelrate", "100");
        put("clip", "1");
        put("crouch", "0");
        put("acceltick", "0");
        put("vel0", "0");
        put("vel1", "0");
        put("vel2", "0");
        put("vel3", "0");
        put("mov0", "0");
        put("mov1", "0");
        put("mov2", "0");
        put("mov3", "0");
        put("native","0");
        put("id", cScripts.createID(8));
        sprite = gTextures.getScaledImage(get("sprite"), getInt("dimw"), getInt("dimh"));
    }
}
