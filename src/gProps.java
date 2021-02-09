public class gProps {

    static String[] spritesSelection = new String[]{
            "misc/misc_energy_ball_trans_purple.png",
            "none",
            "misc/flag_red.png",
            "misc/flag_blue.png",
            "misc/powerup.png",
            "misc/boostup.png",
            "none"
    };

    static String[] titles = new String[]{
            "PROP_TELEPORTER",
            "PROP_SCOREPOINT",
            "PROP_FLAGRED",
            "PROP_FLAGBLUE",
            "PROP_POWERUP",
            "PROP_BOOSTUP",
            "PROP_SPAWNPOINT"
    };

    static int getCodeForTitle(String title) {
        for(int i = 0; i < gProps.titles.length;i++) {
            if(title.equalsIgnoreCase(gProps.titles[i]))
                return i;
        }
        return -1;
    }

    public static String getTitleForCode(int code) {
        return titles[code];
    }

    public static String getTitleForProp(gProp prop) {
        return getTitleForCode(prop.getInt("code"));
    }
}
