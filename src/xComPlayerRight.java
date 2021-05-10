public class xComPlayerRight extends xCom {
    public String doCommand(String fullCommand) {
        if(cClientLogic.getUserPlayer() != null) {
            cClientLogic.getUserPlayer().putInt("mov3", 1);
        }
        else {
            gCamera.move(3);
        }
        return fullCommand;
    }

    public String undoCommand(String fullCommand) {
        if(cClientLogic.getUserPlayer() != null) {
            cClientLogic.getUserPlayer().putInt("mov3", 0);
        }
        else {
            gCamera.stopMove(3);
        }
        return fullCommand;
    }
}