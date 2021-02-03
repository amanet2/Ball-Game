import java.util.TreeMap;

public class xComEditorNextThing extends xCom {
    public String doCommand(String fullCommand) {
        switch (cEditorLogic.state.createObjCode) {
            case gScene.THING_FLARE:
                TreeMap orderedFlareMap = new TreeMap<>();
                orderedFlareMap.putAll(eManager.currentMap.scene.getThingMap("THING_FLARE"));
                String nextSelectedFlareId = "";
                String firstFlareId = "";
                int idIndex = 0;
                int c = 0;
                for(Object id : orderedFlareMap.keySet()) {
                    if(c < 1) {
                        firstFlareId = (String) id;
                        c = 1;
                    }
                    if(idIndex > 0) {
                        nextSelectedFlareId = (String) id;
                        break;
                    }
                    if(id.equals(cEditorLogic.state.selectedFlareId)) {
                        idIndex = 1;
                    }
                }
                xCon.ex(String.format("e_selectflare %s",
                        nextSelectedFlareId.length() > 0 ? nextSelectedFlareId : firstFlareId));
                return cEditorLogic.state.selectedFlareId;
            case gScene.THING_PROP:
                xCon.ex(String.format("e_selectprop %d",
                        cEditorLogic.state.selectedPropId < eManager.currentMap.scene.props().size() - 1 ?
                                cEditorLogic.state.selectedPropId + 1 : 0));
                return Integer.toString(cEditorLogic.state.selectedPropId);
            default:
                xCon.ex(String.format("e_selecttile %d",
                        cEditorLogic.state.selectedTileId < eManager.currentMap.scene.tiles().size() - 1 ?
                                cEditorLogic.state.selectedTileId + 1 : 0));
                return Integer.toString(cEditorLogic.state.selectedTileId);
        }
    }

    public String undoCommand(String fullCommand) {
        switch (cEditorLogic.state.createObjCode) {
            case gScene.THING_FLARE:
                TreeMap orderedFlareMap = new TreeMap<>();
                orderedFlareMap.putAll(eManager.currentMap.scene.getThingMap("THING_FLARE"));
                String prevSelectedFlareId = "";
                String lastFlareId = "";
                int idIndex = 0;
                for(Object id : orderedFlareMap.keySet()) {
                    if(idIndex < 1 && !id.equals(cEditorLogic.getEditorState().selectedFlareId))
                        prevSelectedFlareId = (String) id;
                    lastFlareId = (String) id;
                    if(id.equals(cEditorLogic.getEditorState().selectedFlareId))
                        idIndex = 1;
                }
                xCon.ex(String.format("e_selectflare %s",
                        prevSelectedFlareId.length() > 0 ? prevSelectedFlareId : lastFlareId));
                return cEditorLogic.state.selectedFlareId;
            case gScene.THING_PROP:
                xCon.ex(String.format("e_selectprop %d",
                        cEditorLogic.state.selectedPropId > 0 ? cEditorLogic.state.selectedPropId - 1
                                : eManager.currentMap.scene.props().size() - 1));
                return Integer.toString(cEditorLogic.state.selectedPropId);
            default:
                xCon.ex(String.format("e_selecttile %d",
                        cEditorLogic.state.selectedTileId > 0 ? cEditorLogic.state.selectedTileId - 1
                                : eManager.currentMap.scene.tiles().size() - 1));
                return Integer.toString(cEditorLogic.state.selectedTileId);
        }
    }
}
