public class xComAddTimeEvent extends xCom {
    public String doCommand(String fullCommand) {
        if(!sSettings.IS_SERVER)
            return "addevent can only be used by active server";
        String[] args = fullCommand.split(" ");
        if(args.length < 3)
            return "usage: addevent <time> <string to execute>";
        for(int i = 0; i < args.length; i++) {
            if(!args[i].startsWith("$"))
                continue;
            if(cServerVars.instance().contains(args[i].substring(1)))
                args[i] = cServerVars.instance().get(args[i].substring(1));
            else if(sVars.get(args[i]) != null)
                args[i] = sVars.get(args[i]);
        }
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
