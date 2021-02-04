public class xComEditorNextFlare extends xCom {
    public String doCommand(String fullCommand) {
        xCon.ex(String.format("e_selectflare %d",
            cEditorLogic.state.selectedFlareTag < eManager.currentMap.scene.flares().size()-1 ?
                cEditorLogic.state.selectedFlareTag+1 : 0));
        return fullCommand;
    }
}
