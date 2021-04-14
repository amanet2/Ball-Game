public class xComShowScore extends xCom {
    public String doCommand(String fullCommand) {
        cVars.put("showscore", "1");
        return fullCommand;
    }
    public String undoCommand(String fullCommand) {
        cVars.put("showscore", "0");
        return fullCommand;
    }
}
