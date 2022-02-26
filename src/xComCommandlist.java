import java.util.TreeSet;

public class xComCommandlist extends xCom {
    public String doCommand(String fullCommand) {
        TreeSet<String> sorted = new TreeSet<>(xCon.instance().commands.keySet());
        return sorted.toString();
    }
}
