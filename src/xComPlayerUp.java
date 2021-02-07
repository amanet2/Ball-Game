public class xComPlayerUp extends xCom {
    public String doCommand(String fullCommand) {
        if(!uiInterface.inplay)
            gCamera.move(0);
        else if(cGameLogic.userPlayer() != null) {
            if(cVars.getInt("mapview") != gMap.MAP_SIDEVIEW
                || cVars.isOne("inboost"))
                xCon.ex("THING_PLAYER.0.mov0 1");
        }
        return fullCommand;
    }

    public String undoCommand(String fullCommand) {
        if(!uiInterface.inplay)
            gCamera.stopMove(0);
        else if(cGameLogic.userPlayer() != null) {
            if(cVars.getInt("mapview") != gMap.MAP_SIDEVIEW ||
                    cVars.isOne("inboost"))
                xCon.ex("THING_PLAYER.0.mov0 0");
        }
        return fullCommand;
    }
}