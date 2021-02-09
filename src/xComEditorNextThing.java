public class xComEditorNextThing extends xCom {
    public String doCommand(String fullCommand) {
        switch (cEditorLogic.state.createObjCode) {
            case gScene.THING_FLARE:
                xCon.ex(String.format("e_selectflare %d",
                        cEditorLogic.state.selectedFlareTag < eManager.currentMap.scene.flares().size() - 1 ?
                                cEditorLogic.state.selectedFlareTag + 1 : 0));
                return Integer.toString(cEditorLogic.state.selectedFlareTag);
            case gScene.THING_PROP:
                xCon.ex(String.format("e_selectprop %d",
                        cEditorLogic.state.selectedPropId < eManager.currentMap.scene.props().size() - 1 ?
                                cEditorLogic.state.selectedPropId + 1 : 0));
                return Integer.toString(cEditorLogic.state.selectedPropId);
            default:
                xCon.ex(String.format("e_selecttile %d",
                        cEditorLogic.state.selectedTileId < eManager.currentMap.scene.tiles().size() - 1 ?
                                cEditorLogic.state.selectedTileId + 1 : 0));
                return Integer.toString(cEditorLogic.state.selectedTileId);
        }
    }

    public String undoCommand(String fullCommand) {
        switch (cEditorLogic.state.createObjCode) {
            case gScene.THING_FLARE:
                xCon.ex(String.format("e_selectflare %s",
                        cEditorLogic.state.selectedFlareTag > 0 ? cEditorLogic.state.selectedFlareTag - 1
                                : eManager.currentMap.scene.flares().size() - 1));
                return Integer.toString(cEditorLogic.state.selectedFlareTag);
            case gScene.THING_PROP:
                xCon.ex(String.format("e_selectprop %d",
                        cEditorLogic.state.selectedPropId > 0 ? cEditorLogic.state.selectedPropId - 1
                                : eManager.currentMap.scene.props().size() - 1));
                return Integer.toString(cEditorLogic.state.selectedPropId);
            default:
                xCon.ex(String.format("e_selecttile %d",
                        cEditorLogic.state.selectedTileId > 0 ? cEditorLogic.state.selectedTileId - 1
                                : eManager.currentMap.scene.tiles().size() - 1));
                return Integer.toString(cEditorLogic.state.selectedTileId);
        }
    }
}
