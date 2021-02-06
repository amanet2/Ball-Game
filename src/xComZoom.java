public class xComZoom extends xCom {
    public String doCommand(String fullCommand) {
        double newzoomlevel = eUtils.zoomLevel + 0.10;
        eUtils.zoomLevel = newzoomlevel <= 2.0 ? newzoomlevel : eUtils.zoomLevel;
        return "+zoom";
    }

    public String undoCommand(String fullCommand) {
        double newzoomlevel = eUtils.zoomLevel - 0.10;
        eUtils.zoomLevel = newzoomlevel > 0.2 ? newzoomlevel : eUtils.zoomLevel;
        return "-zoom";
    }
}
