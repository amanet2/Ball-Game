public class cServer {
    public static String getGameStateServer() {
        //ugly if else
        switch(cVars.getInt("gamemode")) {
            case cGameMode.FLAG_MASTER:
                return cVars.get("flagmasterid");
            case cGameMode.VIRUS:
                if(nServer.instance().clientArgsMap.containsKey("server"))
                    return nServer.instance().clientArgsMap.get("server").get("state");
                return "";
            default:
                break;
        }
        return "";
    }
}
