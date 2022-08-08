public class xComAddCommand extends xCom {
    public String doCommand(String fullCommand) {
        if(!sSettings.IS_SERVER)
            return "addcom can only be used by active clients";
        String[] args = fullCommand.split(" ");
        if(args.length < 2)
            return "usage: addcom <string>";
        for(int i = 0; i < args.length; i++) {
            if(args[i].startsWith("$") && cServerVars.instance().contains(args[i].substring(1))) {
                args[i] = cServerVars.instance().get(args[i].substring(1));
            }
        }
        StringBuilder act = new StringBuilder("");
        for(int i = 1; i < args.length; i++) {
            act.append(" ").append(args[i]);
        }
        String actStr = act.substring(1);
        nServer.instance().addNetCmd(actStr);
        System.out.println(actStr);
        return "server net com: " + actStr;
    }
}
