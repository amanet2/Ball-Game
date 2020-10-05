public class xComEditorTileUp extends xCom {
    public String doCommand(String fullCommand) {
        cScripts.movetileUp(cEditorLogic.state.selectedTileId);
        return "tile moved up a layer";

    }
}
