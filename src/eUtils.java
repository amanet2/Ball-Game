import java.awt.Image;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class eUtils {
    static double zoomLevel = 1.0;

    public static String getPath(String s) {
        return sVars.get("datapath") + "/" + s;
    }

    public static String getTimeString(Long l) {
        Date date = new Date(l);
        DateFormat formatter = new SimpleDateFormat("mm:ss");
        return formatter.format(date);
    }

	public static int scaleInt(int inputInt) {
		return (int) ((((double) inputInt / sVars.getDouble("gamescale")) * (double) sSettings.height));
	}

	public static int unscaleInt(int inputInt) {
		return (int) ((((double) inputInt * sVars.getDouble("gamescale")) / (double) sSettings.height));
	}

    public static Image getWeaponScaledSpriteForPath(String path, int x, int y) {
        return  gTextures.getScaledImage(path, x, y);
    }

	public static int roundToNearest(int val, int snap) {
		return (Math.round(val/snap))*snap;
	}

    public static void disableApplePressAndHold() {
        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            try {
                Runtime.getRuntime().exec("defaults write NSGlobalDomain ApplePressAndHoldEnabled -bool false");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
