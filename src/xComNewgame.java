public class xComNewgame extends xCom {
    public String doCommand(String fullCommand) {
        sSettings.net_server = true;
        sSettings.NET_MODE = sSettings.NET_SERVER;
        xCon.ex(String.format("load wp_steam_gray.map"));
        return "new game started";
    }
}
