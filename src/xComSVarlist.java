import java.util.TreeMap;

public class xComSVarlist extends xCom {
    public String doCommand(String fullCommand) {
        TreeMap<String, gArg> sorted = new TreeMap<>(cServerVars.instance().args);
        return sorted.toString();
    }
}
