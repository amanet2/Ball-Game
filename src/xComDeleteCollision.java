public class xComDeleteCollision extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 1) {
            String id = toks[1];
            if(eManager.currentMap.scene.getThingMap("THING_COLLISION").containsKey(id)) {
                gCollision collisionToDelete =
                        (gCollision) eManager.currentMap.scene.getThingMap("THING_COLLISION").get(id);
                eManager.currentMap.scene.getThingMap("THING_COLLISION").remove(id);
                return "removed collision id: " + id;
            }
        }
        return "usage: deletecollision <id>";
    }
}
