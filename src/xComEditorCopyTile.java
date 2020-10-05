import java.util.Arrays;
import java.util.List;

public class xComEditorCopyTile extends xCom {
    public String doCommand(String fullCommand) {
        String[] dontcopy = new String[]{"id", "coordx", "coordy", "canspawn", "sprite0", "sprite1", "sprite2"};
        List dontCopyList = Arrays.asList(dontcopy);
        gTile t = eManager.currentMap.scene.tiles().get(cEditorLogic.state.selectedTileId);
        for(String k : t.vars().keySet()) {
            if(!dontCopyList.contains(k)) {
                cEditorLogic.state.newTile.put(k, t.get(k));
            }
        }
        return "";
    }
}
