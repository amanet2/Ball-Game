public class xComPlayerDown extends xCom {
    public String doCommand(String fullCommand) {
        if(cGameLogic.userPlayer() != null) {
            cGameLogic.userPlayer().putInt("mov1", 1);
        }
        else {
            gCamera.move(1);
        }
        return fullCommand;
    }

    public String undoCommand(String fullCommand) {
        if(cGameLogic.userPlayer() != null) {
            cGameLogic.userPlayer().putInt("mov1", 0);
        }
        else {
            gCamera.stopMove(1);
        }
        return fullCommand;
    }
}
