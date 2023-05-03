public class nStateBallGame extends nState {
    public nStateBallGame() {
        //generates a "dummy snapshot" of all vars with value of 0/default
        //
        super();
        map.putArg(new gArg("color", "blue") {
            String oldcolor = "blue";
            public void onChange() {
                xCon.ex(String.format("echo %s#%s changed color to %s#%s", get("name"), oldcolor, value, value));
                oldcolor = value;
            }
        });
        map.putArg(new gArg("name", "player") {
            String oldname = "player";
            public void onChange() {
                xCon.ex(String.format("echo %s#%s changed name to %s#%s", oldname, get("color"), value, get("color")));
                oldname = value;
            }
        });
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
        map.putArg(new gArg("cmdrcv", "0") {
            public void onChange() {
                if(value.equals("1")) {
//                    xCon.instance().debug("SERVER_CMDRCV_" + get("id") + ": " + cServerLogic.netServerThread.clientNetCmdMap.toString());
                    if(cServerLogic.netServerThread.clientNetCmdMap.get(get("id")).size() > 0)
                        cServerLogic.netServerThread.clientNetCmdMap.get(get("id")).remove();
                    value = "0";
                }
            }
        });
        map.putArg(new gArg("cmd", "") {
            public void onChange() {
                if(value.length() > 0) {
                    cServerLogic.netServerThread.handleClientCommand(get("id"), value);
                }
            }
        });
    }

    private void setPlayerVal(String key, String val) {
        gPlayer pl = cServerLogic.getPlayerById(get("id"));
        if(pl != null)
            pl.put(key, val);
    }
}
