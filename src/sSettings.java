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
	static int width = 1280;
	static int height = 720;
	static int framerate = 60;
	static int vfxfactor = 144;
	static int vfxfactordiv = 16;
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
}
