public class sSettings {
    /**
	 * data source
	 */
	static String CONFIG_FILE_LOCATION_SERVER = "config/server";
	static String CONFIG_FILE_LOCATION_CLIENT = "config/client";
	static String CONFIG_FILE_LOCATION_GAME = "config/game";
	static String CONSOLE_LOG_LOCATION = "_console_log";
	static String datapath = "ballgame";
	public static boolean show_mapmaker_ui = false;
	static boolean IS_SERVER = false;
	static boolean IS_CLIENT = false;
	static String[] colorSelection = {"blue", "green", "orange", "pink", "purple", "red", "teal", "yellow"};
	static String[] resolutions = {"640x480", "800x600", "1024x768", "1280x720", "1280x1024", "1680x1050", "1600x1200", "1920x1080", "2560x1440", "3840x2160"};
	static int[] framerates = {24, 30, 60, 75, 98, 120, 144, 165, 240, 320, 360};
	public static String[] object_titles = new String[]{};
	public static String[] prefab_titles;
	/**
	* vfx
	* */
	static int gamescale = 2160;
	static int width = 1920;
	static int height = 1080;
	static int framerate = 240;
	static int displaymode = 0;
	static int vfxfactor = 144;
	static int vfxfactordiv = 8;
	static double vfxshadowfactor = 0.3;
	static boolean drawplayerarrow = true;
	static boolean drawhitboxes = false;
	static boolean drawmapmakergrid = false;
	static boolean vfxenableshading = true;
	static boolean vfxenableshadows = true;
	static boolean vfxenableflares = true;
	static boolean vfxenableanimations = true;
	static int popuplivetime = 2000;
	static int velocity_popup = 2;
	static double zoomLevel = 1.0;
	/**
	 * AUDIO
	 * **/
	static boolean audioenabled = true;
	/**
	 * NET
	 * **/
	static int max_packet_size = 1200;
	static int rcvbytesclient = 2048;
	static int rcvbytesserver = 512;
	static int rateserver = 60; //server internal game rate for simulation
	static int rateservernet = 1000; //server rate to poll for packet (<= 0 means unlimited)
	static int rateclient = 30; //client net rate to request update from server
	static int rateShell = 240; //desktop window rate like input, visual update
}
