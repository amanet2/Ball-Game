import java.util.ArrayList;

public class xComEditorNewMap extends xCom {
    public String doCommand(String fullCommand) {
        xCon.ex("exec scripts/e_newmap");
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
