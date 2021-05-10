public class cGameLogic {
    static final int DEATHMATCH = 0;
    static final int VIRUS = 1;
    static final int FLAG_MASTER = 2;

    static String[] net_gamemode_texts = {
            "Kill Master",
            "Virus Master",
            "Flag Master"
    };

    static String[] net_gamemode_descriptions = {
            "Kill other players",
            "Don't catch the virus",
            "Hold onto the flag"
    };

    public static void resetVirusPlayers() {
        if(nServer.instance().clientArgsMap.containsKey("server") && nServer.instance().clientIds.size() > 0) {
            int randomClientIndex = (int) (Math.random() * nServer.instance().clientIds.size());
            nServer.instance().clientArgsMap.get("server").put("state",
                    nServer.instance().clientIds.get(randomClientIndex));
        }
    }
}
