public class xComEditorTileDown extends xCom {
    public String doCommand(String fullCommand) {
        cScripts.moveTileDown(cEditorLogic.state.selectedTileId);
        return "tile moved down a layer";
    }
}
