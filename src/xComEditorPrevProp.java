public class xComEditorPrevProp extends xCom {
    public String doCommand(String fullCommand) {
        xCon.ex(String.format("e_selectprop %d",
            cEditorLogic.state.selectedPropId >0 ? cEditorLogic.state.selectedPropId-1
                : eManager.currentMap.scene.props().size()-1));
        return "";
    }
}
