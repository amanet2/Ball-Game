public class xComSpawnPointgiver extends xCom {
    public String doCommand(String fullCommand) {
        gThing spawnpoint = cServerLogic.scene.getThingMap("ITEM_SPAWNPOINT").get(
                xCon.ex("getrandthing ITEM_SPAWNPOINT"));
        if(spawnpoint == null)
            return "could not get random spawnpoint";
        xCon.ex(String.format("exec scripts/spawnpointgiver %d %d %d", cServerLogic.getNewItemId(),
                spawnpoint.getInt("coordx"), spawnpoint.getInt("coordy")));
        return "spawned pointgiver";
    }
}
