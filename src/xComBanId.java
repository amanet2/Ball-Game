public class xComBanId extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(sSettings.net_server && toks.length > 1) {
            for(String s : nServer.clientArgsMap.keySet()) {
                if(s.equals(toks[1])) {
                    return String.format("kicked %s", toks[1]);
                }
            }
        }
        return "cannot banid";
    }
}
