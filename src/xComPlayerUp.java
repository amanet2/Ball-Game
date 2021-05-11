public class xComPlayerUp extends xCom {
    public String doCommand(String fullCommand) {
        if(cClientLogic.getUserPlayer() != null) {
            cClientLogic.getUserPlayer().putInt("mov0", 1);
        }
        else if(sSettings.show_mapmaker_ui){
            gCamera.move(0);
        }
        return fullCommand;
    }

    public String undoCommand(String fullCommand) {
        if(cClientLogic.getUserPlayer() != null) {
            cClientLogic.getUserPlayer().putInt("mov0", 0);
        }
        else {
            gCamera.stopMove(0);
        }
        return fullCommand;
    }
}