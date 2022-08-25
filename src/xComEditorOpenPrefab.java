import javax.swing.JFileChooser;
import java.io.File;

public class xComEditorOpenPrefab extends xCom {
    public String doCommand(String fullCommand) {
        if(sSettings.show_mapmaker_ui) {
            JFileChooser fileChooser = new JFileChooser();
            File workingDirectory = new File("prefabs");
            fileChooser.setCurrentDirectory(workingDirectory);
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                cClientLogic.newprefabname = file.getName();
                uiEditorMenus.newitemname = "";
                return "set prefab from file " + file.getPath();
            }
        }
        return "";
    }
}
