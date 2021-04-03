public class uiInterface {
	static boolean inplay = sVars.isZero("startpaused");
	static long gameTime = System.currentTimeMillis();
	static long gameTimeNanos = System.nanoTime();
	static long tickCounterTime = gameTime;
	static long tickTimeNanos = gameTimeNanos;
	static long framecounterTime = gameTime;
	static long nettickcounterTime = gameTime;
	static long networkTime = gameTime;
	static int tickReport = 0;
	static int fpsReport = 0;
	static int netReport = 0;
	static int[] camReport = new int[]{0,0};
    static int frames = 0;
    static long lastFrameTime = 0;
    static String uuid = cScripts.createId();

	public static void startTicker() {
	    int ticks = 0;

        cScripts.setupGame();
		while(true) {
            try {
                //inits
                if(sSettings.net_server && !nServer.instance().isAlive())
                    nServer.instance().start();
                else if(sSettings.net_client && !nClient.instance().isAlive())
                    nClient.instance().start();
                gameTime = System.currentTimeMillis();
                gameTimeNanos = System.nanoTime();
                //game loop
                if(sSettings.net_server) {
                    cVars.putLong("timeleft",
                            sVars.getLong("timelimit") - (int) (gameTime - cVars.getLong("starttime")));
                }
                if(sSettings.net_server && cVars.contains("serveraddbots")
                        && cVars.getLong("serveraddbotstime") < gameTime) {
                    nServer.instance().addBots();
                    cVars.remove("serveraddbots");
                }
                while(tickTimeNanos < gameTimeNanos) {
                    //nano = billion
                    tickTimeNanos += (1000000000/cVars.getInt("gametick"));
                    oDisplay.instance().checkDisplay();
                    oAudio.instance().checkAudio();
                    iInput.readKeyInputs();
                    gCamera.updatePosition();
                    if(sSettings.net_server)
                        nServer.instance().processPackets();
                    else if(sSettings.net_client)
                        nClient.instance().processPackets();
                    else if(sSettings.show_mapmaker_ui) {
                        cScripts.selectThingUnderMouse();
                    }
                    gMessages.checkMessages();
                    camReport[0] = cVars.getInt("camx");
                    camReport[1] = cVars.getInt("camy");
                    if (inplay || cScripts.isNetworkGame()) {
                        eManager.updateEntityPositions();
                        cGameLogic.customLoop();
                    }
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
                    lastFrameTime = System.currentTimeMillis();
                    if (framecounterTime < lastFrameTime) {
                        fpsReport = frames;
                        frames = 0;
                        framecounterTime = lastFrameTime + 1000;
                    }
                long nextFrameTime = (gameTimeNanos + (1000000000/sSettings.framerate));
                while(nextFrameTime >= System.nanoTime()); //do nothing
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
	    eManager.mapsSelection = eManager.getFilesSelection(eUtils.getPath(""),sVars.get("mapextension"));
        uiMenus.menuSelection[uiMenus.MENU_MAP].setupMenuItems();
        eManager.winClipSelection = eManager.getFilesSelection(eUtils.getPath("sounds/win"));
        eManager.prefabSelection = eManager.getFilesSelection(eUtils.getPath("prefabs"));
	    if(sSettings.show_mapmaker_ui) {
            xCon.ex("load");
            cVars.putInt("camx", 0);
            cVars.putInt("camy", 0);
        }
	    else {
//            xCon.ex("load "+ sVars.get("defaultmap"));
            xCon.ex("load");
        }
        xCon.ex("exec " + sVars.get("defaultexec"));
        uiMenus.menuSelection[uiMenus.MENU_CONTROLS].items = uiMenusControls.getControlsMenuItems();
        startTicker();
	}

	public static void exit() {
        xCon.ex(String.format("playsound %s", Math.random() > 0.5 ? "sounds/shout.wav" : "sounds/death.wav"));
        sVars.saveFile(sSettings.CONFIG_FILE_LOCATION);
        if(sVars.isOne("debuglog"))
            xCon.instance().saveLog(sSettings.net_server
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
