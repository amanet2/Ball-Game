import javax.swing.*;
import java.io.File;

public class xComEditorSaveAs extends xCom{
    public String doCommand(String fullcommand) {
        JFileChooser fileChooser = new JFileChooser();
        File workingDirectory = new File(sVars.get("datapath"));
        fileChooser.setCurrentDirectory(workingDirectory);
        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String filename = file.getName();
            eManager.currentMap.save(filename);
            eManager.currentMap.mapName = filename.split("\\.")[0];
        }
        return fullcommand;
    }
}
