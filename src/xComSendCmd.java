public class xComSendCmd extends xCom {
    public String doCommand(String fullCommand) {
        if(sSettings.net_server) {
            String[] toks = fullCommand.split(" ");
            if(toks.length > 1) {
                String id = toks[1];
                String cmd = fullCommand.replace(toks[0]+" "+id+" ", "");
                nServer.instance().addNetCmd(toks[1], cmd);
            }
        }
        return "usage: sendcmd <id> <any valid console command>";
    }
}