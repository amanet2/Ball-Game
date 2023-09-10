package game;

import java.util.HashMap;

public class gAnimations {
    static int ANIM_SPLASH_BLUE = 0;
    static int ANIM_SPLASH_ORANGE = 1;
    static int ANIM_SPLASH_GREEN = 2;
    static int ANIM_SPLASH_RED = 3;
    static int ANIM_EXPLOSION_REG = 4;
    static int ANIM_EXPLOSION_BLUE = 5;
    static int ANIM_EXPLOSION_GREEN = 6;
    static int ANIM_EXPLOSION_ORANGE = 7;
    static int ANIM_EXPLOSION_PINK = 8;
    static int ANIM_EXPLOSION_PURPLE = 9;
    static int ANIM_EXPLOSION_RED = 10;
    static int ANIM_EXPLOSION_TEAL = 11;
    static int ANIM_EXPLOSION_YELLOW = 12;

    static HashMap<String, Integer> colorNameToExplosionAnimMap;

    static gAnimationsObject[] animation_selection = new gAnimationsObject[] {
        new gAnimationsObject("animations/fire_splash_blue", 50, 50, 16),
        new gAnimationsObject("animations/fire_splash_orange", 50, 50, 16),
        new gAnimationsObject("animations/fire_splash_green", 50, 50, 16),
        new gAnimationsObject("animations/fire_splash_red", 50, 50, 16),
        new gAnimationsObject("animations/fire_splash_blue", 300, 300, 16),
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
