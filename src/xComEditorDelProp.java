public class xComEditorDelProp extends xCom {
    public String doCommand(String fullCommand) {
        if(eManager.currentMap.scene.props().size() > cEditorLogic.state.selectedPropId) {
            try {
                gProp toRemove = eManager.currentMap.scene.props().get(cEditorLogic.state.selectedPropId);
                eManager.currentMap.scene.props().remove(toRemove);
                //get specific proparray
                System.out.println(eManager.currentMap.scene.objectLists.get(eManager.currentMap.scene.getPropArrayTitleForProp(toRemove)).size());
                eManager.currentMap.scene.objectLists.get(eManager.currentMap.scene.getPropArrayTitleForProp(toRemove)).remove(toRemove);
                System.out.println(eManager.currentMap.scene.objectLists.get(eManager.currentMap.scene.getPropArrayTitleForProp(toRemove)).size());
            } catch (Exception e) {
                eUtils.echoException(e);
                e.printStackTrace();
            }
        }
        return fullCommand;
    }
}
