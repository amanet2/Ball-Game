public class nStateBallGame extends nState {
    public nStateBallGame() {
        //generates a "dummy snapshot" of all vars with value of 0/default
        //
        super();
        map.putArg(new gArg("color", "blue") {
            String oldcolor = "blue";
            public void onChange() {
                nServer.instance().addExcludingNetCmd("server",
                        String.format("echo %s#%s changed color to %s#%s", get("name"), oldcolor, value, value));
                oldcolor = value;
            }
        });
        map.putArg(new gArg("name", "player") {
            String oldname = "player";
            public void onChange() {
                nServer.instance().addExcludingNetCmd("server",
                        String.format("echo %s#%s changed name to %s#%s", oldname, get("color"), value, get("color")));
                oldname = value;
            }
        });
//        map.putArg(new gArg("x", "0") {
//            public void onChange() {
//                if(sSettings.smoothing && cServerLogic.scene.getPlayerById(get("id")) != null)
//                    cServerLogic.scene.getPlayerById(get("id")).put("coordx", value);
//            }
//        });
//        map.putArg(new gArg("y", "0") {
//            public void onChange() {
//                if(sSettings.smoothing && cServerLogic.scene.getPlayerById(get("id")) != null)
//                    cServerLogic.scene.getPlayerById(get("id")).put("coordy", value);
//            }
//        });
        map.putArg(new gArg("fv", "0") {
            public void onChange() {
                if(cServerLogic.scene.getPlayerById(get("id")) != null)
                    cServerLogic.scene.getPlayerById(get("id")).put("fv", value);
            }
        });
        map.putArg(new gArg("mov0", "0") {
            public void onChange() {
                setPlayerVal("mov0", value);
            }
        });
        map.putArg(new gArg("mov1", "0") {
            public void onChange() {
                setPlayerVal("mov1", value);
            }
        });
        map.putArg(new gArg("mov2", "0") {
            public void onChange() {
                setPlayerVal("mov2", value);
            }
        });
        map.putArg(new gArg("mov3", "0") {
            public void onChange() {
                setPlayerVal("mov3", value);
            }
        });
        map.putArg(new gArg("vel0", "0") {
            public void onChange() {
                setVelVec("0", value);

            }
        });
        map.putArg(new gArg("vel1", "0") {
            public void onChange() {
                setVelVec("1", value);

            }
        });
        map.putArg(new gArg("vel2", "0") {
            public void onChange() {
                setVelVec("2", value);

            }
        });
        map.putArg(new gArg("vel3", "0") {
            public void onChange() {
                setVelVec("3", value);
            }
        });
        map.putArg(new gArg("cmdrcv", "0") {
            public void onChange() {
                if(value.equals("1")) {
//                    xCon.instance().debug("SERVER_CMDRCV_" + get("id") + ": " + nServer.instance().clientNetCmdMap.toString());
                    if(nServer.instance().clientNetCmdMap.get(get("id")).size() > 0)
                        nServer.instance().clientNetCmdMap.get(get("id")).remove();
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
                    xCon.ex("exec scripts/checkmsgforsound " + testmsg); //check for special sound
                    nServer.instance().checkClientMessageForTimeAndVoteSkip(get("id"), testmsg);
                }
            }
        });
    }

    private void setVelVec(String dir, String val) {
        gPlayer pl = cServerLogic.scene.getPlayerById(get("id"));
        if(pl != null)
            pl.put("vel"+dir, val);
    }

    private void setPlayerVal(String key, String val) {
        gPlayer pl = cServerLogic.getPlayerById(get("id"));
        if(pl != null)
            pl.put(key, val);
    }
}
