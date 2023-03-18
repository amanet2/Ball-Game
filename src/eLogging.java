import java.io.PrintWriter;
import java.io.StringWriter;

public class eLogging {
    public static void logException(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String sStackTrace = sw.toString(); // stack trace as a string
        xCon.ex("echo " + sStackTrace.split("\\n")[0]);
    }
}
