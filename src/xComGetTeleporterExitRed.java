public class xComGetTeleporterExitRed extends xCom {
    public String doCommand(String fullCommand) {
        //usage: getteleporterexitred $teleporterid
        String[] args = fullCommand.split(" ");
        if(args.length < 2)
            return "null";
        String hostid = args[1];
        for(String id : cServerLogic.scene.getThingMap("ITEM_TELEPORTER_RED").keySet()) {
            gThing teleporter = cServerLogic.scene.getThingMap("ITEM_TELEPORTER_RED").get(id);
            if(!hostid.equals(teleporter.get("id"))) {
                return teleporter.get("id");
            }
        }
        return "null";
    }
}
