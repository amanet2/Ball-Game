public class sSettings {
    /**
	 * data source
	 */
	static String CONFIG_FILE_LOCATION = "config/settings.cfg";
	static String CONSOLE_LOG_LOCATION = "_console_log.txt";
	public static boolean show_mapmaker_ui = false;
	static boolean IS_SERVER = false;
	static boolean IS_CLIENT = false;
	/**
	* vfx
	* */
	static int gamescale = 2160;
	static int width = 1920;
	static int height = 1080;
	static int framerate = 240;
	static int displaymode = 0;
	static int vfxfactor = 144;
	static int vfxfactordiv = 16;
	static double vfxshadowfactor = 0.3;
	static boolean drawplayerarrow = true;
	static boolean drawhitboxes = true;
	static boolean drawmapmakergrid = true;
	static boolean vfxenableshading = true;
	static boolean vfxenableshadows = true;
	static boolean vfxenableflares = true;
	static boolean vfxenableanimations = true;
	/**
	 * AUDIO
	 * **/
	static boolean audioenabled = true;
	/**
	 * NET
	 * **/
	static int max_packet_size = 508;
	static int rcvbytesclient = 2048;
	static int rcvbytesserver = 512;
	static int rateclient = 60;
	static int rateserver = 1000;
	static int rategame = 240;
	static int ratebots = 15;
	static boolean debug = false;
	static boolean smoothing = true;
}
