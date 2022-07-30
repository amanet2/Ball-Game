import java.util.Arrays;

public class xComSoundlist extends xCom {
    public String doCommand(String fullCommand) {
        return Arrays.toString(eManager.winSoundFileSelection);
    }
}
