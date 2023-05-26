public class gAnimationEmitter extends gThing {

    public gAnimationEmitter(int a, int x, int y) {
        super();
        putInt("coordx", x);
        putInt("coordy", y);
        putInt("animation", a);
        putInt("frame", 0);
        putLong("frametime", sSettings.gameTime);
    }
}
