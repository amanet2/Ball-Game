import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MultipleGradientPaint;
import java.awt.RadialGradientPaint;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

public class gThing {
    Image sprite = null;
    String spritePath = "null";
    String type = "null";
    String color = "red"; //for players
    int[] coords = {0, 0};
    int[] dims = {0, 0};
    public String id;
    gArgSet args;
    int acceldelay = 32;
    int interpDelay = 4;
    int accelrate = 2;
    int decelrate = 1;
    String waypoint = "null";
    long cooldown = 0;
    long acceltick = 0;
    long interpTick = 0;
    double fv = 0.0;
    int vel0 = 0;
    int vel1 = 0;
    int vel2 = 0;
    int vel3 = 0;
    int mov0 = 0;
    int mov1 = 0;
    int mov2 = 0;
    int mov3 = 0;
    int src = gWeapons.none; //for getting weapon source of a bullet e.g. launcher explosion
    int dmg = 0; //bullets
    int ammo = 0; //max ammo
    String srcId = "-1"; //bullets
    int occupied = 0;

    public gThing() {
        args = new gArgSet();
        gThing parent = this;
        //args are bindings for what scripts can use
        args.putArg(new gArg("ammo", "0") {
            public void onUpdate() {
                parent.ammo = Integer.parseInt(value);
            }

            public String getValue() {
                return Integer.toString(parent.ammo);
            }
        });
        args.putArg(new gArg("coords", "0:0") {
            public void onChange() {
                String[] argCoords = value.split(":");
                parent.coords = new int[]{Integer.parseInt(argCoords[0]), Integer.parseInt(argCoords[1])};
            }

            public String getValue() {
                return coords[0] + ":" + coords[1];
            }
        });
        args.putArg(new gArg("coordx", "0") {
            public String getValue() {
                return Integer.toString(parent.coords[0]);
            }
        });
        args.putArg(new gArg("occupied", "0") {
            public String getValue() {
                return Integer.toString(parent.coords[0]);
            }
        });
        args.putArg(new gArg("coordy", "0") {
            public String getValue() {
                return Integer.toString(parent.coords[1]);
            }
        });
        args.putArg(new gArg("vel0", "0") {
            public void onChange() {
                parent.vel0 = Integer.parseInt(value);
            }

            public String getValue() {
                return Integer.toString(parent.vel0);
            }
        });
        args.putArg(new gArg("vel1", "0") {
            public void onChange() {
                parent.vel1 = Integer.parseInt(value);
            }

            public String getValue() {
                return Integer.toString(parent.vel1);
            }
        });
        args.putArg(new gArg("vel2", "0") {
            public void onChange() {
                parent.vel2 = Integer.parseInt(value);
            }

            public String getValue() {
                return Integer.toString(parent.vel2);
            }
        });
        args.putArg(new gArg("vel3", "0") {
            public void onChange() {
                parent.vel3 = Integer.parseInt(value);
            }

            public String getValue() {
                return Integer.toString(parent.vel3);
            }
        });
        args.putArg(new gArg("waypoint", parent.waypoint) {
            public void onChange() {
                parent.waypoint = value;
            }

            public String getValue() {
                return parent.waypoint;
            }
        });
    }

    public String toString() {
        return args.toString();
    }

    public void addToScene(gScene scene) {
        scene.getThingMap(type).put(id, this);
    }

    public boolean coordsWithinBounds(int x, int y) {
        return (x >= eUtils.scaleInt(coords[0] - (int) gCamera.coords[0])
                && x <= eUtils.scaleInt(coords[0] - (int) gCamera.coords[0] + dims[0]))
                && (y >= eUtils.scaleInt(coords[1] - (int) gCamera.coords[1])
                && y <= eUtils.scaleInt(coords[1] - (int) gCamera.coords[1] + dims[1]));
    }

    public boolean collidesWithThing(gThing target) {
        return new Rectangle(target.coords[0], target.coords[1], target.dims[0], target.dims[1]).intersects(new Rectangle(coords[0], coords[1], dims[0], dims[1]));
    }

    public boolean isOnScreen() {
        return !sSettings.culling || ((coords[0] - gCamera.coords[0] < eUtils.unscaleInt(sSettings.width))
                && (coords[0] + dims[0] - gCamera.coords[0] > 0)
                && (coords[1] - gCamera.coords[1] < eUtils.unscaleInt(sSettings.height))
                && (coords[1] + dims[1] + (int)(300*sSettings.vfxshadowfactor) - gCamera.coords[1] > 0));
    }
    
    public void drawRoundShadow(Graphics2D g2) {
        if(sSettings.vfxenableshadows) {
            Rectangle2D shadowBounds = new Rectangle.Double(
                    coords[0],
                    coords[1] + 5*dims[1]/6,
                    dims[0],
                    (double)dims[1]/3
            );
            RadialGradientPaint df = new RadialGradientPaint(
                    shadowBounds, new float[]{0f, 1f},
                    new Color[]{gColors.getColorFromName("clrw_shadow1"), gColors.getColorFromName("clrw_clear")},
                    MultipleGradientPaint.CycleMethod.NO_CYCLE
            );
            g2.setPaint(df);
            g2.fillRect(
                    (int)shadowBounds.getX(),
                    (int)shadowBounds.getY(),
                    (int)shadowBounds.getWidth(),
                    (int)shadowBounds.getHeight()
            );
        }
    }
}
