public class xComEditorDelTile extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        int toRemove = toks.length > 1 ? Integer.parseInt(toks[1]) : cEditorLogic.state.selectedTileId;
        if(eManager.currentMap.scene.tiles().size() > toRemove) {
            try {
                eManager.currentMap.scene.tiles().remove(eManager.currentMap.scene.tiles().get(toRemove));
                if(eManager.currentMap.scene.tiles().size() > 0)
                    xCon.ex(String.format("HIDDEN e_selecttile %d",
                        eManager.currentMap.scene.tiles().size() - 1));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }
}
