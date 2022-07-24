public class xComBanId extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 2) {
            int banTimeMillis = Integer.parseInt(toks[2]);
            nServer.instance().banIds.put(toks[1], gTime.gameTime+banTimeMillis);
            return "banned " + toks[1] + " for " + banTimeMillis +"ms";
        }
        else if(toks.length > 1) {
            nServer.instance().banIds.put(toks[1], gTime.gameTime+1000);
            return "banned " + toks[1] + " for 1000ms";
        }
        return "usage: banid <id> <optional: time_millis>";
    }
}
