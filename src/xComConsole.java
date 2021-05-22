public class xComConsole extends xCom {
    public String doCommand(String fullCommand) {
//        if(sVars.isOne("showmapmakerui"))
            sVars.put("inconsole", sVars.isOne("inconsole") ? "0" : "1");
        return "console";
    }
}
