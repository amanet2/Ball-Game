public class uiInterface {
	static boolean inplay = sVars.isZero("startpaused");
	static long gameTime = System.currentTimeMillis();
	private static long gameTimeNanos = System.nanoTime();
	private static long tickCounterTime = gameTime;
	private static long tickTimeNanos = gameTimeNanos;
	private static long framecounterTime = gameTime;
	static long nettickcounterTime = gameTime;
	static int tickReport = 0;
	static int fpsReport = 0;
	static int netReport = 0;
	static int[] camReport = new int[]{0,0};
    private static int frames = 0;
    static String uuid = cScripts.createId();

	public static void startGame() {
	    int ticks = 0;
        cGameLogic.resetGameState();
        while(true) {
            try {
                gameTime = System.currentTimeMillis();
                gameTimeNanos = System.nanoTime();
                //game loop
                if(sSettings.IS_SERVER) {
                    if(sVars.getInt("timelimit") > 0)
                        cVars.putLong("timeleft",
                            sVars.getLong("timelimit") - (int) (gameTime - cVars.getLong("starttime")));
                    else
                        cVars.putLong("timeleft", -1);
                }
                while(tickTimeNanos < gameTimeNanos) {
                    //nano = billion
                    tickTimeNanos += (1000000000/cVars.getInt("gametick"));
                    iInput.readKeyInputs();
                    if(sSettings.IS_SERVER) {
                        gServerLogic.gameLoop();
                    }
                    if (sSettings.IS_CLIENT) {
                        gClientLogic.gameLoop();
                    }
                    gMessages.checkMessages();
                    camReport[0] = cVars.getInt("camx");
                    camReport[1] = cVars.getInt("camy");
                    eManager.updateEntityPositions();
                    cGameLogic.customLoop();
                    ticks += 1;
                    if(tickCounterTime < gameTime) {
                        tickReport = ticks;
                        ticks = 0;
                        tickCounterTime = gameTime + 1000;
                    }
                }
                //draw gfx
                oDisplay.instance().frame.repaint();
                frames += 1;
                long lastFrameTime = System.currentTimeMillis();
                if (framecounterTime < lastFrameTime) {
                    fpsReport = frames;
                    frames = 0;
                    framecounterTime = lastFrameTime + 1000;
                }
                if(sSettings.framerate > 0) {
                    long nextFrameTime = (gameTimeNanos + (1000000000/sSettings.framerate));
                    while (nextFrameTime >= System.nanoTime()) {
//                        Thread.sleep(0,1);//do nothing
                    }
                }
            } catch (Exception e) {
                eUtils.echoException(e);
                e.printStackTrace();
            }
		}
	}

	public static void addListeners() {
		oDisplay.instance().frame.addKeyListener(iInput.keyboardInput);
		oDisplay.instance().frame.addMouseListener(iInput.mouseInput);
		oDisplay.instance().frame.addMouseMotionListener(iInput.mouseMotion);
		oDisplay.instance().frame.addMouseWheelListener(iInput.mouseWheelInput);
		oDisplay.instance().frame.setFocusTraversalKeysEnabled(false);
	}

	public static void init() {
	    eManager.mapsSelection = eManager.getFilesSelection("maps", sVars.get("mapextension"));
        uiMenus.menuSelection[uiMenus.MENU_MAP].setupMenuItems();
        eManager.winClipSelection = eManager.getFilesSelection(eUtils.getPath("sounds/win"));
        eManager.prefabSelection = eManager.getFilesSelection("prefabs");
	    if(sSettings.show_mapmaker_ui) {
            xCon.ex("load");
            cVars.putInt("camx", 0);
            cVars.putInt("camy", 0);
        }
	    else {
            sVars.putInt("drawhitboxes", 0);
            sVars.putInt("drawmapmakergrid", 0);
            xCon.ex("load");
        }
        xCon.ex("exec " + sVars.get("defaultexec"));
        uiMenus.menuSelection[uiMenus.MENU_CONTROLS].items = uiMenusControls.getControlsMenuItems();
        oDisplay.instance().showFrame();
        addListeners();
        startGame();
	}

	public static void exit() {
        xCon.ex(String.format("playsound %s", Math.random() > 0.5 ? "sounds/shout.wav" : "sounds/death.wav"));
        sVars.saveFile(sSettings.CONFIG_FILE_LOCATION);
        if(sVars.isOne("debuglog"))
            xCon.instance().saveLog(sSettings.IS_SERVER
                    ? sSettings.CONSOLE_LOG_LOCATION_SERVER : sSettings.CONSOLE_LOG_LOCATION_CLIENT);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            eUtils.echoException(e);
            e.printStackTrace();
        }
        System.exit(0);
    }
}
