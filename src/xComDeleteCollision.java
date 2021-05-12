public class xComDeleteCollision extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 1) {
            String id = toks[1];
            if(sSettings.IS_SERVER) {
                if(cServerLogic.scene.getThingMap("THING_COLLISION").containsKey(id))
                    cServerLogic.scene.getThingMap("THING_COLLISION").remove(id);
            }
            if(sSettings.IS_CLIENT) {
                if(cClientLogic.scene.getThingMap("THING_COLLISION").containsKey(id))
                    cClientLogic.scene.getThingMap("THING_COLLISION").remove(id);
            }
        }
        return "usage: deletecollision <id>";
    }
}
