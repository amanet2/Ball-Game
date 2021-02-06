import java.util.Arrays;
import java.util.List;

public class xComEditorCopyTile extends xCom {
    public String doCommand(String fullCommand) {
        List dontCopyList = Arrays.asList("id", "coordx", "coordy");
        List spriteFieldList = Arrays.asList("sprite0", "sprite1", "sprite2");
        gThingTile t = eManager.currentMap.scene.tiles().get(cEditorLogic.state.selectedTileId);
        for(String k : t.vars().keySet()) {
            if(spriteFieldList.contains(k)) {
                cEditorLogic.state.newTile.put(k,
                        t.get(k).replaceFirst(sVars.get("datapath")+"/",""));
            }
            else if(!dontCopyList.contains(k)) {
                cEditorLogic.state.newTile.put(k, t.get(k));
            }
        }
        return "";
    }
}
