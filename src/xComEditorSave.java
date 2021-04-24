public class xComEditorSave extends xCom {
    public String doCommand(String fullcommand) {
        String[] args = eManager.currentMap.mapName.split("\\\\");
        String filename = args[args.length-1]  + sVars.get("mapextension");
        eManager.currentMap.saveAs(filename);
        return "map saved";
    }
}
