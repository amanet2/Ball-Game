public class xComShowScore extends xCom {
    public String doCommand(String fullCommand) {
        dScreenMessages.showscore = true;
        return fullCommand;
    }
    public String undoCommand(String fullCommand) {
        dScreenMessages.showscore = false;
        return fullCommand;
    }
}
