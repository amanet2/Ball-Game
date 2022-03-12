public class cServerVars extends gArgs {
    protected void init() {
        putArg(new gArg("timelimit", "180000") {
            public void onChange() {
                cServerLogic.timelimit = Integer.parseInt(value);
                cServerLogic.starttime = System.currentTimeMillis();
            }
        });
        putArg(new gArg("maxhp", "500") {
            public void onChange() {
                cServerLogic.maxhp = Integer.parseInt(value);
                nServer.instance().addNetCmd("cv_maxstockhp " + cServerLogic.maxhp);
            }
        });
    }
    public static gArgs instance() {
        if(instance == null) {
            instance = new cServerVars();
            instance.init();
        }
        return instance;
    }
}
