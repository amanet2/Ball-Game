import java.awt.*;
import java.util.List;
import java.util.Arrays;

public class xComEditorPasteTile extends xCom {
    public String doCommand(String fullCommand) {
        String[] dontcopy = new String[]{"id", "coordx", "coordy", "sprite0", "sprite1", "sprite2"};
        List dontCopyList = Arrays.asList(dontcopy);
        gTile p = eManager.currentMap.scene.tiles().get(cEditorLogic.state.selectedTileId);
        for(String k : cEditorLogic.state.newTile.vars().keySet()) {
            if(!dontCopyList.contains(k)) {
                p.put(k, cEditorLogic.state.newTile.get(k));
            }
        }
        p.sprites = new Image[] {
                gTextures.getScaledImage(p.get("sprite0"), p.getInt("dim0w"), p.getInt("dim0h")),
                gTextures.getScaledImage(p.get("sprite1"), p.getInt("dim1w"), p.getInt("dim1h")),
                gTextures.getScaledImage(p.get("sprite2"), p.getInt("dim2w"), p.getInt("dim2h")),
                gTextures.getScaledImage(p.get("sprite0"), p.getInt("dim3w"), p.getInt("dim3h")),
                gTextures.getScaledImage(p.get("sprite1"), p.getInt("dim4w"), p.getInt("dim4h")),
                gTextures.getScaledImage(p.get("sprite0"), p.getInt("dim5w"), p.getInt("dim5h")),
                gTextures.getScaledImage(p.get("sprite0"), p.getInt("dim6w"), p.getInt("dim6h"))
        };
        return "";
    }
}
