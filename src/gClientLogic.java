public class gClientLogic {
    gScene scene;
    public static void gameLoop() {
        oDisplay.instance().checkDisplay();
        oAudio.instance().checkAudio();
        gCamera.updatePosition();
        nClient.instance().processPackets();
        if(sSettings.show_mapmaker_ui)
            cScripts.selectThingUnderMouse();
        checkGameState();
    }

    static void checkGameState() {
        for(String id : gScene.getPlayerIds()) {
            if(id.equals(uiInterface.uuid))
                continue;
            gPlayer obj = gScene.getPlayerById(id);
            for (int i = 0; i < 4; i++) {
                if(nClient.instance().serverArgsMap.get(obj.get("id")).containsKey("vels"))
                    obj.putInt("vel"+i, Integer.parseInt(nClient.instance().serverArgsMap.get(
                            obj.get("id")).get("vels").split("-")[i]));
            }
        }
    }
}
