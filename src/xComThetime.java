import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class xComThetime extends xCom {
    public String doCommand(String fullCommand) {
        eManager.currentMap = new gMap();
        return "map cleared";
//        return "the time is " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }
}
