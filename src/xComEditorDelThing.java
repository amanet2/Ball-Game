import java.util.ArrayList;

public class xComEditorDelThing extends xCom {
    public String doCommand(String fullCommand) {
        if(cClientLogic.selectedPrefabId.length() > 0) {
                xCon.ex("cl_addcom deleteprefab " + cClientLogic.selectedPrefabId);
                return "deleted prefab " + cClientLogic.selectedPrefabId;
        }
        if(cClientLogic.selecteditemid.length() > 0) {
                String toRemoveItemId = "";
                for(String id : cClientLogic.scene.getThingMap("THING_ITEM").keySet()) {
                    gThing item = cClientLogic.scene.getThingMap("THING_ITEM").get(id);
                    if(item.isVal("id", cClientLogic.selecteditemid))
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
