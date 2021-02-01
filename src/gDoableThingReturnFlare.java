import java.util.HashMap;

public class gDoableThingReturnFlare extends gDoableThingReturn {
    public gThing getThing(String[] args) {
        gFlare flare = new gFlare(
                Integer.parseInt(args[0]),
                Integer.parseInt(args[1]),
                Integer.parseInt(args[2]),
                Integer.parseInt(args[3]),
                Integer.parseInt(args[4]),
                Integer.parseInt(args[5]),
                Integer.parseInt(args[6]),
                Integer.parseInt(args[7]),
                Integer.parseInt(args[8]),
                Integer.parseInt(args[9]),
                Integer.parseInt(args[10]),
                Integer.parseInt(args[11])
        );
        if(args.length > 12)
            flare.put("flicker", args[12]);
        return flare;
    }

    public void storeThing(gThing thingToLoad, gScene sceneToStore) {
        HashMap flaresMap = sceneToStore.getThingMap("THING_FLARE");
        thingToLoad.put("tag", Integer.toString(flaresMap.size()));
        flaresMap.put(thingToLoad.get("id"), thingToLoad);
    }
}
