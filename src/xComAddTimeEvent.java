public class xComAddTimeEvent extends xCom {
    public String doCommand(String fullCommand) {
        if(!sSettings.IS_SERVER)
            return "addevent can only be used by active server";
        if(eUtils.argsLength(fullCommand) < 3)
            return "usage: addevent <time> <string to execute>";
        String[] args = eUtils.parseScriptArgsServer(fullCommand);
        StringBuilder act = new StringBuilder("");
        for(int i = 2; i < args.length; i++) {
            act.append(" ").append(args[i]);
        }
        String timeToExec = args[1];
        String actStr = act.substring(1);
        cServerLogic.timedEvents.put(timeToExec,
                new gTimeEvent() {
                    public void doCommand() {
                        xCon.ex(actStr);
                    }
                }
        );
        return "added time event @" + timeToExec + ": " + actStr;
    }
}
