import javax.swing.*;

public class xComEditorShowLossAlert extends xCom {
    public  String doCommand(String fullcomm) {
        if (JOptionPane.showConfirmDialog(oDisplay.instance(), "Any unsaved changes will be lost...",
                sVars.get("defaulttitle"), JOptionPane.YES_NO_OPTION) > 0)
            return "1";
        return "0";
    }
}
