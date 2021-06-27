import java.awt.*;

public class uiInterface {
    static boolean inplay = sVars.isZero("startpaused");
    static long gameTime = System.currentTimeMillis();
    private static long gameTimeNanos = System.nanoTime();
    private static long tickCounterTime = gameTime;
    private static long tickTimeNanos = gameTimeNanos;
    private static long framecounterTime = gameTime;
    static long nettickcounterTimeClient = gameTime;
    static long nettickcounterTimeServer = gameTime;
    static int tickReport = 0;
    static int fpsReport = 0;
    static int netReportClient = 0;
    static int netReportServer = 0;
    static int[] camReport = new int[]{0,0};
    private static int frames = 0;
    static String uuid = eManager.createId();

    private static void startGame() {
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

    public static void init(String[] launch_args) {
        //without this, holding any key, e.g. W to move, will eventually lock ALL controls.  on a mac of course
        eUtils.disableApplePressAndHold();
        sVars.loadFromFile(sSettings.CONFIG_FILE_LOCATION);
        sVars.readLaunchArguments(launch_args);
        eManager.mapsSelection = eManager.getFilesSelection("maps", ".map");
        uiMenus.menuSelection[uiMenus.MENU_MAP].setupMenuItems();
        eManager.winClipSelection = eManager.getFilesSelection(eUtils.getPath("sounds/win"));
        eManager.prefabSelection = eManager.getFilesSelection("prefabs");
        xCon.ex("exec config/autoexec.cfg");
        //finish loading args
        if(!sVars.isOne("showmapmakerui")) {
            sVars.putInt("drawhitboxes", 0);
            sVars.putInt("drawmapmakergrid", 0);
            sVars.putInt("showcam", 0);
            sVars.putInt("showfps", 0);
            sVars.putInt("showmouse", 0);
            sVars.putInt("shownet", 0);
            sVars.putInt("showplayer", 0);
            sVars.putInt("showscale", 0);
            sVars.putInt("showtick", 0);
        }
        else {
            sSettings.show_mapmaker_ui = true;
            eUtils.zoomLevel = 0.5;
        }
        uiMenus.menuSelection[uiMenus.MENU_CONTROLS].items = uiMenusControls.getControlsMenuItems();
        oDisplay.instance().showFrame();
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
        int[] fabdims = uiEditorMenus.getNewPrefabDims();
        int pfx = eUtils.roundToNearest(eUtils.unscaleInt(mc[0])+cVars.getInt("camx") - fabdims[0]/2,
                uiEditorMenus.snapToX);
        int pfy = eUtils.roundToNearest(eUtils.unscaleInt(mc[1])+cVars.getInt("camy") - fabdims[1]/2,
                uiEditorMenus.snapToY);
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

    public static void exit() {
        sVars.saveFile(sSettings.CONFIG_FILE_LOCATION);
        if(sVars.isOne("debuglog"))
            xCon.instance().saveLog(sSettings.CONSOLE_LOG_LOCATION);
        System.exit(0);
    }
}
