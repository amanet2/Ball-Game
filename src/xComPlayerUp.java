public class xComPlayerUp extends xCom {
    public String doCommand(String fullCommand) {
        if(cGameLogic.userPlayer() != null) {
            cGameLogic.userPlayer().putInt("mov0", 1);
        }
        else {
            gCamera.move(0);
        }
        return fullCommand;
    }

    public String undoCommand(String fullCommand) {
        if(cGameLogic.userPlayer() != null) {
            cGameLogic.userPlayer().putInt("mov0", 0);
        }
        else {
            gCamera.stopMove(0);
        }
        return fullCommand;
    }
}