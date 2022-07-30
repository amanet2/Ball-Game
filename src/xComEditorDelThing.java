import java.util.ArrayList;

public class xComEditorDelThing extends xCom {
    public String doCommand(String fullCommand) {
        if(cClientLogic.selectedPrefabId.length() > 0) {
                ArrayList<String> toRemoveBlockIds = new ArrayList<>();
                ArrayList<String> toRemoveCollisionIds = new ArrayList<>();
                for(String id : cClientLogic.scene.getThingMap("THING_BLOCK").keySet()) {
                    gThing block = cClientLogic.scene.getThingMap("THING_BLOCK").get(id);
                    if(block.isVal("prefabid", cClientLogic.selectedPrefabId))
                        toRemoveBlockIds.add(id);
                }
                for(String id : cClientLogic.scene.getThingMap("THING_COLLISION").keySet()) {
                    gThing collision = cClientLogic.scene.getThingMap("THING_COLLISION").get(id);
                    if(collision.isVal("prefabid", cClientLogic.selectedPrefabId))
                        toRemoveCollisionIds.add(id);
                }
                for(String id : toRemoveBlockIds) {
                    String cmd = "deleteblock " + id;
                    nClient.instance().addNetCmd(cmd);
                }
                for(String id : toRemoveCollisionIds) {
                    String cmd = "deletecollision " + id;
                    nClient.instance().addNetCmd(cmd);
                }
                return "deleted prefab " + cClientLogic.selectedPrefabId;
        }
        if(cClientLogic.selecteditemid.length() > 0) {
                String toRemoveItemId = "";
                for(String id : cClientLogic.scene.getThingMap("THING_ITEM").keySet()) {
                    gThing item = cClientLogic.scene.getThingMap("THING_ITEM").get(id);
                    if(item.isVal("itemid", cClientLogic.selecteditemid))
                        toRemoveItemId = id;
                }
                if(toRemoveItemId.length() > 0) {
                    String cmd = "deleteitem " + toRemoveItemId;
                    nClient.instance().addNetCmd(cmd);
                }
                return "deleted item " + cClientLogic.selecteditemid;
        }
        return "nothing to delete";
    }
}
