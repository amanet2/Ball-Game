public class gAnimations {
    static int ANIM_SPLASH_BLUE = 0;
    static int ANIM_SPLASH_ORANGE = 1;
    static int ANIM_SPLASH_GREEN = 2;
    static int ANIM_SPLASH_RED = 3;
    static int ANIM_EXPLOSION_REG = 4;
    static int ANIM_EXPLOSION_BLUE = 5;

    static gAnimation[] animation_selection = new gAnimation[] {
        new gAnimation("animations/fire_splash_blue", 50, 50, 16),
        new gAnimation("animations/fire_splash_orange", 50, 50, 16),
        new gAnimation("animations/fire_splash_green", 50, 50, 16),
        new gAnimation("animations/fire_splash_red", 50, 50, 16),
        new gAnimation("animations/explosion", 300, 300, 16),
        new gAnimation("animations/explosion_blue", 300, 300, 16)
    };
}
