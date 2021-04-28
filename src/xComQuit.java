public class xComQuit extends xCom {
    public String doCommand(String fullCommand) {
        if(sSettings.show_mapmaker_ui) {
            if(xCon.getInt("e_showlossalert") > 0) {
                return "";
            }
        }
        if(sSettings.net_client) {
            xCon.ex("disconnect");
            xCon.ex("cv_quitting 1");
        }
        else {
            uiInterface.exit();
        }
        return "";
    }
}
