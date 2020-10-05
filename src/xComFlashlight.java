public class xComFlashlight extends xCom {
    public String doCommand(String fullCommand) {
        cVars.flip("flashlight");
        xCon.ex("playsound sounds/tap.wav");
        return fullCommand;
    }
}
