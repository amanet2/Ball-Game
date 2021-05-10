public class xComPlayerLeft extends xCom {
    public String doCommand(String fullCommand) {
        if(gClientLogic.getUserPlayer() != null) {
            gClientLogic.getUserPlayer().putInt("mov2", 1);
        }
        else {
            gCamera.move(2);
        }
        return fullCommand;
    }

    public String undoCommand(String fullCommand) {
        if(gClientLogic.getUserPlayer() != null) {
            gClientLogic.getUserPlayer().putInt("mov2", 0);
        }
        else {
            gCamera.stopMove(2);
        }
        return fullCommand;
    }
}
