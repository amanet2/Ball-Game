public class xComGoSpectate extends xCom {
    public String doCommand(String fullCommand) {
        String removeplayerString = "removeplayer " + uiInterface.uuid;
        if (sSettings.IS_CLIENT) {
            nClient.instance().addNetCmd(removeplayerString);
        } else {
            xCon.ex(removeplayerString);
        }
        return "entered spectator mode";
    }
}
