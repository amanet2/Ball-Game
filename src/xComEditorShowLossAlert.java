import javax.swing.*;

public class xComEditorShowLossAlert extends xCom {
    public  String doCommand(String fullcomm) {
//        xCon.ex(String.format("playsound %s", Math.random() > 0.5 ? "sounds/shout.wav" : "sounds/death.wav"));
        if (JOptionPane.showConfirmDialog(oDisplay.instance(), "Any unsaved changes will be lost...",
                "Are You Sure?", JOptionPane.YES_NO_OPTION) > 0)
            return "1";
        return "0";
    }
}
