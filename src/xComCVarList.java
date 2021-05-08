import java.util.TreeMap;

public class xComCVarList extends xCom {
    public String doCommand(String fullCommand) {
        TreeMap<String, String> sorted = new TreeMap<>(cVars.vars());
        return sorted.toString();
    }
}
