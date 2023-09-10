package game;

public class gAnimation extends gThing {
    int code;
    int frame;
    long frametime;

    public gAnimation(int code, int x, int y) {
        super();
        this.code = code;
        this.frame = 0;
        this.frametime = sSettings.gameTime;
        this.coords = new int[]{x, y};
    }
}
