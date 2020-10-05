import java.util.TreeMap;

public class xComSVarlist extends xCom {
    public String doCommand(String fullCommand) {
        TreeMap<String, String> sorted = new TreeMap<>();
        sorted.putAll(sVars.vars());
        return sorted.toString();
    }
}
