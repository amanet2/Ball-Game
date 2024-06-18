import java.util.HashMap;

public class gAnimations {
    static int ANIM_EXPLOSION_BLUE = 0;
    static int ANIM_EXPLOSION_GREEN = 1;
    static int ANIM_EXPLOSION_ORANGE = 2;
    static int ANIM_EXPLOSION_PINK = 3;
    static int ANIM_EXPLOSION_PURPLE = 4;
    static int ANIM_EXPLOSION_RED = 5;
    static int ANIM_EXPLOSION_TEAL = 6;
    static int ANIM_EXPLOSION_YELLOW = 7;

    static HashMap<String, Integer> colorNameToExplosionAnimMap;

    static gAnimationsObject[] animation_selection = new gAnimationsObject[] {
        new gAnimationsObject("animations/fire_splash_blue", 300, 300, 16),
        new gAnimationsObject("animations/fire_splash_green", 300, 300, 16),
        new gAnimationsObject("animations/fire_splash_orange", 300, 300, 16),
        new gAnimationsObject("animations/fire_splash_pink", 300, 300, 16),
        new gAnimationsObject("animations/fire_splash_purple", 300, 300, 16),
        new gAnimationsObject("animations/fire_splash_red", 300, 300, 16),
        new gAnimationsObject("animations/fire_splash_teal", 300, 300, 16),
        new gAnimationsObject("animations/fire_splash_yellow", 300, 300, 16)
    };

    static void init() {
        colorNameToExplosionAnimMap = new HashMap<>();
        colorNameToExplosionAnimMap.put("blue", ANIM_EXPLOSION_BLUE);
        colorNameToExplosionAnimMap.put("green", ANIM_EXPLOSION_GREEN);
        colorNameToExplosionAnimMap.put("orange", ANIM_EXPLOSION_ORANGE);
        colorNameToExplosionAnimMap.put("pink", ANIM_EXPLOSION_PINK);
        colorNameToExplosionAnimMap.put("purple", ANIM_EXPLOSION_PURPLE);
        colorNameToExplosionAnimMap.put("red", ANIM_EXPLOSION_RED);
        colorNameToExplosionAnimMap.put("teal", ANIM_EXPLOSION_TEAL);
        colorNameToExplosionAnimMap.put("yellow", ANIM_EXPLOSION_YELLOW);
    }
}
