public class xComAddCommandIgnore extends xCom {
    public String doCommand(String fullCommand) {
        if(!sSettings.IS_SERVER)
            return "addcom can only be used by active clients";
        String[] args = fullCommand.split(" ");
        if(args.length < 2)
            return "usage: addcom <string>";
        for(int i = 0; i < args.length; i++) {
            if(args[i].startsWith("$")) {
                if(args[i].contains("#")) {
                    String[] toks = args[i].split("#");
                    if(cServerVars.instance().contains(toks[0].substring(1)))
                        toks[0] = cServerVars.instance().get(toks[0].substring(1));
                    if(cServerVars.instance().contains(toks[1].substring(1)))
                        toks[1] = cServerVars.instance().get(toks[1].substring(1));
                    args[i] = toks[0] + "#" + toks[1];
                }
                else if(cServerVars.instance().contains(args[i].substring(1)))
                    args[i] = cServerVars.instance().get(args[i].substring(1));
            }
        }
        StringBuilder act = new StringBuilder("");
        for(int i = 1; i < args.length; i++) {
            act.append(" ").append(args[i]);
        }
        String actStr = act.substring(1);
        nServer.instance().addExcludingNetCmd("server", actStr);
        return "server net com: " + actStr;
    }
}
