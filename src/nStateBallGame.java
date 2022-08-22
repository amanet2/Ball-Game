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
                nServer.instance().addExcludingNetCmd("server", String.format("echo %s changed name to %s",
                        oldname + "#"+get("color"), value + "#"+get("color")));
                oldname = value;
            }
        });
        map.put("x", "0");
        map.put("y", "0");
        map.put("hp", "0");
        map.putArg(new gArg("fv", "0") {
            public void onChange() {
                if(cServerLogic.scene.getPlayerById(get("id")) != null)
                    cServerLogic.scene.getPlayerById(get("id")).put("fv", value);
            }
        });
        map.put("vels", "0-0-0-0");
        map.put("px", "0");
        map.put("py", "0");
        map.put("pw", "0");
        map.put("ph", "0");
        map.putArg(new gArg("cmdrcv", "0") {
            public void onChange() {
                if(value.equals("1")) {
//                    nServer.instance().clientNetCmdMap.get(get("id")).remove();
                    value = "0";
                }
            }
        });
        map.putArg(new gArg("cmd", "") {
            public void onChange() {
                if(value.length() > 0) {
                    nServer.instance().handleClientCommand(get("id"), value);
                }
            }
        });
        map.putArg(new gArg("msg", "") {
            public void onChange() {
                if(value.length() > 0) {
                    nServer.instance().handleClientMessage(value);
                    nServer.instance().checkClientMessageForVoteSkip(get("id"), value.substring(value.indexOf(':')+2));
                }
            }
        });
    }
}
