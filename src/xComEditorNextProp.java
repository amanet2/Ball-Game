public class xComEditorNextProp extends xCom {
    public String doCommand(String fullCommand) {
        xCon.ex(String.format("e_selectprop %d",
            cEditorLogic.state.selectedPropId < eManager.currentMap.scene.props().size()-1 ?
                cEditorLogic.state.selectedPropId+1 : 0));
        return "";
    }
}
