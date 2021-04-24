public class xComGoSpectate extends xCom {
    public String doCommand(String fullCommand) {
        String removeplayerString = "removeplayer " + uiInterface.uuid;
        switch (sSettings.NET_MODE) {
            case sSettings.NET_CLIENT:
                nClient.instance().addNetCmd(removeplayerString);
                break;
            default:
                xCon.ex(removeplayerString);
                break;
        }
        return "entered spectator mode";
    }
}
