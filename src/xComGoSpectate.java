public class xComGoSpectate extends xCom {
    public String doCommand(String fullCommand) {
        nClient.instance().addNetCmd("removeplayer " + uiInterface.uuid);
        return "entered spectator mode";
    }
}
