import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class eUtils {

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

    public static String createId() {
        return xCon.ex("getrand 11111111 99999999");
    }
}
