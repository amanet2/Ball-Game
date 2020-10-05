public class xComPlayerRight extends xCom {
    public String doCommand(String fullCommand) {
        if(!uiInterface.inplay)
            gCamera.move(3);
        else if(cGameLogic.getPlayerByIndex(0) != null) {
            xCon.ex("THING_PLAYER.0.mov3 1");
        }
        return fullCommand;
    }

    public String undoCommand(String fullCommand) {
        if(!uiInterface.inplay)
            gCamera.stopMove(3);
        else if(cGameLogic.getPlayerByIndex(0) != null) {
            xCon.ex("THING_PLAYER.0.mov3 0");
        }
        return fullCommand;
    }
}