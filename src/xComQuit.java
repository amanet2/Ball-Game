public class xComQuit extends xCom {
    public String doCommand(String fullCommand) {
        if(sVars.isOne("showmapmakerui") && cVars.isOne("maploaded")) {
            if(xCon.instance().getInt("e_showlossalert") > 0) {
                return "";
            }
        }
        uiInterface.exit();
        return "";
    }
}
