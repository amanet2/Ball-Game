public class xComMouseLeft extends xCom {
    public String doCommand(String fullCommand) {
        if (uiInterface.inplay) {
//            xCon.ex("attack");
            iMouse.holdingMouseLeft = true;
        }
        else {
            if(sSettings.show_mapmaker_ui) {
                int[] mc = cScripts.getMouseCoordinates();
                switch(cEditorLogic.state.createObjCode) {
                    case gScene.THING_PROP:
                        xCon.ex(String.format("e_putprop %s %d %d %d %d %s %s",
                                cEditorLogic.state.newProp.get("code"),
                                cEditorLogic.state.newProp.getInt("int0"),
                                cEditorLogic.state.newProp.getInt("int1"),
                                eUtils.roundToNearest(eUtils.unscaleInt(mc[0]) + cVars.getInt("camx")
                                        - cEditorLogic.state.newProp.getInt("dimw")/2, cEditorLogic.state.snapToX),
                                eUtils.roundToNearest(eUtils.unscaleInt(mc[1]) + cVars.getInt("camy")
                                        - cEditorLogic.state.newProp.getInt("dimh")/2, cEditorLogic.state.snapToY),
                                cEditorLogic.state.newProp.get("dimw"), cEditorLogic.state.newProp.get("dimh")));
                        break;
                    case gScene.THING_FLARE:
                        xCon.ex(String.format("e_putflare %d %d %s %s %s %s %s %s %s %s %s %s",
                                mc[0], mc[1], cEditorLogic.state.newFlare.get("dimw"),
                                cEditorLogic.state.newFlare.get("dimh"),
                                cEditorLogic.state.newFlare.get("r1"), cEditorLogic.state.newFlare.get("g1"),
                                cEditorLogic.state.newFlare.get("b1"), cEditorLogic.state.newFlare.get("a1"),
                                cEditorLogic.state.newFlare.get("r2"), cEditorLogic.state.newFlare.get("g2"),
                                cEditorLogic.state.newFlare.get("b2"), cEditorLogic.state.newFlare.get("a2")));
                        break;
                    default:
                        xCon.ex(
                            String.format("e_puttile %d %d %d %d %d %d %d %d %d %d %d %s %s %s %s",
                                mc[0], mc[1], cEditorLogic.state.newTile.getInt("dimw"),
                                    cEditorLogic.state.newTile.getInt("dimh"),
                                    cEditorLogic.state.newTile.getInt("dim0h"),
                                    cEditorLogic.state.newTile.getInt("dim1h"),
                                    cEditorLogic.state.newTile.getInt("dim2h"),
                                    cEditorLogic.state.newTile.getInt("dim3h"),
                                    cEditorLogic.state.newTile.getInt("dim4h"),
                                    cEditorLogic.state.newTile.getInt("dim5w"),
                                    cEditorLogic.state.newTile.getInt("dim6w"),
                                    cEditorLogic.state.newTile.get("sprite0"),
                                    cEditorLogic.state.newTile.get("sprite1"),
                                    cEditorLogic.state.newTile.get("sprite2"),
                                    cEditorLogic.state.newTile.get("brightness")));
                        break;
                }
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
