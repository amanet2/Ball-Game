import java.util.TreeMap;

public class xComCVarList extends xCom {
    public String doCommand(String fullCommand) {
        TreeMap<String, gArg> sorted = new TreeMap<>(cClientVars.instance().args);
        return sorted.toString();
    }
}
