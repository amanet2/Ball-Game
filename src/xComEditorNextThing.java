public class xComEditorNextThing extends xCom {
    public String doCommand(String fullCommand) {
        switch (cEditorLogic.state.createObjCode) {
            case gScene.THING_FLARE:
                xCon.ex(String.format("e_selectflare %d",
                        cEditorLogic.state.selectedFlareTag < eManager.currentMap.scene.flares().size() - 1 ?
                                cEditorLogic.state.selectedFlareTag + 1 : 0));
                return Integer.toString(cEditorLogic.state.selectedFlareTag);
        }
        return Integer.toString(0);
    }

    public String undoCommand(String fullCommand) {
        switch (cEditorLogic.state.createObjCode) {
            case gScene.THING_FLARE:
                xCon.ex(String.format("e_selectflare %s",
                        cEditorLogic.state.selectedFlareTag > 0 ? cEditorLogic.state.selectedFlareTag - 1
                                : eManager.currentMap.scene.flares().size() - 1));
                return Integer.toString(cEditorLogic.state.selectedFlareTag);
        }
        return Integer.toString(0);
    }
}
