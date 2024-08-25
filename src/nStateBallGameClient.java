public class nStateBallGameClient extends nState {
    public nStateBallGameClient() {
        //generates a "dummy snapshot" of all vars with value of 0/default
        //
        super();
        map.put("id", "");
        map.putArg(new gArg("color", "blue") {
            public void onChange() {
                gPlayer p = xMain.shellLogic.getPlayerById(get("id"));
                if(p == null || gColors.getColorFromName("clrp_" + value) == null)
                    return;
                String oldColor = p.color;
                p.color = value;
                p.setSpriteFromPath(p.spritePath.replace(oldColor, p.color));
            }
        });
        map.putArg(new gArg("coords", "0:0") {
            public void onChange() {
                gPlayer pl = xMain.shellLogic.getPlayerById(get("id"));
                if(pl != null) {
                    if (pl.interpTick < sSettings.gameTime) {
                        pl.interpTick = sSettings.gameTime + pl.interpDelay;
                        setPlayerVal("coords", value);
                    }
                }
            }
        });
        map.putArg(new gArg("fv", "0") {
            public void onChange() {
                if(get("id").equals(sSettings.uuid))
                    return;
                gPlayer pl = xMain.shellLogic.getPlayerById(get("id"));
                if(pl == null)
                    return;
                pl.fv = Double.parseDouble(value);
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
        gPlayer pl = xMain.shellLogic.getPlayerById(get("id"));
        if(pl != null)
            pl.args.put(key, val);
    }
}
