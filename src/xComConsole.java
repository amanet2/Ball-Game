public class xComConsole extends xCom {
    public String doCommand(String fullCommand) {
        if(sSettings.show_mapmaker_ui)
            sVars.put("inconsole", sVars.isOne("inconsole") ? "0" : "1");
        return "console";
    }
}
