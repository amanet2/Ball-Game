public class xComEditorDelProp extends xCom {
    public String doCommand(String fullCommand) {
        if(eManager.currentMap.scene.props().size() > cEditorLogic.state.selectedPropId) {
            try {
                eManager.currentMap.scene.props().remove(eManager.currentMap.scene.props().get(cEditorLogic.state.selectedPropId));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return fullCommand;
    }
}
