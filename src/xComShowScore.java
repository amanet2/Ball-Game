public class xComShowScore extends xCom {
    public String doCommand(String fullCommand) {
        xCon.ex("cv_showscore 1");
        return fullCommand;
    }
    public String undoCommand(String fullCommand) {
        xCon.ex("cv_showscore 0");
        return fullCommand;
    }
}
