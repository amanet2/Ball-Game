import javax.swing.*;
import java.io.File;

public class xComExportAsPrefab extends xCom{
    public String doCommand(String fullcommand) {
        JFileChooser fileChooser = new JFileChooser();
        File workingDirectory = new File("prefabs");
        fileChooser.setCurrentDirectory(workingDirectory);
        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String filename = file.getName();
            if(sSettings.IS_SERVER)
                cServerLogic.scene.exportasprefab(filename);
            else if(sSettings.IS_CLIENT)
                cClientLogic.scene.exportasprefab(filename);
        }
        return fullcommand;
    }
}
