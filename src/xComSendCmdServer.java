public class xComSendCmdServer extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 1) {
            String pid = toks[1];
            if(!nServer.instance().clientArgsMap.containsKey(pid))
                pid = "";
            String cmd = fullCommand.replace(toks[0]+" ", "");
            if(cmd.length() > 0) {
                if (pid.length() > 0)
                    nServer.instance().addNetCmd(pid, cmd.replace(pid + " ", ""));
                else
                    nServer.instance().addNetCmd(cmd);
                return cmd;
            }
        }
        return "usage: sv_sendcmd <any valid console command>";
    }
}