import javax.swing.*;
import java.io.File;

public class xComEditorOpenFile extends xCom {
    public String doCommand(String fullCommand) {
        if(sSettings.show_mapmaker_ui) {
            JFileChooser fileChooser = new JFileChooser();
            File workingDirectory = new File(sVars.get("datapath"));
            fileChooser.setCurrentDirectory(workingDirectory);
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                if(xCon.getInt("e_showlossalert") > 0) {
                    return "";
                }
                File file = fileChooser.getSelectedFile();
                xCon.ex("load " + file.getPath());
                cEditorLogic.menus.get("Scene").getItem(0).setText("Game Mode: " +
                        cGameMode.net_gamemode_texts[cVars.getInt("gamemode")]);
                cEditorLogic.menus.get("Scene").getItem(2).setText("Map Execs: " + eManager.currentMap.execLines.size());
            }
        }
        return "";
    }
}
