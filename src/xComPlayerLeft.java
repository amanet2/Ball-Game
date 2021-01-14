public class xComPlayerLeft extends xCom {
    public String doCommand(String fullCommand) {
        if(!uiInterface.inplay)
            gCamera.move(2);
        else if(cGameLogic.getUserPlayer() != null) {
            xCon.ex("THING_PLAYER.0.mov2 1");
        }
        return fullCommand;
    }

    public String undoCommand(String fullCommand) {
        if(!uiInterface.inplay)
            gCamera.stopMove(2);
        else if(cGameLogic.getUserPlayer() != null) {
            xCon.ex("THING_PLAYER.0.mov2 0");
        }
        return fullCommand;
    }
}
