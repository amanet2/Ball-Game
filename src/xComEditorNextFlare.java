public class xComEditorNextFlare extends xCom {
    public String doCommand(String fullCommand) {
        xCon.ex("echo e_nexflare is deprecated use e_nextthing");
//        xCon.ex(String.format("e_selectflare %d",
//            cEditorLogic.state.selectedFlareId < eManager.currentMap.scene.flares().size()-1 ?
//                cEditorLogic.state.selectedFlareId+1 : 0));
        return fullCommand;
    }
}
