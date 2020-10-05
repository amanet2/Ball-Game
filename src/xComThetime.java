import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class xComThetime extends xCom {
    public String doCommand(String fullCommand) {
        return "the time is " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }
}
