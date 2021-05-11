import java.awt.Image;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class eUtils {
    static double zoomLevel = 1.0;
    private static int[] screendims = new int[]{Integer.parseInt(sVars.get("vidmode").split(",")[0]),
            Integer.parseInt(sVars.get("vidmode").split(",")[1])};

    public static boolean containsFields(HashMap<String, String> map, String[] fields) {
        for(String required : fields) {
            if(map.get(required) == null)
                return false;
        }
        return true;
    }

    public static boolean resolutionChanged() {
        if(screendims[0] != Integer.parseInt(sVars.get("vidmode").split(",")[0])
        || screendims[1] != Integer.parseInt(sVars.get("vidmode").split(",")[1])) {
            screendims = new int[]{
                    Integer.parseInt(sVars.get("vidmode").split(",")[0]),
                    Integer.parseInt(sVars.get("vidmode").split(",")[1])};
            return true;
        }
        return false;
    }

    public static String getPath(String s) {
        return sVars.get("datapath") + "/" + s;
    }

    public static String getTimeString(Long l) {
        Date date = new Date(l);
        DateFormat formatter = new SimpleDateFormat("mm:ss");
        return formatter.format(date);
    }

	public static int scaleInt(int inputInt) {
		return (int) ((((double) inputInt / cVars.getDouble("gamescale")) * (double) sSettings.height));
	}

	public static int unscaleInt(int inputInt) {
		return (int) ((((double) inputInt * cVars.getDouble("gamescale")) / (double) sSettings.height));
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
                eUtils.echoException(e);
                e.printStackTrace();
            }
        }
    }

    public static double areaOfTriangle(int x1, int y1, int x2, int y2, int x3, int y3) {
        return Math.abs((x1*(y2-y3) + x2*(y3-y1)+ x3*(y1-y2))/2.0);
    }

    public static boolean pointInsideTriangle(int x, int y, int x1, int y1, int x2, int y2, int x3, int y3) {
        double fullArea = areaOfTriangle(x1,y1,x2,y2,x3,y3);
        double area1 = areaOfTriangle(x, y, x2, y2, x3, y3);
        double area2 = areaOfTriangle(x1, y1, x, y, x3, y3);
        double area3 = areaOfTriangle(x1, y1, x2, y2, x, y);
        return fullArea == area1 + area2 + area3;
    }

    public static void echoException(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String sStackTrace = sw.toString(); // stack trace as a string
        xCon.ex("echo " + sStackTrace.split("\\n")[0]);
    }
}
