import java.util.Arrays;

public class xComMaplist extends xCom {
    public String doCommand(String fullCommand) {
        return Arrays.toString(eManager.mapsSelection);
    }
}
