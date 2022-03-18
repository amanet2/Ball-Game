import javax.swing.*;
import java.io.File;

public class xComEditorOpenFile extends xCom {
    public String doCommand(String fullCommand) {
        if(sSettings.show_mapmaker_ui) {
            JFileChooser fileChooser = new JFileChooser();
            uiEditorMenus.setFileChooserFont(fileChooser.getComponents());
            File workingDirectory = new File("maps");
            fileChooser.setCurrentDirectory(workingDirectory);
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                if(xCon.instance().getInt("e_showlossalert") > 0)
                    return "";
                File file = fileChooser.getSelectedFile();
                if(!nServer.instance().isAlive()) {
                    xCon.ex("startserver");
                    xCon.ex("load");
                    xCon.ex("joingame localhost 5555");
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                xCon.ex("changemap " + file.getPath());
                uiEditorMenus.refreshGametypeCheckBoxMenuItems();
                return "opening " + file.getPath();
            }
        }
        return "";
    }
}
