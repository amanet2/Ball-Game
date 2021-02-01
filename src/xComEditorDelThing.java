public class xComEditorDelThing extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        int toRemove = -1;
        String toRemoveId = "";
        switch (cEditorLogic.state.createObjCode) {
            case gScene.THING_FLARE:
                toRemoveId = toks.length > 1 ? toks[1] : cEditorLogic.state.selectedFlareId;
                try {
                    eManager.currentMap.scene.getThingMap("THING_FLARE").remove(toRemoveId);
                    //selects another flare after deletion of flare
//                        if(eManager.currentMap.scene.getThingMap("THING_FLARE").size() > 0)
//                            xCon.ex(String.format("HIDDEN e_selectflare %d",
//                                    eManager.currentMap.scene.flares().size() - 1));
                } catch (Exception e) {
                    eUtils.echoException(e);
                    e.printStackTrace();
                }
                break;
            case gScene.THING_PROP:
                toRemove = toks.length > 1 ? Integer.parseInt(toks[1]) : cEditorLogic.state.selectedPropId;
                if(eManager.currentMap.scene.props().size() > toRemove) {
                    try {
                        eManager.currentMap.scene.props().remove(eManager.currentMap.scene.props().get(toRemove));
                        if(eManager.currentMap.scene.props().size() > 0)
                            xCon.ex(String.format("HIDDEN e_selectprop %d",
                                    eManager.currentMap.scene.props().size() - 1));
                    } catch (Exception e) {
                        eUtils.echoException(e);
                        e.printStackTrace();
                    }
                }
                break;
            default:
                toRemove = toks.length > 1 ? Integer.parseInt(toks[1]) : cEditorLogic.state.selectedTileId;
                if(eManager.currentMap.scene.tiles().size() > toRemove) {
                    try {
                        eManager.currentMap.scene.tiles().remove(eManager.currentMap.scene.tiles().get(toRemove));
                        if(eManager.currentMap.scene.tiles().size() > 0)
                            xCon.ex(String.format("HIDDEN e_selecttile %d",
                                    eManager.currentMap.scene.tiles().size() - 1));
                    } catch (Exception e) {
                        eUtils.echoException(e);
                        e.printStackTrace();
                    }
                }
                break;
        }
        return "deleted " + toRemove;
    }
}
