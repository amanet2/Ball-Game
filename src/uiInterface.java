import java.net.DatagramSocket;

public class uiInterface {
	static boolean inplay = sVars.isZero("startpaused");
	static long gameTime = System.currentTimeMillis();
	static long gameTimeNanos = System.nanoTime();
	static long tickCounterTime = gameTime;
	static long tickTime = gameTime;
	static long framecounterTime = gameTime;
	static long nettickcounterTime = gameTime;
	static long networkTime = gameTime;
	static int tickReport = 0;
	static int fpsReport = 0;
	static int netReport = 0;
	static int[] camReport = new int[]{0,0};
    static int frames = 0;

    static DatagramSocket serverSocket = null;
    static DatagramSocket clientSocket = null;
    static String uuid = cScripts.createID(8);

	public static void startTicker() {
	    int ticks = 0;

        cScripts.setupGame();
		while(true) {
            try {
                //inits
//                if(!vFrameFactory.instance().isAlive())
//                    vFrameFactory.instance().start();
                if(sSettings.net_server && !nServer.instance().isAlive())
                    nServer.instance().start();
                else if(sSettings.net_client && !nClient.instance().isAlive())
                    nClient.instance().start();
                gameTime = System.currentTimeMillis();
                gameTimeNanos = System.nanoTime();
                //game loop
                if(sSettings.net_server && cVars.getInt("timeleft") > 0)
                    xCon.ex(String.format("cv_timeleft %d",
                            xCon.getLong("timelimit") - (int) (gameTime - xCon.getLong("cv_starttime"))));
                if(sSettings.net_server && cVars.contains("serveraddbots")
                        && cVars.getLong("serveraddbotstime") < gameTime) {
                    nServer.addBots();
                    cVars.remove("serveraddbots");
                }
                while(tickTime < gameTime) {
                    tickTime += (1000/cVars.getInt("gametick"));
                    oDisplay.instance().checkDisplay();
                    oAudio.instance().checkAudio();
                    iInput.readKeyInputs();
                    gCamera.updatePosition();
                    if(sSettings.net_server)
                        nServer.processPackets();
                    if(sSettings.net_client)
                        nClient.processPackets();
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
//                Image[] toAdd = new Image[]{
//                vFrameFactory.showImage = vFrameFactory.showImage == 1 ? 2 : 1;
//
//                vFrameFactory.createFrame(0);
//                vFrameFactory.createFrame(1);
//                };
//                vFrameFactory.instance().frameImageQueue.add(toAdd);
//                while(vFrameFactory.instance().frameImageQueue.size() > 1) {
//                    vFrameFactory.instance().frameImageQueue.remove();
//                }
                oDisplay.instance().frame.repaint();
                frames += 1;
                if (framecounterTime < uiInterface.gameTime) {
                    fpsReport = frames;
                    frames = 0;
                    framecounterTime = gameTime + 1000;
                }
                //sleep
                if(sVars.isOne("lowpowermode")) {
                    if (sSettings.framerate > 999) {
                        long toSleepNanos = 1000000 / sSettings.framerate - (System.nanoTime() - gameTimeNanos);
                        Thread.sleep(0, Math.max(0, (int) toSleepNanos));
                    } else {
                        long toSleepMillis = 1000 / sSettings.framerate - (System.currentTimeMillis() - gameTime);
                        Thread.sleep(Math.max(0, toSleepMillis));
                    }
                }
            } catch (Exception e) {
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

	public static void startNew() {
        eManager.getMapsSelection();
	    if(sSettings.show_mapmaker_ui) {
            xCon.ex("load");
            xCon.ex("cv_camx -6000");
            xCon.ex("cv_camy -6000");
        }
	    else {
            xCon.ex("load "+ sVars.get("defaultmap"));
        }
        xCon.ex("exec " + sVars.get("defaultexec"));
        uiMenus.menuSelection[uiMenus.MENU_CONTROLS].items = uiMenusControls.getControlsMenuItems();
        startTicker();
	}

	public static void exit() {
        xCon.ex(String.format("playsound %s", Math.random() > 0.5 ? "sounds/shout.wav" : "sounds/death.wav"));
        sVars.saveFile(sSettings.CONFIG_FILE_LOCATION);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
