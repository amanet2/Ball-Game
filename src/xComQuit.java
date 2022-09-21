public class xComQuit extends xCom {
    public String doCommand(String fullCommand) {
        if(sSettings.show_mapmaker_ui && cClientLogic.maploaded) {
            if(!xCon.ex("e_showlossalert").equals("0"))
                return "";
        }
        uiInterface.exit();
        return "";
    }
}
