public class xComMouseLeft extends xCom {
    public String doCommand(String fullCommand) {
        if(oDisplay.instance().frame.hasFocus()) {
            if (uiInterface.inplay) {
                iMouse.holdingMouseLeft = true;
            }
            else {
                if(sSettings.show_mapmaker_ui && cVars.isOne("maploaded")) {
                    int[] mc = uiInterface.getMouseCoordinates();
                    if(cVars.get("newprefabname").length() > 0) {
                        int[] pfd = dMapmakerOverlay.getNewPrefabDims();
                        int w = pfd[0];
                        int h = pfd[1];
                        int pfx = eUtils.roundToNearest(eUtils.unscaleInt(mc[0]) + gCamera.getX() - w / 2,
                                uiEditorMenus.snapToX);
                        int pfy = eUtils.roundToNearest(eUtils.unscaleInt(mc[1]) + gCamera.getY() - h / 2,
                                uiEditorMenus.snapToY);
                        String cmd = String.format("exec prefabs/%s %d %d", cVars.get("newprefabname"), pfx, pfy);
                        nClient.instance().addNetCmd(cmd);
                        return "put prefab " + cVars.get("newprefabname");
                    }
                    if(cVars.get("newitemname").length() > 0) {
                        int iw = 300;
                        int ih = 300;
                        int ix = eUtils.roundToNearest(eUtils.unscaleInt(mc[0]) + gCamera.getX() - iw/2,
                                uiEditorMenus.snapToX);
                        int iy = eUtils.roundToNearest(eUtils.unscaleInt(mc[1]) + gCamera.getY() - ih/2,
                                uiEditorMenus.snapToY);
                        String cmd = String.format("putitem %s %d %d", cVars.get("newitemname"), ix, iy);
                        nClient.instance().addNetCmd(cmd);
                        return "put item " + cVars.get("newitemname");
                    }
                }
                else if(uiMenus.gobackSelected) {
                    xCon.ex("gobackui");
                }
                else if(uiMenus.menuSelection[uiMenus.selectedMenu].selectedItem > -1) {
                    xCon.ex("activateui");
                }
            }
        }
        return fullCommand;
    }

    public String undoCommand(String fullCommand) {
        iMouse.holdingMouseLeft = false;
        return fullCommand;
    }
}
