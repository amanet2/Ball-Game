public class xComFreecam extends xCom {
    public String doCommand(String fullCommand) {
        gCamera.free();
        return "usage: freecam";
    }
}
