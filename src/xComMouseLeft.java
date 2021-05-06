public class xComMouseLeft extends xCom {
    public String doCommand(String fullCommand) {
        if(oDisplay.instance().frame.hasFocus()) {
            if (uiInterface.inplay) {
                iMouse.holdingMouseLeft = true;
            }
            else {
                if(sSettings.show_mapmaker_ui) {
                    int[] mc = cScripts.getMouseCoordinates();
                    if(cVars.get("newprefabname").length() > 0) {
                        int[] pfd = cScripts.getNewPrefabDims();
                        int w = pfd[0];
                        int h = pfd[1];
                        int pfx = eUtils.roundToNearest(eUtils.unscaleInt(mc[0]) + cVars.getInt("camx") - w / 2,
                                cEditorLogic.state.snapToX);
                        int pfy = eUtils.roundToNearest(eUtils.unscaleInt(mc[1]) + cVars.getInt("camy") - h / 2,
                                cEditorLogic.state.snapToY);
                        cVars.putInt("prefabid", eManager.currentMap.scene.getHighestPrefabId());
                        String cmd = String.format("exec %s/prefabs/%s %d %d",
                                sVars.get("datapath"), cVars.get("newprefabname"),
                                pfx, pfy);
                        cGameLogic.doCommand(cmd);
                        return "put prefab " + cVars.get("newprefabname");
                    }
                    if(cVars.get("newitemname").length() > 0) {
                        int iw = 300;
                        int ih = 300;
                        int ix = eUtils.roundToNearest(eUtils.unscaleInt(mc[0]) + cVars.getInt("camx") - iw/2,
                                cEditorLogic.state.snapToX);
                        int iy = eUtils.roundToNearest(eUtils.unscaleInt(mc[1]) + cVars.getInt("camy") - ih/2,
                                cEditorLogic.state.snapToY);
                        cVars.putInt("itemid", eManager.currentMap.scene.itemIdCtr);
                        xCon.ex(String.format("putitem %s %d %d", cVars.get("newitemname"), ix, iy));
                        eManager.currentMap.scene.itemIdCtr++;
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
