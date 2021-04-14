public class xComCentercamera extends xCom {
    public String doCommand(String fullCommand) {
        cScripts.centerCamera();
        return "camera centered";
    }
}
