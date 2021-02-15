public class xComBanId extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(sSettings.net_server && toks.length > 1) {
            nServer.banClientIds.put(toks[1], System.currentTimeMillis());
        }
        return "cannot banid";
    }
}
