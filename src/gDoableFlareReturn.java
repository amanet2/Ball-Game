public class gDoableFlareReturn {
    public gFlare getFlare(String[] args) {
        return new gFlare(
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
                Integer.parseInt(args[11]),
                Integer.parseInt(args[12])
        );
    }

    public void storeFlare(gFlare flareToLoad) {
        if(sSettings.IS_SERVER)
            storeFlareDelegate(flareToLoad, cServerLogic.scene);
        if(sSettings.IS_CLIENT)
            storeFlareDelegate(flareToLoad, cClientLogic.scene);
    }

    private void storeFlareDelegate(gFlare flareToLoad, gScene sceneToStore) {
        String flareId = Integer.toString(sceneToStore.flareIdCtr);
        sceneToStore.getThingMap("THING_FLARE").put(flareId, flareToLoad);
        sceneToStore.flareIdCtr += 1;
    }
}
