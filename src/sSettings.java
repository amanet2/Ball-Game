import java.util.concurrent.ThreadLocalRandom;

public class sSettings {
    // sources
	static String CONFIG_FILE_LOCATION_SERVER = "config/server.bs";
	static String CONFIG_FILE_LOCATION_CLIENT = "config/client.bs";
	static String CONFIG_FILE_LOCATION_GAME = "config/game.bs";
	static String CONSOLE_LOG_LOCATION = "_console_log";
	static String datapath = "assets";
	public static boolean show_mapmaker_ui = false;
	static boolean IS_SERVER = false;
	static boolean IS_CLIENT = false;
	static String[] colorSelection = {"blue", "green", "orange", "pink", "purple", "red", "teal", "yellow"};
	static String[] resolutions = {"640x480", "800x600", "1024x768", "1280x720", "1600x900", "1920x1080", "2560x1440", "3840x2160"};
	static int[] framerates = {30, 60, 75, 120, 240, 360, 540, 1000};
	public static String[] object_titles = new String[]{};
	public static String[] prefab_titles;

	// vfx
	static int gamescale = 2160;
	static int width = 1920;
	static int height = 1080;
	static boolean borderless = false;
	static int vfxfactor = 144;
	static int vfxfactordiv = 8;
	static double vfxshadowfactor = 0.3;
	static boolean drawhitboxes = false;
	static boolean drawmapmakergrid = false;
	static boolean vfxenableshading = true;
	static boolean vfxenableshadows = true;
	static boolean vfxenableflares = true;
	static boolean vfxenableanimations = true;
	static int popuplivetime = 2000;
	static int velocity_popup = 2;
	static double zoomLevel = 1.0;

	// audio
	static boolean audioenabled = true;
	static double clientVolume = 100.0;

	// net
	static int max_packet_size = 1200;
	static int rcvbytesclient = 2048;
	static int rcvbytesserver = 512;
	static int ratesimulation = 60; //server internal game rate for simulation
	static int rateserver = 1000; //server rate to poll for packet (<= 0 means unlimited)
	static int rateclient = 30; //client net rate to request update from server
	static int rateShell = 1000; //desktop window rate like input, visual update

	// server
	static int serverTimeLimit = 180000;
    static long serverTimeLeft = 180000;
	static int serverListenPort = 5555;
	static boolean serverLoadingFromHDD = false;
	static int serverGameMode = 0;
	static int serverVoteSkipLimit = 2;
	static int serverRespawnDelay = 3000;
	static int serverMaxHP = 500;
	static int serverVelocityPlayerBase = 16;
	static boolean respawnEnabled = true;
	static int botThinkTimeDelay = 150;
	static int botShootRange = 1200;
	static int botCount = 0;
	static int botsPaused = 0;

	//client
	static int clientMaxHP = 500;
	static String clientSelectedItemId = "";
	static String clientSelectedPrefabId = "";
	static String clientPlayerName = "player";
	static String clientPlayerColor = "blue";
	static int clientVelocityPlayerBase = 16;
	static boolean clientDebug = false;
	static boolean clientDebugLog = false;
	static String clientNewPrefabName = "room";
	static int clientGameMode = 0;
	static String clientGameModeTitle = "Rock Master";
	static String clientGameModeText = "Rock Other Players";
	static boolean clientMapLoaded = false;
	static int clientPrevX = 0;
	static int clientPrevY = 0;
	static int clientPrevW = 300;
	static int clientPrevH = 300;
	static long clientNetSendTime = 0;
	static long clientNetRcvTime = 0;
	static int clientPing = 0;
	static long clientTimeLeft = 120000;
    static long gameTime = System.currentTimeMillis();
    static boolean inplay = false; //must be FALSE for mapmaker to work right
    static boolean inconsole = false;
    static int tickReport = 0;
    static int fpsReport = 0;
    static int tickReportClient = 0;
    static int tickReportSimulation = 0;
    static int tickReportServer = 0;
    static int frames = 0;
    static String uuid = Integer.toString(ThreadLocalRandom.current().nextInt(11111111, 99999999));
    static boolean hideMouseUI = false;
    static boolean showscore = false;
    static boolean showfps = false;
    static boolean showcam = false;
    static boolean showmouse = false;
    static boolean shownet = false;
    static boolean showplayer = false;
    static boolean showtick = false;
    static boolean showscale = false;
	static int screenMessageFadeTime = 10000;
}
