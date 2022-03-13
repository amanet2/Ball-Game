import java.util.HashMap;

public class cServerVars extends gArgSet {
    protected static gArgSet instance;

    protected cServerVars() {
        args = new HashMap<>();
    }
    protected void init() {
        putArg(new gArg("timelimit", "180000") {
            public void onChange() {
                cServerLogic.timelimit = Integer.parseInt(value);
                cServerLogic.starttime = System.currentTimeMillis();
            }
        });
        putArg(new gArg("maxhp", "500") {
            public void onChange() {
                int newval = Integer.parseInt(value);
                if(sSettings.IS_SERVER && cServerLogic.maxhp != newval) {
                    cServerLogic.maxhp = newval;
                    nServer.instance().addNetCmd("cv_maxhp " + cServerLogic.maxhp);
                    for(String s : cServerLogic.scene.getThingMap("THING_PLAYER").keySet()) {
                        gPlayer p = cServerLogic.scene.getPlayerById(s);
                        p.putInt("stockhp", cServerLogic.maxhp);
                    }
                }
            }
        });
    }
    public static gArgSet instance() {
        if(instance == null) {
            instance = new cServerVars();
            instance.init();
        }
        return instance;
    }
}
