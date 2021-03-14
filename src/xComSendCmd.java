public class xComSendCmd extends xCom {
    public String doCommand(String fullCommand) {
        if(sSettings.net_server) {
            String[] toks = fullCommand.split(" ");
            if(toks.length > 1) {
                String id = toks[1];
                String cmd = fullCommand.replace(toks[0]+" "+id+" ", "");
                if(cmd.length() > 0) {
                    nServer.instance().addNetCmd(toks[1], cmd);
                    return cmd;
                }
            }
        }
        else if(sSettings.net_client) {
            String[] toks = fullCommand.split(" ");
            if(toks.length > 1) {
                String cmd = fullCommand.replace(toks[0]+" ", "");
                if(cmd.length() > 0) {
                    nClient.instance().addNetCmd(cmd);
                    return cmd;
                }
            }
        }
        return "usage: sendcmd <id> <any valid console command>";
    }
}