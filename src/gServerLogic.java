public class gServerLogic {
    gScene scene;
    public static void gameLoop() {
        if(sVars.getInt("timelimit") > 0)
            cVars.putLong("timeleft",
                    sVars.getLong("timelimit") - (int) (uiInterface.gameTime - cVars.getLong("starttime")));
        else
            cVars.putLong("timeleft", -1);
        nServer.instance().processPackets();
        nServer.instance().checkOutgoingCmdMap();
        nServer.instance().checkForUnhandledQuitters();
//        cGameLogic.checkHealthStatus();
        cGameLogic.checkForMapChange();
        cServerLogic.checkGameState();
    }
}
