public class xComEditorPrevTile extends xCom {
    public String doCommand(String fullCommand) {
        xCon.ex(String.format("e_selecttile %d",
            cEditorLogic.state.selectedTileId >0 ? cEditorLogic.state.selectedTileId-1
                : eManager.currentMap.scene.tiles().size()-1));
        return "";
    }
}
