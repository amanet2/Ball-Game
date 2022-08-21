public class nStateBallGame extends nState {
    public nStateBallGame() {
        //generates a "dummy snapshot" of all vars with value of 0/default
        //
        super();
        map.put("id", "");
        map.putArg(new gArg("color", "blue") {
            public void onChange() {
                nServer.instance().addExcludingNetCmd("server",
                        String.format("echo %s changed color to %s", get("name"), value));
            }
        });
        map.putArg(new gArg("name", "player") {
            String oldname = "player";
            public void onChange() {
                nServer.instance().addExcludingNetCmd("server",
                        String.format("echo %s changed name to %s", oldname, value));
                oldname = value;
            }
        });
        map.put("x", "0");
        map.put("y", "0");
        map.put("hp", "0");
        map.put("fv", "0");
        map.put("vels", "0-0-0-0");
        map.putArg(new gArg("cmdrcv", "0") {
            public void onChange() {
                System.out.println("cmdrcv");
                if(value.equals("1")) {
//                    nServer.instance().clientNetCmdMap.get(get("id")).remove();
                    value = "0";
                }
            }
        });
        map.put("cmd", "");
        map.put("msg", "");
    }
}
