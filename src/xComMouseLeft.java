public class xComMouseLeft extends xCom {
    public String doCommand(String fullCommand) {
        if(oDisplay.instance().frame.hasFocus()) {
            if (uiInterface.inplay) {
                iMouse.holdingMouseLeft = true;
            }
            else {
                if(sSettings.show_mapmaker_ui) {
                    int[] mc = uiInterface.getMouseCoordinates();
                    if(cVars.get("newprefabname").length() > 0) {
                        int[] pfd = uiEditorMenus.getNewPrefabDims();
                        int w = pfd[0];
                        int h = pfd[1];
                        int pfx = eUtils.roundToNearest(eUtils.unscaleInt(mc[0]) + cVars.getInt("camx") - w / 2,
                                uiEditorMenus.snapToX);
                        int pfy = eUtils.roundToNearest(eUtils.unscaleInt(mc[1]) + cVars.getInt("camy") - h / 2,
                                uiEditorMenus.snapToY);
                        cVars.putInt("prefabid", cClientLogic.scene.getHighestPrefabId() + 1);
                        String cmd = String.format("exec prefabs/%s %d %d", cVars.get("newprefabname"), pfx, pfy);
                        nClient.instance().addNetCmd(cmd);
                        return "put prefab " + cVars.get("newprefabname");
                    }
                    if(cVars.get("newitemname").length() > 0) {
                        int iw = 300;
                        int ih = 300;
                        int ix = eUtils.roundToNearest(eUtils.unscaleInt(mc[0]) + cVars.getInt("camx") - iw/2,
                                uiEditorMenus.snapToX);
                        int iy = eUtils.roundToNearest(eUtils.unscaleInt(mc[1]) + cVars.getInt("camy") - ih/2,
                                uiEditorMenus.snapToY);
                        cVars.putInt("itemid", cClientLogic.scene.getHighestItemId() + 1);
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
