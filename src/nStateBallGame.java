public class nStateBallGame extends nState {
    public nStateBallGame() {
        //generates a "dummy snapshot" of all vars with value of 0/default
        super();
        stateMap.put("x", "0");
        stateMap.put("y", "0");
        stateMap.put("cmd", "");
        stateMap.put("cmdrcv", "");
        stateMap.put("msg", "");
    }
}
