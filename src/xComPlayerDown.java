public class xComPlayerDown extends xCom {
    public String doCommand(String fullCommand) {
        if(!uiInterface.inplay)
            gCamera.move(1);
        else if(cGameLogic.userPlayer() != null) {
            cGameLogic.userPlayer().putInt("mov1", 1);
        }
        return fullCommand;
    }

    public String undoCommand(String fullCommand) {
        if(!uiInterface.inplay)
            gCamera.stopMove(1);
        else if(cGameLogic.userPlayer() != null) {
            cGameLogic.userPlayer().putInt("mov1", 0);
        }
        return fullCommand;
    }
}
