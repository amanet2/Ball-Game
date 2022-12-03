public class xComGetWinnerId extends xCom {
    public String doCommand(String fullCommand) {
        return gScoreboard.getWinnerId();
    }
}
