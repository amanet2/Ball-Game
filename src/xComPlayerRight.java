public class xComPlayerRight extends xCom {
    public String doCommand(String fullCommand) {
        if(cGameLogic.userPlayer() != null) {
            cGameLogic.userPlayer().putInt("mov3", 1);
        }
        else {
            gCamera.move(3);
        }
        return fullCommand;
    }

    public String undoCommand(String fullCommand) {
        if(cGameLogic.userPlayer() != null) {
            cGameLogic.userPlayer().putInt("mov3", 0);
        }
        else {
            gCamera.stopMove(3);
        }
        return fullCommand;
    }
}