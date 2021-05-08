public class xComGoUnspectate extends xCom {
    public String doCommand(String fullCommand) {
        nClient.instance().addNetCmd("respawnnetplayer " + uiInterface.uuid);
        return "left spectator mode";
    }
}
