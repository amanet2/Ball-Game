public class cGameLogic {
    static final int DEATHMATCH = 0;
    static final int VIRUS = 1;
    static final int FLAG_MASTER = 2;
    static final int GOLD_MASTER = 3;

    static String[] net_gamemode_texts = {
            "Rock Master",
            "Virus Master",
            "Flag Master",
            "Gold Master"
    };

    static String[] net_gamemode_descriptions = {
            "Rock other players",
            "Don't become infected",
            "Hold onto the flag",
            "Pick up the gold"
    };

    public static boolean isDeathmatch() {
        return cClientLogic.gamemode == DEATHMATCH;
    }

    public static boolean isVirus() {
        return cClientLogic.gamemode == VIRUS;
    }

    public static boolean isFlagMaster() {
        return cClientLogic.gamemode == FLAG_MASTER;
    }

    public static boolean isGame(int mode) {
        return cClientLogic.gamemode == mode;
    }

    public static void resetVirusPlayers() {
        if(nServer.instance().clientArgsMap.containsKey("server") && nServer.instance().hasClients()) {
            nServer.instance().clientArgsMap.get("server").put("virusids", nServer.instance().getRandomClientId());
        }
    }
}
