public class xComCentercamera extends xCom {
    public String doCommand(String fullCommand) {
        gCamera.centerCamera();
        return "camera centered";
    }
}
