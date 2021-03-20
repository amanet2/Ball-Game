public class xComSendCmd extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 1) {
            String cmd = fullCommand.replace(toks[0]+" ", "");
            if(cmd.length() > 0) {
                if(sSettings.net_server)
                    nServer.instance().addNetCmd(cmd);
                else
                    nClient.instance().addNetCmd(cmd);
                return cmd;
            }
        }
        return "usage: sendcmd <any valid console command>";
    }
}