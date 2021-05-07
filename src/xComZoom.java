public class xComZoom extends xCom {
    public String doCommand(String fullCommand) {
        eUtils.zoomLevel = Math.min(2.0, eUtils.zoomLevel + 0.10);
        return "+zoom";
    }

    public String undoCommand(String fullCommand) {
        eUtils.zoomLevel = Math.max(0.1, eUtils.zoomLevel - 0.10);
        return "-zoom";
    }
}
