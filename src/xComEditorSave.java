import javax.swing.*;
import java.io.File;

public class xComEditorSave extends xCom {
    public String doCommand(String fullcommand) {
        String[] args = eManager.currentMap.mapName.split("\\\\");
        String filename = args[args.length-1]  + sVars.get("mapextension");
        eManager.currentMap.save(filename);
        return "map saved";
    }
}
