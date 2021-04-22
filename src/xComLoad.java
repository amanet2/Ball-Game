import java.util.HashMap;

public class xComLoad extends xCom {
    public String doCommand(String fullCommand) {
        String mapPath = fullCommand.split(" ").length > 1 ? fullCommand.split(" ")[1] : "";
        oDisplay.instance().clearAndRefresh();
        nServer.instance().clearBots();
        cVars.put("botbehavior", "");
        if(mapPath.length() > 0) {
            if (!mapPath.contains(sVars.get("datapath")))
                mapPath = eUtils.getPath(mapPath);
            eManager.loadMap(mapPath);
        }
        else {
            //load the most basic blank map
            eManager.currentMap = new gMap();
            if(sSettings.show_mapmaker_ui)
                cVars.put("maploaded", "1");
        }
        cVars.putLong("starttime", System.currentTimeMillis());
        oDisplay.instance().createPanels();
        return "";
    }
}
