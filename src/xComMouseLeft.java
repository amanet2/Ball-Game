public class xComMouseLeft extends xCom {
    public String doCommand(String fullCommand) {
        if (uiInterface.inplay) {
//            xCon.ex("attack");
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
                    xCon.ex(String.format("exec %s/prefabs/%s %d %d",
                            sVars.get("datapath"), cVars.get("newprefabname"),
                            pfx, pfy));
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
//                switch(cEditorLogic.state.createObjCode) {
//                    case gScene.THING_FLARE:
//                        int fw = cEditorLogic.state.newFlare.getInt("dimw");
//                        int fh = cEditorLogic.state.newFlare.getInt("dimh");
//                        int fx = eUtils.roundToNearest(eUtils.unscaleInt(mc[0]) + cVars.getInt("camx") - fw/2,
//                                cEditorLogic.state.snapToX);
//                        int fy = eUtils.roundToNearest(eUtils.unscaleInt(mc[1]) + cVars.getInt("camy") - fh/2,
//                                cEditorLogic.state.snapToY);
//                        xCon.ex(String.format("putflare %d %d %s %s %s %s %s %s %s %s %s %s",
//                                fx, fy, fw, fh,
//                                cEditorLogic.state.newFlare.get("r1"), cEditorLogic.state.newFlare.get("g1"),
//                                cEditorLogic.state.newFlare.get("b1"), cEditorLogic.state.newFlare.get("a1"),
//                                cEditorLogic.state.newFlare.get("r2"), cEditorLogic.state.newFlare.get("g2"),
//                                cEditorLogic.state.newFlare.get("b2"), cEditorLogic.state.newFlare.get("a2")));
//                        break;
//                    case gScene.THING_PREFAB:
//                        int[] pfd = cScripts.getNewPrefabDims();
//                        int w = pfd[0];
//                        int h = pfd[1];
//                        int pfx = eUtils.roundToNearest(eUtils.unscaleInt(mc[0]) + cVars.getInt("camx") - w/2,
//                                cEditorLogic.state.snapToX);
//                        int pfy = eUtils.roundToNearest(eUtils.unscaleInt(mc[1]) + cVars.getInt("camy") - h/2,
//                                cEditorLogic.state.snapToY);
//                        cVars.putInt("prefabid", eManager.currentMap.scene.prefabIdCtr);
//                        xCon.ex(String.format("exec %s/prefabs/%s %d %d",
//                                sVars.get("datapath"), cVars.get("newprefabname"),
//                                pfx, pfy));
//                        eManager.currentMap.scene.prefabIdCtr++;
//                        break;
//                    case gScene.THING_ITEM:
//                        int iw = 300;
//                        int ih = 300;
//                        int ix = eUtils.roundToNearest(eUtils.unscaleInt(mc[0]) + cVars.getInt("camx") - iw/2,
//                                cEditorLogic.state.snapToX);
//                        int iy = eUtils.roundToNearest(eUtils.unscaleInt(mc[1]) + cVars.getInt("camy") - ih/2,
//                                cEditorLogic.state.snapToY);
//                        cVars.putInt("itemid", eManager.currentMap.scene.itemIdCtr);
//                        xCon.ex(String.format("putitem %s %d %d", cVars.get("newitemname"), ix, iy));
//                        eManager.currentMap.scene.itemIdCtr++;
//                        break;
//                }
            }
            else if(uiMenus.gobackSelected) {
                xCon.ex("gobackui");
            }
            else if(uiMenus.menuSelection[uiMenus.selectedMenu].selectedItem > -1) {
                xCon.ex("activateui");
            }
        }
        return fullCommand;
    }

    public String undoCommand(String fullCommand) {
        iMouse.holdingMouseLeft = false;
        return fullCommand;
    }
}
