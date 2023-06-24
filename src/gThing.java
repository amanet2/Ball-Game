import java.awt.*;

public class gThing {
    String type = "null";
    String color = "red";
    int[] coords = {0, 0};
    int[] dims = {0, 0};
    String id;
    String prefabId;
    gArgSet args;
    int acceldelay = 100;
    int accelrate = 2;
    int decelrate = 1;
    String pathsprite = "null";
    int weapon = gWeapons.none;
    String decorationsprite = "null";
    String weaponsprite = "null";
    String waypoint = "null";
    int cooldown = 0;
    int acceltick = 0;
    double fv = 0.0;
    int vel0 = 0;
    int vel1 = 0;
    int vel2 = 0;
    int vel3 = 0;
    int mov0 = 0;
    int mov1 = 0;
    int mov2 = 0;
    int mov3 = 0;
    int toph = 0;
    int wallh = 0;
    int frame = 0;
    long frametime = 0;
    int animation = -1;

    public gThing() {
        args = new gArgSet();
        gThing parent= this;
        //args are bindings for what scripts can use
        args.putArg(new gArg("coords", "0:0") {
            public void onChange() {
                String[] coords = value.split(":");
                parent.coords = new int[]{
                        Integer.parseInt(coords[0]),
                        Integer.parseInt(coords[1])
                };
            }
        });
        args.putArg(new gArg("dims", "0:0") {
            public void onChange() {
                String[] dims = value.split(":");
                parent.coords = new int[]{
                        Integer.parseInt(dims[0]),
                        Integer.parseInt(dims[1])
                };
            }
        });
    }

    public boolean coordsWithinBounds(int x, int y) {
        return (x >= eUtils.scaleInt(coords[0] - gCamera.coords[0])
                && x <= eUtils.scaleInt(coords[0] - gCamera.coords[0] + dims[0]))
                && (y >= eUtils.scaleInt(coords[1] - gCamera.coords[1])
                && y <= eUtils.scaleInt(coords[1] - gCamera.coords[1] + dims[1]));
    }

    public boolean collidesWithThing(gThing target) {
        return new Rectangle(target.coords[0], target.coords[1], target.dims[0], target.dims[1]).intersects(new Rectangle(coords[0], coords[1], dims[0], dims[1]));
    }
}
