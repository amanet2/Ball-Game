public class xComGoSpectate extends xCom {
    public String doCommand(String fullCommand) {
        nClient.instance().addNetCmd("deleteplayer " + uiInterface.uuid);
        return "entered spectator mode";
    }
}
