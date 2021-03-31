import java.util.ArrayList;

public class xComEditorDelThing extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        int toRemove = -1;
        String toRemoveId = "";
        switch (cEditorLogic.state.createObjCode) {
            case gScene.THING_FLARE:
                toRemove = toks.length > 1 ? Integer.parseInt(toks[1]) : cEditorLogic.state.selectedFlareTag;
                try {
                    eManager.currentMap.scene.flares().remove(cEditorLogic.state.selectedFlareTag);
                    //selects another flare after deletion of flare
                        if(eManager.currentMap.scene.flares().size() > 0)
                            xCon.ex(String.format("HIDDEN e_selectflare %d",
                                    eManager.currentMap.scene.flares().size() - 1));
                } catch (Exception e) {
                    eUtils.echoException(e);
                    e.printStackTrace();
                }
                break;
            case gScene.THING_PROP:
                toRemove = toks.length > 1 ? Integer.parseInt(toks[1]) : cEditorLogic.state.selectedPropId;
                if(eManager.currentMap.scene.props().size() > toRemove) {
                    try {
                        eManager.currentMap.scene.props().remove(eManager.currentMap.scene.props().get(toRemove));
                        if(eManager.currentMap.scene.props().size() > 0)
                            xCon.ex(String.format("HIDDEN e_selectprop %d",
                                    eManager.currentMap.scene.props().size() - 1));
                    } catch (Exception e) {
                        eUtils.echoException(e);
                        e.printStackTrace();
                    }
                }
                break;
            case gScene.THING_PREFAB:
                toRemove = toks.length > 1 ? Integer.parseInt(toks[1]) : cVars.getInt("prefabid");
                ArrayList<String> toRemoveBlockIds = new ArrayList<>();
                ArrayList<String> toRemoveCollisionIds = new ArrayList<>();
                for(String id : eManager.currentMap.scene.getThingMap("THING_BLOCK").keySet()) {
                    gThing block = eManager.currentMap.scene.getThingMap("THING_BLOCK").get(id);
                    if(block.isVal("prefabid", cVars.get("prefabid"))) {
                        toRemoveBlockIds.add(id);
                    }
                }
                for(String id : eManager.currentMap.scene.getThingMap("THING_COLLISION").keySet()) {
                    gThing collision = eManager.currentMap.scene.getThingMap("THING_COLLISION").get(id);
                    if(collision.isVal("prefabid", cVars.get("prefabid"))) {
                        toRemoveCollisionIds.add(id);
                    }
                }
                for(String id : toRemoveBlockIds) {
                    xCon.ex("deleteblock " + id);
                }
                for(String id : toRemoveCollisionIds) {
                    xCon.ex("deletecollision " + id);
                }
                break;
        }
        return "deleted " + toRemove;
    }
}
