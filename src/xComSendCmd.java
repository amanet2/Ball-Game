public class xComSendCmd extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 1) {
            String pid = toks[1];
            if((sSettings.IS_SERVER && !nServer.instance().clientArgsMap.containsKey(pid))
            || (sSettings.IS_CLIENT && !nClient.instance().serverArgsMap.containsKey(pid)))
                pid = "";
            String cmd = fullCommand.replace(toks[0]+" ", "");
            if(cmd.length() > 0) {
                if (sSettings.IS_SERVER) {
                    if (pid.length() > 0)
                        nServer.instance().addNetCmd(pid, cmd.replace(pid + " ", ""));
                    else
                        nServer.instance().addNetCmd(cmd);
                }
                else
                    nClient.instance().addNetCmd(cmd);
                return cmd;
            }
        }
        return "usage: sendcmd <any valid console command>";
    }
}