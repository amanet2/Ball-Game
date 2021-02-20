import java.util.ArrayList;
import java.util.Random;

public class xComNudgeCamera extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if (toks.length > 1) {
            int intensity = Integer.parseInt(toks[1]);
            cVars.addIntVal("camx", intensity);
            cVars.addIntVal("camy", intensity);
            return "nudged camera " + intensity;
        }
        return "usage: nudgecamera <intensity>";
    }
}
