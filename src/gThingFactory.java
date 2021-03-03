import java.util.HashMap;

public class gThingFactory {
//    HashMap<String, gDoableThingReturn> thingLoadMap;
    //the plan for this map is for each string to point to a unique doable that returns a prop configured to match one
    //of the prop types we'd like to feature in the game
    HashMap<String, gDoablePropReturn> propLoadMap;
    static gThingFactory instance = null;

    private gThingFactory() {
//        thingLoadMap = new HashMap<>();
//        thingLoadMap.put("THING_FLARE", new gDoableThingReturnFlare());
        propLoadMap = new HashMap<>();
        propLoadMap.put("PROP_TELEPORTER", new gDoablePropReturnTeleporter());
        propLoadMap.put("PROP_BOOST", new gDoablePropReturnBoost());
        propLoadMap.put("PROP_SCOREPOINT", new gDoablePropReturnScorepoint());
        propLoadMap.put("PROP_FLAGBLUE", new gDoablePropReturnFlagBlue());
        propLoadMap.put("PROP_FLAGRED", new gDoablePropReturnFlagRed());
        propLoadMap.put("PROP_POWERUP", new gDoablePropReturnPowerup());
        propLoadMap.put("PROP_SPAWNPOINT", new gDoablePropReturnSpawnpoint());
    }

    public static gThingFactory instance() {
        if(instance == null)
            instance = new gThingFactory();
        return instance;
    }
}
