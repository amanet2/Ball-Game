import java.awt.MouseInfo;

public class uiInterface {

    public static int[] getMouseCoordinates() {
        return new int[]{
                MouseInfo.getPointerInfo().getLocation().x - xMain.shellLogic.displayPane.frame.getLocationOnScreen().x,
                MouseInfo.getPointerInfo().getLocation().y - xMain.shellLogic.displayPane.frame.getLocationOnScreen().y
        };
    }

    public static int[] getPlaceObjCoords() {
        int[] mc = getMouseCoordinates();
        int[] fabdims = dHUD.getNewPrefabDims();
        int pfx = eUtils.roundToNearest(eUtils.unscaleInt(mc[0])+(int) gCamera.coords[0] - fabdims[0]/2,
                uiEditorMenus.snapToX);
        int pfy = eUtils.roundToNearest(eUtils.unscaleInt(mc[1])+(int) gCamera.coords[1] - fabdims[1]/2,
                uiEditorMenus.snapToY);
        return new int[]{pfx, pfy};
    }

    public static synchronized void getUIMenuItemUnderMouse() {
        if(!sSettings.hideMouseUI) {
            int[] mc = uiInterface.getMouseCoordinates();
            int[] xBounds = new int[]{0, sSettings.width / 4};
            int[] yBounds = sSettings.borderless
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
                    if (!sSettings.borderless) {
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
}
