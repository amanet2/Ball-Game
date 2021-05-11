import java.awt.*;

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
    static String uuid = eManager.createId();

	public static void startGame() {
	    int ticks = 0;
        while(true) {
            try {
                gameTime = System.currentTimeMillis();
                gameTimeNanos = System.nanoTime();
                //game loop
                while(tickTimeNanos < gameTimeNanos) {
                    //nano = billion
                    tickTimeNanos += (1000000000/cVars.getInt("gametick"));
                    iInput.readKeyInputs();
                    if(sSettings.IS_SERVER)
                        cServerLogic.gameLoop();
                    if(sSettings.IS_CLIENT)
                        cClientLogic.gameLoop();
                    camReport[0] = cVars.getInt("camx");
                    camReport[1] = cVars.getInt("camy");
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
                    while (nextFrameTime >= System.nanoTime()); // do nothing
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

    public static int[] getMouseCoordinates() {
        return new int[]{
                MouseInfo.getPointerInfo().getLocation().x - oDisplay.instance().frame.getLocationOnScreen().x
                        - oDisplay.instance().getContentPaneOffsetDimension()[0],
                MouseInfo.getPointerInfo().getLocation().y - oDisplay.instance().frame.getLocationOnScreen().y
                        - oDisplay.instance().getContentPaneOffsetDimension()[1]
        };
    }

    public static int[] getPlaceObjCoords() {
        int[] mc = getMouseCoordinates();
        int[] fabdims = cEditorLogic.getNewPrefabDims();
        int pfx = eUtils.roundToNearest(eUtils.unscaleInt(mc[0])+cVars.getInt("camx") - fabdims[0]/2,
                cEditorLogic.snapToX);
        int pfy = eUtils.roundToNearest(eUtils.unscaleInt(mc[1])+cVars.getInt("camy") - fabdims[1]/2,
                cEditorLogic.snapToY);
        return new int[]{pfx, pfy};
    }

    public static synchronized void getUIMenuItemUnderMouse() {
        if(cVars.isZero("blockmouseui")) {
            int[] mc = uiInterface.getMouseCoordinates();
            int[] xBounds = new int[]{0, sSettings.width / 4};
            int[] yBounds = sVars.getInt("displaymode") > 0
                    ? new int[]{14 * sSettings.height / 16, 15 * sSettings.height / 16}
                    : new int[]{15 * sSettings.height / 16, sSettings.height};
            if ((mc[0] >= xBounds[0] && mc[0] <= xBounds[1]) && (mc[1] >= yBounds[0] && mc[1] <= yBounds[1])) {
                if (!uiMenus.gobackSelected) {
                    uiMenus.gobackSelected = true;
                    uiMenus.menuSelection[uiMenus.selectedMenu].selectedItem = -1;
                }
                return;
            } else
                uiMenus.gobackSelected = false;
            if (uiMenus.selectedMenu != uiMenus.MENU_CONTROLS) {
                for (int i = 0; i < uiMenus.menuSelection[uiMenus.selectedMenu].items.length; i++) {
                    xBounds = new int[]{sSettings.width / 2 - sSettings.width / 8,
                            sSettings.width / 2 + sSettings.width / 8};
                    yBounds = new int[]{11 * sSettings.height / 30 + i * sSettings.height / 30,
                            11 * sSettings.height / 30 + (i + 1) * sSettings.height / 30};
                    if (sVars.isIntVal("displaymode", oDisplay.displaymode_windowed)) {
                        yBounds[0] += 40;
                        yBounds[1] += 40;
                    }
                    if ((mc[0] >= xBounds[0] && mc[0] <= xBounds[1]) && (mc[1] >= yBounds[0] && mc[1] <= yBounds[1])) {
                        if (uiMenus.menuSelection[uiMenus.selectedMenu].selectedItem != i)
                            uiMenus.menuSelection[uiMenus.selectedMenu].selectedItem = i;
                        return;
                    }
                }
            }
            uiMenus.menuSelection[uiMenus.selectedMenu].selectedItem = -1;
        }
    }

    public static synchronized void selectThingUnderMouse() {
        int[] mc = uiInterface.getMouseCoordinates();
        for(String id : eManager.currentMap.scene.getThingMap("THING_ITEM").keySet()) {
            gThing item = eManager.currentMap.scene.getThingMap("THING_ITEM").get(id);
            if(item.contains("itemid") && item.coordsWithinBounds(mc[0], mc[1])) {
                cVars.put("selecteditemid", item.get("itemid"));
                cVars.put("selecteditemname", item.get("type"));
                cVars.put("selectedprefabid", "");
                cVars.put("selectedprefabname", "");
                return;
            }
        }
        for(String id : eManager.currentMap.scene.getThingMap("THING_BLOCK").keySet()) {
            gThing block = eManager.currentMap.scene.getThingMap("THING_BLOCK").get(id);
            if(block.contains("prefabid") && block.coordsWithinBounds(mc[0], mc[1])) {
                cVars.put("selectedprefabid", block.get("prefabid"));
                cVars.put("selectedprefabname", block.get("prefabname"));
                cVars.put("selecteditemid", "");
                cVars.put("selecteditemname", "");
                return;
            }
        }
        cVars.put("selectedprefabid", "");
        cVars.put("selecteditemid", "");
    }

	public static void exit() {
        xCon.ex(String.format("playsound %s", Math.random() > 0.5 ? "sounds/shout.wav" : "sounds/death.wav"));
        sVars.saveFile(sSettings.CONFIG_FILE_LOCATION);
        if(sVars.isOne("debuglog"))
            xCon.instance().saveLog(sSettings.CONSOLE_LOG_LOCATION);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            eUtils.echoException(e);
            e.printStackTrace();
        }
        System.exit(0);
    }
}
