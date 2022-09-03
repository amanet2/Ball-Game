public class xComSetCamMovs extends xCom {
    public String doCommand(String fullCommand) {
        String[] args = fullCommand.split(" ");
        if(args.length < 5)
            return "0";
        for(int i = 1; i < args.length; i++) {
            if(args[i].startsWith("$")) {
                if(cServerVars.instance().contains(args[i].substring(1)))
                    args[i] = cServerVars.instance().get(args[i].substring(1));
                else if(cClientVars.instance().contains(args[i].substring(1)))
                    args[i] = cClientVars.instance().get(args[i].substring(1));
                else if(sVars.get(args[i]) != null)
                    args[i] = sVars.get(args[i]);
            }
        }
        gCamera.put("mov0", args[1]);
        gCamera.put("mov1", args[2]);
        gCamera.put("mov2", args[3]);
        gCamera.put("mov3", args[4]);
        return "1";
    }
}