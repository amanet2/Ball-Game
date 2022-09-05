public class xComAddCommandClient extends xCom {
    public String doCommand(String fullCommand) {
        if(!sSettings.IS_CLIENT)
            return "cl_addcom can only be used by active clients";
        if(eUtils.argsLength(fullCommand) < 2)
            return "usage: cl_addcom <command to execute>";
        String[] args = eUtils.parseScriptArgsClient(fullCommand);
        StringBuilder act = new StringBuilder("");
        for(int i = 1; i < args.length; i++) {
            act.append(" ").append(args[i]);
        }
        String actStr = act.substring(1);
        nClient.instance().addNetCmd(actStr);
        return "client net com: " + actStr;
    }
}
