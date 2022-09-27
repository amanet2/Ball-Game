import java.util.ArrayList;

public class xComEditorNewMap extends xCom {
    public String doCommand(String fullCommand) {
        xCon.ex("exec scripts/e_newmap");
        //reset game state
        gScoreboard.resetScoresMap();
        nServer.instance().voteSkipList = new ArrayList<>();
        return "";
    }
}
