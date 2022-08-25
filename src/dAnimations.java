import java.awt.Graphics2D;
import java.util.HashMap;

public class dAnimations {
    public static void drawAnimations(Graphics2D g2, gScene scene, long gameTimeMillis){
        HashMap animationsMap = scene.getThingMap("THING_ANIMATION");
        for(Object id : animationsMap.keySet()) {
            gAnimationEmitter a = (gAnimationEmitter) animationsMap.get(id);
            if(a.getInt("frame") < gAnimations.animation_selection[a.getInt("animation")
                    ].frames.length) {
                if (gAnimations.animation_selection[a.getInt("animation")].frames[a.getInt("frame")]
                        != null) {
                    g2.drawImage(gAnimations.animation_selection[a.getInt("animation")].frames[
                                    a.getInt("frame")],
                            a.getInt("coordx"),
                            a.getInt("coordy"),
                            null
                    );
                    if (a.getLong("frametime") + 1000/gAnimations.animation_selection[a.getInt("animation")].framerate
                            < gameTimeMillis) {
                        a.putInt("frame", a.getInt("frame")+1);
                        a.putLong("frametime", gameTimeMillis);
                    }
                }
            }
        }
    }
}
