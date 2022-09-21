import javax.swing.JOptionPane;

public class xComEditorShowLossAlert extends xCom {
    public  String doCommand(String fullcomm) {
        return Integer.toString(JOptionPane.showConfirmDialog(oDisplay.instance(),
                "Any unsaved changes will be lost...", "Are You Sure?", JOptionPane.YES_NO_OPTION));
    }
}
