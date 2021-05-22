import javax.swing.*;
import java.io.File;

public class xComEditorOpenPrefab extends xCom {
    public String doCommand(String fullCommand) {
        if(sVars.isOne("showmapmakerui")) {
            JFileChooser fileChooser = new JFileChooser();
            File workingDirectory = new File("prefabs");
            fileChooser.setCurrentDirectory(workingDirectory);
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                cVars.put("newprefabname", file.getName());
                cVars.put("newitemname", "");
                return "set prefab from file " + file.getPath();
            }
        }
        return "";
    }
}
