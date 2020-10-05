public class xComEditorSelectTile extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if (toks.length > 1) {
            cEditorLogic.state.selectedTileId = Integer.parseInt(toks[1]);
            return "";
        }
        return "usage: e_selecttile <tileId>";
    }
}