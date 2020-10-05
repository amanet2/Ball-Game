public class xComEditorNextTile extends xCom {
    public String doCommand(String fullCommand) {
        xCon.ex(String.format("e_selecttile %d",
            cEditorLogic.state.selectedTileId < eManager.currentMap.scene.tiles().size()-1 ?
                cEditorLogic.state.selectedTileId+1 : 0));
        return "";
    }
}
