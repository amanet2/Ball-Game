import javax.swing.*;
import java.io.File;

public class xComEditorOpenFile extends xCom {
    public String doCommand(String fullCommand) {
        if(sSettings.show_mapmaker_ui) {
            JFileChooser fileChooser = new JFileChooser();
            File workingDirectory = new File("maps");
            fileChooser.setCurrentDirectory(workingDirectory);
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                if(xCon.getInt("e_showlossalert") > 0)
                    return "";
                File file = fileChooser.getSelectedFile();
                xCon.ex("exec " + file.getPath());
                cEditorLogic.refreshGametypeCheckBoxMenuItems();
            }
        }
        return "";
    }
}
