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
        map.putArg(new gArg("vels", "0-0-0-0") {
            public void onChange() {
                gPlayer pl = cServerLogic.scene.getPlayerById(get("id"));
                if(pl == null)
                    return;
                String[] vels = value.split("-");
                pl.put("vel0", vels[0]);
                pl.put("vel1", vels[1]);
                pl.put("vel2", vels[2]);
                pl.put("vel3", vels[3]);
            }
        });
        map.put("px", "0");
        map.put("py", "0");
        map.put("pw", "0");
        map.put("ph", "0");
        map.putArg(new gArg("cmdrcv", "0") {
            public void onChange() {
                if(value.equals("1")) {
//                    if(nServer.instance().clientNetCmdMap.get(get("id")).size() > 0)
//                        nServer.instance().clientNetCmdMap.get(get("id")).remove();
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
                    nServer.instance().addExcludingNetCmd("server", "echo " + value);
                    //handle special sounds, etc
                    String testmsg = value.substring(value.indexOf(':')+2);
                    nServer.instance().checkMessageForSpecialSound(testmsg);
                    nServer.instance().checkClientMessageForVoteSkip(get("id"), testmsg);
                }
            }
        });
    }
}
