import java.util.ArrayList;

public class xComEditorNewMap extends xCom {
    public String doCommand(String fullCommand) {
        xCon.ex("clearthingmap THING_PLAYER;load;cv_maploaded 1;cv_gamemode 0");
        nServer.instance().addExcludingNetCmd("server",
                "cl_clearthingmap THING_PLAYER;cl_load;cv_maploaded 1;cv_gamemode 0");
        //reset game state
        gScoreboard.resetScoresMap();
        nServer.instance().voteSkipList = new ArrayList<>();
        nServer.instance().serverVars.remove("flagmasterid");
        nServer.instance().serverVars.remove("virusids");
        if (cGameLogic.isGame(cGameLogic.VIRUS))
            xCon.ex("exec scripts/resetvirus");
        return "";
    }
}
