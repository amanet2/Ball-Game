public class xComAddCommand extends xCom {
    public String doCommand(String fullCommand) {
        if(!sSettings.IS_SERVER)
            return "addcom can only be used by active clients";
        if(eUtils.argsLength(fullCommand) < 2)
            return "usage: addcom <command to execute>";
        String[] args = eUtils.parseScriptArgsServer(fullCommand);
        StringBuilder act = new StringBuilder("");
        for(int i = 1; i < args.length; i++) {
            act.append(" ").append(args[i]);
        }
        String actStr = act.substring(1);
        nServer.instance().addNetCmd(actStr);
        return "server net com: " + actStr;
    }
}
