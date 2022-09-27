import javax.swing.JFileChooser;
import java.io.File;

public class xComEditorOpenFile extends xCom {
    public String doCommand(String fullCommand) {
        if(sSettings.show_mapmaker_ui) {
            JFileChooser fileChooser = new JFileChooser();
            uiEditorMenus.setFileChooserFont(fileChooser.getComponents());
            File workingDirectory = new File("maps");
            fileChooser.setCurrentDirectory(workingDirectory);
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                if(!xCon.ex("e_showlossalert").equals("0"))
                    return "";
                File file = fileChooser.getSelectedFile();
                if(!nServer.instance().isAlive()) {
                    xCon.ex("startserver");
                    xCon.ex("load");
                    xCon.ex("joingame localhost " + cServerLogic.listenPort);
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
