public class xComAddCommand extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(!sSettings.IS_SERVER)
            return "addcom can only be used by active clients";
        if(toks.length < 2)
            return "usage: addcom <string>";
        StringBuilder act = new StringBuilder("");
        for(int i = 1; i < toks.length; i++) {
            act.append(" ").append(toks[i]);
        }
        String actStr = act.substring(1);
        nServer.instance().addNetCmd(actStr);
        System.out.println(actStr);
        return "server net com: " + actStr;
    }
}
