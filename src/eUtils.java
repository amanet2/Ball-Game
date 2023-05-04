public class eUtils {

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
