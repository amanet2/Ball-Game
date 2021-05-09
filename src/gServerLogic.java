public class gServerLogic {
    gScene scene;
    public static void gameLoop() {
        nServer.instance().processPackets();
        nServer.instance().checkOutgoingCmdMap();
        nServer.instance().checkForUnhandledQuitters();
//        cGameLogic.checkHealthStatus();
        cGameLogic.checkForMapChange();
        cServerLogic.checkGameState();
    }
}
