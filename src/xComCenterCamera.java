import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class xComCenterCamera extends xCom {
    public String doCommand(String fullCommand) {
        gCamera.centerCamera();
        return "usage: centercamera";
    }
}
