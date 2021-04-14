import javax.swing.*;
import java.io.File;

public class xComExportAsPrefab extends xCom{
    public String doCommand(String fullcommand) {
        JFileChooser fileChooser = new JFileChooser();
        File workingDirectory = new File(sVars.get("datapath")+"/prefabs");
        fileChooser.setCurrentDirectory(workingDirectory);
        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String filename = file.getName();
            eManager.currentMap.exportasprefab(filename);
        }
        return fullcommand;
    }
}
