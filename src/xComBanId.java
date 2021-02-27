public class xComBanId extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(sSettings.net_server && toks.length > 1) {
            nServer.instance().banIds.put(toks[1], System.currentTimeMillis()+10000);
            return "banned " + toks[1];
        }
        return "cannot banid";
    }
}
