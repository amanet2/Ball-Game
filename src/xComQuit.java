public class xComQuit extends xCom {
    public String doCommand(String fullCommand) {
        if(sSettings.show_mapmaker_ui) {
            if(xCon.getInt("e_showlossalert") > 0) {
                return "";
            }
        }
        if(sSettings.net_client)
            xCon.ex("cv_quitting 1");
        else {
            xCon.ex("cv_quitconfirmed 1");
            uiInterface.exit();
        }
        return "";
    }
}
