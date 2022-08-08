public class xComAddCommandClient extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(!sSettings.IS_CLIENT)
            return "cl_addcom can only be used by active clients";
        if(toks.length < 2)
            return "usage: cl_addcom <string>";
        StringBuilder act = new StringBuilder("");
        for(int i = 1; i < toks.length; i++) {
            act.append(toks[i]).append(" ");
        }
        String actStr = act.toString();
        nClient.instance().addNetCmd(actStr);
        return "client net com: " + actStr;
    }
}
