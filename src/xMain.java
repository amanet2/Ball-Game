public class xMain {
	private static long gameTimeNanos = System.nanoTime();
	private static long tickTimeNanos = gameTimeNanos;
	static long gameTime = System.currentTimeMillis();
	private static long framecounterTime = gameTime;
	public static void main(String[] args) {
		eManager.configFileSelection = eManager.getFilesSelection("config");
		eManager.prefabFileSelection = eManager.getFilesSelection("prefabs");
		eManager.mapsFileSelection = eManager.getFilesSelection("maps", ".map");
		eManager.winSoundFileSelection = eManager.getFilesSelection(eUtils.getPath("sounds/win"));
		gExecDoableFactory.instance().init();
		cServerVars.instance().init();
		cClientVars.instance().init();
		xCon.ex("exec "+sSettings.CONFIG_FILE_LOCATION_SERVER);
		xCon.ex("exec "+sSettings.CONFIG_FILE_LOCATION_CLIENT);
		xCon.ex("exec config/autoexec.cfg");
		cServerVars.instance().loadFromFile(sSettings.CONFIG_FILE_LOCATION_SERVER);
		cClientVars.instance().loadFromFile(sSettings.CONFIG_FILE_LOCATION_CLIENT);
		cServerVars.instance().loadFromLaunchArgs(args);
		cClientVars.instance().loadFromLaunchArgs(args);
		//without this, holding any key, e.g. W to move, will eventually lock ALL controls on a mac
		uiMenus.menuSelection[uiMenus.MENU_MAP].setupMenuItems();
		uiMenus.menuSelection[uiMenus.MENU_CONTROLS].items = uiMenusControls.getControlsMenuItems();
		//finish loading args
		if(sSettings.show_mapmaker_ui) {
			sSettings.drawhitboxes = true;
			sSettings.drawmapmakergrid = true;
			eUtils.zoomLevel = 0.5;
		}
		oDisplay.instance().showFrame();
		int ticks = 0;
		while(true) {
			try {
				gameTime = System.currentTimeMillis();
				gameTimeNanos = System.nanoTime();
				//game loop
				while(tickTimeNanos < gameTimeNanos) {
					tickTimeNanos += (1000000000/sSettings.rategame);
					iInput.readKeyInputs();
					if(sSettings.IS_SERVER)
						cServerLogic.gameLoop();
					if(sSettings.IS_CLIENT)
						cClientLogic.gameLoop();
					uiInterface.camReport[0] = gCamera.getX();
					uiInterface.camReport[1] = gCamera.getY();
					ticks += 1;
					if(uiInterface.tickCounterTime < gameTime) {
						uiInterface.tickReport = ticks;
						ticks = 0;
						uiInterface.tickCounterTime = gameTime + 1000;
					}
				}
				//draw gfx
				oDisplay.instance().frame.repaint();
				long lastFrameTime = System.currentTimeMillis();
				if (framecounterTime < lastFrameTime) {
					uiInterface.fpsReport = uiInterface.frames;
					uiInterface.frames = 0;
					framecounterTime = lastFrameTime + 1000;
				}
				// framerate limit
				if(sSettings.framerate > 0) {
					long nextFrameTime = (gameTimeNanos + (1000000000/sSettings.framerate));
					while (nextFrameTime >= System.nanoTime()) {
						; // do nothing
						// power saving
						try {
							Thread.sleep(1);
						}
						catch (InterruptedException ie) {}
					}
				}
				//power saving
//                    long toSleep = (gameTime + (1000/sSettings.framerate)) - System.currentTimeMillis();
//                    if(toSleep > 0)
//                        Thread.sleep(toSleep);
			}
			catch (Exception e) {
				eUtils.echoException(e);
				e.printStackTrace();
			}
		}
	}
}
