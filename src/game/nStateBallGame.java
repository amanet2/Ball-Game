package game;

public class nStateBallGame extends nState {
    public nStateBallGame() {
        //generates a "dummy snapshot" of all vars with value of 0/default
        super();
        map.putArg(new gArg("color", "blue") {
            String oldcolor = "blue";
            int changes = 0;
            public void onChange() {
                if(changes > 1)
                    xMain.shellLogic.console.ex(String.format("echo %s#%s changed color to %s#%s", get("name"), oldcolor, value, value));
                else
                    changes++;
                oldcolor = value;
            }
        });
        map.putArg(new gArg("name", "player") {
            String oldname = "player";
            int changes = 0;
            public void onChange() {
                if(changes > 1)
                    xMain.shellLogic.console.ex(String.format("echo %s#%s changed name to %s#%s", oldname, get("color"), value, get("color")));
                else
                    changes++;
                oldname = value;
            }
        });
        map.putArg(new gArg("fv", "0") {
            public void onChange() {
                if(xMain.shellLogic.serverScene.getPlayerById(get("id")) != null)
                    xMain.shellLogic.serverScene.getPlayerById(get("id")).fv = Double.parseDouble(value);
            }
        });
        map.putArg(new gArg("hp", Integer.toString(sSettings.serverMaxHP)) {
            public void onChange() {
                if(Integer.parseInt(value) < 0)
                    value = Integer.toString(sSettings.serverMaxHP);
            }
        });
        map.putArg(new gArg("mov0", "0") {
            public void onChange() {
                gPlayer pl = xMain.shellLogic.serverScene.getPlayerById(get("id"));
                if(pl != null)
                    pl.mov0 = Integer.parseInt(value);
            }
        });
        map.putArg(new gArg("mov1", "0") {
            public void onChange() {
                gPlayer pl = xMain.shellLogic.serverScene.getPlayerById(get("id"));
                if(pl != null)
                    pl.mov1 = Integer.parseInt(value);
            }
        });
        map.putArg(new gArg("mov2", "0") {
            public void onChange() {
                gPlayer pl = xMain.shellLogic.serverScene.getPlayerById(get("id"));
                if(pl != null)
                    pl.mov2 = Integer.parseInt(value);
            }
        });
        map.putArg(new gArg("mov3", "0") {
            public void onChange() {
                gPlayer pl = xMain.shellLogic.serverScene.getPlayerById(get("id"));
                if(pl != null)
                    pl.mov3 = Integer.parseInt(value);
            }
        });
        map.putArg(new gArg("cmdrcv", "0") {
            public void onChange() {
                if(value.equals("1")) {
                    xMain.shellLogic.serverNetThread.clientReceivedCmd(get("id"));
                    value = "0";
                }
            }
        });
        map.putArg(new gArg("cmd", "") {
            public void onChange() {
                if(value.length() > 0)
                    xMain.shellLogic.serverNetThread.handleClientCommand(get("id"), value);
            }
        });
    }
}
