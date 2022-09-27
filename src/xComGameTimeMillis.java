public class xComGameTimeMillis extends xCom {
    public String doCommand(String fullCommand) {
        return Long.toString(gTime.gameTime);
    }
}
