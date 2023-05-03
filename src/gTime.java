import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class gTime {
    static long gameTime = System.currentTimeMillis();

    public static String getTimeString(Long l) {
        Date date = new Date(l);
        DateFormat formatter = new SimpleDateFormat("mm:ss");
        return formatter.format(date);
    }
}
