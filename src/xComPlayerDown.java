public class xComPlayerDown extends xCom {
    public String doCommand(String fullCommand) {
        if(!uiInterface.inplay)
            gCamera.move(1);
        else if(cGameLogic.getUserPlayer() != null) {
            if(cVars.getInt("maptype") != gMap.MAP_SIDEVIEW
                || cVars.isOne("inboost"))
                xCon.ex("THING_PLAYER.0.mov1 1");
        }
        return fullCommand;
    }

    public String undoCommand(String fullCommand) {
        if(!uiInterface.inplay)
            gCamera.stopMove(1);
        else if(cGameLogic.getUserPlayer() != null) {
            if(cVars.getInt("maptype") != gMap.MAP_SIDEVIEW
                || cVars.isOne("inboost"))
                xCon.ex("THING_PLAYER.0.mov1 0");
        }
        return fullCommand;
    }
}
