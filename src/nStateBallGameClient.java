public class nStateBallGameClient extends nState {
    public nStateBallGameClient() {
        //generates a "dummy snapshot" of all vars with value of 0/default
        //
        super();
        map.put("id", "");
        map.putArg(new gArg("color", "blue") {
            public void onChange() {
                gPlayer p = cClientLogic.getPlayerById(get("id"));
                if(p == null || gColors.getColorFromName("clrp_" + value) == null)
                    return;
                p.put("color", value);
                p.setSpriteFromPath(eUtils.getPath(String.format("animations/player_%s/%s", value,
                        p.get("pathsprite").substring(p.get("pathsprite").lastIndexOf('/')))));
            }
        });
        map.putArg(new gArg("coords", "0:0") {
            public void onChange() {
                String[] coords = value.split(":");
                    setPlayerVal("coordx", coords[0]);
                    setPlayerVal("coordy", coords[1]);
            }
        });
        map.putArg(new gArg("fv", "0") {
            public void onChange() {
                if(get("id").equals(uiInterface.uuid))
                    return;
                gPlayer pl = cClientLogic.getPlayerById(get("id"));
                if(pl == null)
                    return;
                pl.put("fv", value);
                pl.checkSpriteFlip();
            }
        });
        map.putArg(new gArg("vel0", "0") {
            public void onChange() {
                setPlayerVal("vel0", value);
            }
        });
        map.putArg(new gArg("vel1", "0") {
            public void onChange() {
                setPlayerVal("vel1", value);
            }
        });
        map.putArg(new gArg("vel2", "0") {
            public void onChange() {
                setPlayerVal("vel2", value);
            }
        });
        map.putArg(new gArg("vel3", "0") {
            public void onChange() {
                setPlayerVal("vel3", value);
            }
        });
    }

    private void setPlayerVal(String key, String val) {
        gPlayer pl = cClientLogic.getPlayerById(get("id"));
        if(pl != null)
            pl.put(key, val);
    }
}
