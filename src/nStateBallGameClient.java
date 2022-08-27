public class nStateBallGameClient extends nState {
    public nStateBallGameClient() {
        //generates a "dummy snapshot" of all vars with value of 0/default
        //
        super();
        map.put("id", "");
        map.putArg(new gArg("color", "blue") {
            public void onChange() {
                gPlayer p = cClientLogic.getPlayerById(get("id"));
                System.out.println(p);
                System.out.println(gColors.instance().getColorFromName("clrp_" + value));
                if(p == null || gColors.instance().getColorFromName("clrp_" + value) == null)
                    return;
                p.setSpriteFromPath(eUtils.getPath(String.format("animations/player_%s/%s", value,
                        p.get("pathsprite").substring(p.get("pathsprite").lastIndexOf('/')))));
            }
        });
        map.put("name", "player");
        map.putArg(new gArg("x", "0") {
            public void onChange() {
                if(!sSettings.smoothing && cClientLogic.getPlayerById(get("id")) != null)
                    cClientLogic.getPlayerById(get("id")).put("coordx", value);
            }
        });
        map.putArg(new gArg("y", "0") {
            public void onChange() {
                if(!sSettings.smoothing && cClientLogic.getPlayerById(get("id")) != null)
                    cClientLogic.getPlayerById(get("id")).put("coordy", value);
            }
        });
        map.put("hp", "0");
        map.putArg(new gArg("fv", "0") {
            public void onChange() {
                gPlayer pl = cClientLogic.getPlayerById(get("id"));
                if(pl == null)
                    return;
                pl.put("fv", value);
                pl.checkSpriteFlip();
            }
        });
        map.putArg(new gArg("vels", "0-0-0-0") {
            public void onChange() {
                gPlayer pl = cClientLogic.getPlayerById(get("id"));
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
        map.put("score", "0:0");
        map.put("cmdrcv", "0");
        map.put("cmd", "");
        map.put("msg", "");
    }
}
