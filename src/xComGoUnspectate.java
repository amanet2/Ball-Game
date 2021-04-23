public class xComGoUnspectate extends xCom {
    public String doCommand(String fullCommand) {
        switch (sSettings.NET_MODE) {
            case sSettings.NET_CLIENT:
                nClient.instance().addNetCmd("respawnplayer " + uiInterface.uuid);
                break;
            default:
                xCon.ex("respawn");
                break;
        }
        return "left spectator mode";
    }
}
