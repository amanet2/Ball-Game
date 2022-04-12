public class xMain {
	public static void main(String[] args) {
		eManager.configSelection = eManager.getFilesSelection("config");
		eManager.prefabSelection = eManager.getFilesSelection("prefabs");
		eManager.mapsSelection = eManager.getFilesSelection("maps", ".map");
		eManager.winClipSelection = eManager.getFilesSelection(eUtils.getPath("sounds/win"));
		gExecDoableFactory.instance().init();
		cServerVars.instance().init();
		cClientVars.instance().init();
		xCon.ex("exec config/server.cfg");
		xCon.ex("exec config/client.cfg");
		xCon.ex("exec config/autoexec.cfg");
		cServerVars.instance().loadFromFile(sSettings.CONFIG_FILE_LOCATION_SERVER);
		cClientVars.instance().loadFromFile(sSettings.CONFIG_FILE_LOCATION_CLIENT);
		cServerVars.instance().loadFromLaunchArgs(args);
		cClientVars.instance().loadFromLaunchArgs(args);
		//without this, holding any key, e.g. W to move, will eventually lock ALL controls on a mac
		eUtils.disableApplePressAndHold();
		uiMenus.menuSelection[uiMenus.MENU_MAP].setupMenuItems();
		uiMenus.menuSelection[uiMenus.MENU_CONTROLS].items = uiMenusControls.getControlsMenuItems();
		//finish loading args
		if(!sSettings.show_mapmaker_ui) {
			sSettings.drawhitboxes = false;
			sSettings.drawmapmakergrid = false;
		}
		else {
			eUtils.zoomLevel = 0.5;
		}
		oDisplay.instance().showFrame();
		int ticks = 0;
		while(true) {
			try {
				uiInterface.gameTime = System.currentTimeMillis();
				uiInterface.gameTimeNanos = System.nanoTime();
				//game loop
				while(uiInterface.tickTimeNanos < uiInterface.gameTimeNanos) {
//                while(tickTime < gameTime) {
					uiInterface.tickTimeNanos += (1000000000/sSettings.rategame);
//                    tickTime += 1000/sSettings.rategame;
					iInput.readKeyInputs();
					if(sSettings.IS_SERVER)
						cServerLogic.gameLoop();
					if(sSettings.IS_CLIENT)
						cClientLogic.gameLoop();
					uiInterface.camReport[0] = gCamera.getX();
					uiInterface.camReport[1] = gCamera.getY();
					ticks += 1;
					if(uiInterface.tickCounterTime < uiInterface.gameTime) {
						uiInterface.tickReport = ticks;
						ticks = 0;
						uiInterface.tickCounterTime = uiInterface.gameTime + 1000;
					}
				}
				//draw gfx
				oDisplay.instance().frame.repaint();
				long lastFrameTime = System.currentTimeMillis();
				if (uiInterface.framecounterTime < lastFrameTime) {
					uiInterface.fpsReport = uiInterface.frames;
					uiInterface.frames = 0;
					uiInterface.framecounterTime = lastFrameTime + 1000;
				}
				// framerate limit
				if(sSettings.framerate > 0) {
					long nextFrameTime = (uiInterface.gameTimeNanos + (1000000000/sSettings.framerate));
					while (nextFrameTime >= System.nanoTime()) {
						; // do nothing
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
