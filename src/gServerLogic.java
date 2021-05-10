public class gServerLogic {
    gScene scene;
    public static void gameLoop() {
        if(sVars.getInt("timelimit") > 0)
            cVars.putLong("timeleft",
                    sVars.getLong("timelimit") - (int) (uiInterface.gameTime - cVars.getLong("starttime")));
        else
            cVars.putLong("timeleft", -1);
//        cGameLogic.checkHealthStatus();
        cGameLogic.checkForMapChange();
        cServerLogic.checkGameState();
    }

    static void changeMap(String mapPath) {
        System.out.println("CHANGING MAP: " + mapPath);
        xCon.ex("exec maps/" + mapPath);
        nServer.instance().addExcludingNetCmd("server", "clearthingmap THING_PLAYER;cv_maploaded 0;load");
        for(String id : nServer.instance().clientIds) {
            nServer.instance().sendMap(id);
            String postString = String.format("spawnplayer %s %s %s",
                    gClientLogic.getUserPlayer().get("id"),
                    gClientLogic.getUserPlayer().get("coordx"),
                    gClientLogic.getUserPlayer().get("coordy")
            );
            nServer.instance().addNetCmd(id, postString);
            xCon.ex("respawnnetplayer " + id);
        }
    }
}
