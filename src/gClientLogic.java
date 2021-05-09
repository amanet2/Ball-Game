public class gClientLogic {
    gScene scene;
    public static void gameLoop() {
        oDisplay.instance().checkDisplay();
        oAudio.instance().checkAudio();
        gCamera.updatePosition();
        nClient.instance().processPackets();
        if(sSettings.show_mapmaker_ui)
            cScripts.selectThingUnderMouse();
    }

}
