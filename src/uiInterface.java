import java.awt.MouseInfo;

public class uiInterface {
    static boolean inplay = false; //must be FALSE for mapmaker to work right
    static boolean inconsole = false;
    static int tickReport = 0;
    static int fpsReport = 0;
    static int tickReportClient = 0;
    static int netReportClient = 0;
    static int tickReportServer = 0;
    static int netReportServer = 0;
    static int frames = 0;
    static String uuid = eUtils.createId();
    static boolean hideMouseUI = false;

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
        int[] fabdims = dMapmakerOverlay.getNewPrefabDims();
        int pfx = eUtils.roundToNearest(eUtils.unscaleInt(mc[0])+gCamera.getX() - fabdims[0]/2,
                uiEditorMenus.snapToX);
        int pfy = eUtils.roundToNearest(eUtils.unscaleInt(mc[1])+gCamera.getY() - fabdims[1]/2,
                uiEditorMenus.snapToY);
        return new int[]{pfx, pfy};
    }

    public static synchronized void getUIMenuItemUnderMouse() {
        if(!hideMouseUI) {
            int[] mc = uiInterface.getMouseCoordinates();
            int[] xBounds = new int[]{0, sSettings.width / 4};
            int[] yBounds = sSettings.displaymode > 0
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
                    if (sSettings.displaymode == oDisplay.displaymode_windowed) {
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
        cServerLogic.vars.saveToFile(sSettings.CONFIG_FILE_LOCATION_SERVER);
        cClientLogic.vars.saveToFile(sSettings.CONFIG_FILE_LOCATION_CLIENT);
        if(cClientLogic.debuglog)
            xCon.instance().saveLog(sSettings.CONSOLE_LOG_LOCATION);
        System.exit(0);
    }
}
