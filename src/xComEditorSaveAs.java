import javax.swing.*;
import java.io.File;

public class xComEditorSaveAs extends xCom{
    public String doCommand(String fullcommand) {
        JFileChooser fileChooser = new JFileChooser();
        File workingDirectory = new File("maps");
        fileChooser.setCurrentDirectory(workingDirectory);
        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String filename = file.getName();
            String foldername = file.getParent();
            eManager.currentMap.saveAs(filename, foldername);
            eManager.currentMap.mapName = filename.split("\\.")[0];
        }
        return fullcommand;
    }
}
