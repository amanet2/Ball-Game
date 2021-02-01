public class xComEditorDelFlare extends xCom {
    public String doCommand(String fullCommand) {
        xCon.ex("echo e_delflare is deprecated use e_delthing");
//        if(cEditorLogic.state.selectedFlareId.length() > 0) {
//            try {
//                eManager.currentMap.scene.getThingMap("THING_FLARE").remove(
//                        cEditorLogic.state.selectedFlareId);
//            } catch(Exception e) {
//                eUtils.echoException(e);
//                e.printStackTrace();
//            }
//        }
        return fullCommand;
    }
}
