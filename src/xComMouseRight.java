public class xComMouseRight extends xCom {
    public String doCommand(String fullCommand) {
        if (uiInterface.inplay ) {
//            xCon.ex("sspeed");
        }
        else {
            if(sSettings.show_mapmaker_ui) {
                cScripts.selectThingUnderMouse();
            }
        }
        return fullCommand;
    }

    public String undoCommand(String fullCommand) {
        iMouse.holdingMouseRight = false;
        return fullCommand;
    }
}
