public class xComGetTeleporterExitBlue extends xCom {
    public String doCommand(String fullCommand) {
        //usage: getteleporterexitblue $teleporterid
        String[] args = fullCommand.split(" ");
        if(args.length < 2)
            return "null";
        String hostid = args[1];
        for(String id : cServerLogic.scene.getThingMap("ITEM_TELEPORTER_BLUE").keySet()) {
            gThing teleporter = cServerLogic.scene.getThingMap("ITEM_TELEPORTER_BLUE").get(id);
            if(!hostid.equals(teleporter.get("id"))) {
                return teleporter.get("id");
            }
        }
        return "null";
    }
}
