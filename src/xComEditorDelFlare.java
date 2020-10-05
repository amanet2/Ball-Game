public class xComEditorDelFlare extends xCom {
    public String doCommand(String fullCommand) {
        if(eManager.currentMap.scene.flares().size() > cEditorLogic.state.selectedFlareId) {
            try {
                eManager.currentMap.scene.flares().remove(eManager.currentMap.scene.flares().get(cEditorLogic.state.selectedFlareId));
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        return fullCommand;
    }
}
