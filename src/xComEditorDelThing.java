import java.util.ArrayList;

public class xComEditorDelThing extends xCom {
    public String doCommand(String fullCommand) {
        if(cVars.get("selectedprefabid").length() > 0) {
                ArrayList<String> toRemoveBlockIds = new ArrayList<>();
                ArrayList<String> toRemoveCollisionIds = new ArrayList<>();
                for(String id : eManager.currentMap.scene.getThingMap("THING_BLOCK").keySet()) {
                    gThing block = eManager.currentMap.scene.getThingMap("THING_BLOCK").get(id);
                    if(block.isVal("prefabid", cVars.get("selectedprefabid"))) {
                        toRemoveBlockIds.add(id);
                    }
                }
                for(String id : eManager.currentMap.scene.getThingMap("THING_COLLISION").keySet()) {
                    gThing collision = eManager.currentMap.scene.getThingMap("THING_COLLISION").get(id);
                    if(collision.isVal("prefabid", cVars.get("selectedprefabid"))) {
                        toRemoveCollisionIds.add(id);
                    }
                }
                for(String id : toRemoveBlockIds) {
                    xCon.ex("deleteblock " + id);
                }
                for(String id : toRemoveCollisionIds) {
                    xCon.ex("deletecollision " + id);
                }
                return "deleted prefab " + cVars.get("selectedprefabid");
        }
//        switch (cEditorLogic.state.createObjCode) {
//            case gScene.THING_FLARE:
//                toRemove = toks.length > 1 ? Integer.parseInt(toks[1]) : cEditorLogic.state.selectedFlareTag;
//                try {
//                    eManager.currentMap.scene.flares().remove(cEditorLogic.state.selectedFlareTag);
//                    //selects another flare after deletion of flare
//                        if(eManager.currentMap.scene.flares().size() > 0)
//                            xCon.ex(String.format("HIDDEN e_selectflare %d",
//                                    eManager.currentMap.scene.flares().size() - 1));
//                } catch (Exception e) {
//                    eUtils.echoException(e);
//                    e.printStackTrace();
//                }
//                break;
//            case gScene.THING_ITEM:
//                toRemove = toks.length > 1 ? Integer.parseInt(toks[1]) : cVars.getInt("selecteditemid");
//                String toRemoveItemId = "";
//                for(String id : eManager.currentMap.scene.getThingMap("THING_ITEM").keySet()) {
//                    gThing item = eManager.currentMap.scene.getThingMap("THING_ITEM").get(id);
//                    if(item.isVal("itemid", cVars.get("selecteditemid"))) {
//                        toRemoveItemId = id;
//                    }
//                }
//                if(toRemoveItemId.length() > 0)
//                    xCon.ex("deleteitem " + toRemoveItemId);
//                cVars.put("newitemname", "");
//                break;
//            case gScene.THING_PREFAB:
//                toRemove = toks.length > 1 ? Integer.parseInt(toks[1]) : cVars.getInt("selectedprefabid");
//                ArrayList<String> toRemoveBlockIds = new ArrayList<>();
//                ArrayList<String> toRemoveCollisionIds = new ArrayList<>();
//                for(String id : eManager.currentMap.scene.getThingMap("THING_BLOCK").keySet()) {
//                    gThing block = eManager.currentMap.scene.getThingMap("THING_BLOCK").get(id);
//                    if(block.isVal("prefabid", cVars.get("selectedprefabid"))) {
//                        toRemoveBlockIds.add(id);
//                    }
//                }
//                for(String id : eManager.currentMap.scene.getThingMap("THING_COLLISION").keySet()) {
//                    gThing collision = eManager.currentMap.scene.getThingMap("THING_COLLISION").get(id);
//                    if(collision.isVal("prefabid", cVars.get("selectedprefabid"))) {
//                        toRemoveCollisionIds.add(id);
//                    }
//                }
//                for(String id : toRemoveBlockIds) {
//                    xCon.ex("deleteblock " + id);
//                }
//                for(String id : toRemoveCollisionIds) {
//                    xCon.ex("deletecollision " + id);
//                }
//                break;
//        }
        return "nothing to delete";
    }
}
