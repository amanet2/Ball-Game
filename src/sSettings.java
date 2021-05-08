public class sSettings {
    /**
	 * data source
	 */
	static String CONFIG_FILE_LOCATION = "config/settings.cfg";
	static String CONSOLE_LOG_LOCATION_SERVER = "_console_log_server.txt";
	static String CONSOLE_LOG_LOCATION_CLIENT = "_console_log_client.txt";
	static final int NET_OFFLINE = 0;
	static final int NET_SERVER = 1;
	static final int NET_CLIENT = 2;
	static int NET_MODE = NET_OFFLINE; //0 offline/mapeditor, 1 server, 2 client
	static boolean show_mapmaker_ui = false;
	/**
	* vfx
	* */
	static int width = 1280;
	static int height = 720;
	static int framerate = 60;

	public static boolean isServer() {
		return NET_MODE == NET_SERVER;
	}

	public static boolean isClient() {
		return NET_MODE == NET_CLIENT;
	}

	public static boolean isOffline() {
		return NET_MODE == NET_OFFLINE;
	}
}
