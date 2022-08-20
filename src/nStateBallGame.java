public class nStateBallGame extends nState {
    public nStateBallGame() {
        //generates a "dummy snapshot" of all vars with value of 0/default
        //
        super();
        stateMap.put("id", "");
        stateMap.put("name", "");
        stateMap.put("x", "0");
        stateMap.put("y", "0");
        stateMap.put("hp", "0");
        stateMap.put("rcv", "0");
        stateMap.put("cmd", "");
        stateMap.put("msg", "");
    }
}
