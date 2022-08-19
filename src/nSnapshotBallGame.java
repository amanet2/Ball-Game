import java.util.HashMap;

public class nSnapshotBallGame extends nSnapshot {
    public nSnapshotBallGame() {
        //dummy snapshot with all vars 0
        super();
        statesMap = new HashMap<>();
        nState serverState = new nState();
        serverState.put("cmd", "");
        statesMap.put("server", new nState());
    }
}
