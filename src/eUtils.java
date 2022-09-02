import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class eUtils {
    static double zoomLevel = 1.0;

    public static String getPath(String s) {
        return sSettings.datapath + "/" + s;
    }

    public static String getTimeString(Long l) {
        Date date = new Date(l);
        DateFormat formatter = new SimpleDateFormat("mm:ss");
        return formatter.format(date);
    }

	public static int scaleInt(int inputInt) {
		return (int) ((((double) inputInt / sSettings.gamescale) * (double) sSettings.height));
	}

	public static int unscaleInt(int inputInt) {
		return (int) ((((double) inputInt * sSettings.gamescale) / (double) sSettings.height));
	}

	public static int roundToNearest(int val, int snap) {
		return (Math.round(val/snap))*snap;
	}

    public static void echoException(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String sStackTrace = sw.toString(); // stack trace as a string
        xCon.ex("echo " + sStackTrace.split("\\n")[0]);
    }
}
