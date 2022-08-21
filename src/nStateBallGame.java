public class nStateBallGame extends nState {
    public nStateBallGame() {
        //generates a "dummy snapshot" of all vars with value of 0/default
        //
        super();
        map.put("id", "");
        map.put("color", "blue");
        map.put("name", "player");
        map.put("x", "0");
        map.put("y", "0");
        map.put("hp", "0");
        map.put("fv", "0");
        map.put("vels", "0-0-0-0");
        map.put("cmdrcv", "0");
        map.put("cmd", "");
        map.put("msg", "");
    }
}
