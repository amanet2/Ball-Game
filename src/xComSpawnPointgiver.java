public class xComSpawnPointgiver extends xCom {
    public String doCommand(String fullCommand) {
        String spawnpointId = xCon.ex("getrandthing ITEM_SPAWNPOINT");
        gThing spawnpoint = cServerLogic.scene.getThingMap("ITEM_SPAWNPOINT").get(spawnpointId);
        if(spawnpoint == null)
            return "could not get random spawnpoint";
        int itemId = 0;
        for(String id : cServerLogic.scene.getThingMap("THING_ITEM").keySet()) {;
            if(itemId < Integer.parseInt(id))
                itemId = Integer.parseInt(id);
        }
        itemId++; //want to be the _next_ id
        xCon.ex(String.format("exec scripts/spawnpointgiver %d %d %d", itemId,
                spawnpoint.getInt("coordx"), spawnpoint.getInt("coordy")));
        return "spawned pointgiver";
    }
}
