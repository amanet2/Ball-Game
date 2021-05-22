public class xComPlayerDown extends xCom {
    public String doCommand(String fullCommand) {
        if(cClientLogic.getUserPlayer() != null)
            cClientLogic.getUserPlayer().putInt("mov1", 1);
        else if(sVars.isOne("showmapmakerui"))
            gCamera.move(1);
        return fullCommand;
    }

    public String undoCommand(String fullCommand) {
        if(cClientLogic.getUserPlayer() != null)
            cClientLogic.getUserPlayer().putInt("mov1", 0);
        else
            gCamera.stopMove(1);
        return fullCommand;
    }
}
