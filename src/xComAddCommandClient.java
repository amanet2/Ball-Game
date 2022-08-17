public class xComAddCommandClient extends xCom {
    public String doCommand(String fullCommand) {
        String[] args = fullCommand.split(" ");
        if(!sSettings.IS_CLIENT)
            return "cl_addcom can only be used by active clients";
        if(args.length < 2)
            return "usage: cl_addcom <string>";
        for(int i = 1; i < args.length; i++) {
            //parse the $ vars for placing prefabs
            if(args[i].startsWith("$")) {
                if(cClientVars.instance().contains(args[i].substring(1)))
                    args[i] = cClientVars.instance().get(args[i].substring(1));
                else if(sVars.get(args[i]) != null)
                    args[i] = sVars.get(args[i]);
            }
        }
        StringBuilder act = new StringBuilder("");
        for(int i = 1; i < args.length; i++) {
            act.append(" ").append(args[i]);
        }
        String actStr = act.substring(1);
        nClient.instance().addNetCmd(actStr);
        return "client net com: " + actStr;
    }
}
