public class cGameLogic {
    static final int DEATHMATCH = 0;
    static final int VIRUS = 1;
    static final int FLAG_MASTER = 2;
    static final int GOLD_MASTER = 3;

    static String[][] net_gamemode_strings = {
            {"Rock Master", "Rock other players"},
            {"Virus Master", "Don't become infected"},
            {"Flag Master", "Hold onto the flag"},
            {"Gold Master", "Pick up the gold"}
    };

    public static boolean isGame(int mode) {
        return cClientLogic.gamemode == mode;
    }
}
