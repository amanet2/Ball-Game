public class xComSendCmdClient extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 1) {
            String cmd = fullCommand.replace(toks[0]+" ", "");
            if(cmd.length() > 0) {
                nClient.instance().addNetCmd(cmd);
                return cmd;
            }
        }
        return "usage: cl_sendcmd <any valid console command>";
    }
}