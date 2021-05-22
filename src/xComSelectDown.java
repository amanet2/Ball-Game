public class xComSelectDown extends xCom {
    public String doCommand(String fullCommand) {
        if(!uiInterface.inplay && !sVars.isOne("showmapmakerui")) {
            cVars.putInt("blockmouseui", 1);
            uiMenus.nextItem();
        }
        else
            xCon.ex("playerdown");
        return fullCommand;
    }

    public String undoCommand(String fullCommand) {
        if(uiInterface.inplay || sVars.isOne("showmapmakerui"))
            xCon.ex("-playerdown");
        return fullCommand;
    }
}
