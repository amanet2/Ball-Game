import java.util.HashMap;

public class xComEditorNewMap extends xCom {
    public String doCommand(String fullCommand) {
        xCon.ex("clearthingmap THING_PLAYER;load;cv_maploaded 1;cv_gamemode 0");
        nServer.instance().addExcludingNetCmd("server",
                "cl_clearthingmap THING_PLAYER;cl_load;cv_maploaded 1;cv_gamemode 0");
        //reset game state
        gScoreboard.resetScoresMap();
        nServer.instance().voteSkipMap = new HashMap<>();
        nServer.instance().clientArgsMap.get("server").remove("flagmasterid");
        nServer.instance().clientArgsMap.get("server").remove("virusids");
        cServerLogic.starttime = System.currentTimeMillis();
        cServerLogic.gameover = false;
        if (cGameLogic.isVirus())
            cGameLogic.resetVirusPlayers();
        return "";
    }
}
