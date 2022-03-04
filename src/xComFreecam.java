public class xComFreecam extends xCom {
    public String doCommand(String fullCommand) {
        gCamera.mode = gCamera.MODE_FREE;
        return "usage: freecam";
    }
}
