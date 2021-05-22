public class xComSelectUp extends xCom {
    public String doCommand(String fullCommand) {
        if(!uiInterface.inplay && !sVars.isOne("showmapmakerui")) {
            cVars.putInt("blockmouseui", 1);
            uiMenus.prevItem();
        }
        else
            xCon.ex("playerup");
        return fullCommand;
    }

    public String undoCommand(String fullCommand) {
        if(uiInterface.inplay || sVars.isOne("showmapmakerui"))
            xCon.ex("-playerup");
        return fullCommand;
    }
}
