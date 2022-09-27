public class xComAddCommandIgnore extends xCom {
    public String doCommand(String fullCommand) {
        if(!sSettings.IS_SERVER)
            return "addcomi can only be used by the host";
        String[] args = fullCommand.split(" ");
        if(args.length < 3)
            return "usage: addcomi <ignore id> <string>";
        for(int i = 1; i < args.length; i++) {
            if(args[i].contains("#")) {
                String[] toks = args[i].split("#");
                for(int j = 0; j < toks.length; j++) {
                    if(!toks[j].startsWith("$"))
                        continue;
                    if(cServerVars.instance().contains(toks[j].substring(1)))
                        toks[j] = cServerVars.instance().get(toks[j].substring(1));
                    else if(sVars.get(toks[j]) != null)
                        toks[j] = sVars.get(toks[0]);
                }
                args[i] = toks[0] + "#" + toks[1];
            }
            else if(args[i].startsWith("$") && cServerVars.instance().contains(args[i].substring(1)))
                args[i] = cServerVars.instance().get(args[i].substring(1));
            else if(args[i].startsWith("$") && sVars.get(args[i]) != null)
                args[i] = sVars.get(args[i]);
        }
        String ignoreId = args[1];
        StringBuilder act = new StringBuilder("");
        for(int i = 2; i < args.length; i++) {
            act.append(" ").append(args[i]);
        }
        String actStr = act.substring(1);
        nServer.instance().addExcludingNetCmd(ignoreId, actStr);
        return "server net com ignoring: " + actStr;
    }
}
