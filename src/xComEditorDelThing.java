public class xComEditorDelThing extends xCom {
    public String doCommand(String fullCommand) {
        if(cClientLogic.selectedPrefabId.length() > 0) {
                xCon.ex("cl_addcom deleteprefab " + cClientLogic.selectedPrefabId);
                return "deleted prefab " + cClientLogic.selectedPrefabId;
        }
        if(cClientLogic.selecteditemid.length() > 0) {
            nClient.instance().addNetCmd("deleteitem " + cClientLogic.selecteditemid);
            return "deleted item " + cClientLogic.selecteditemid;
        }
        return "nothing to delete";
    }
}
