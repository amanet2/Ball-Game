public class xComSetServerState extends xCom {
    //usage: setnstate $id $key $value
    public String doCommand(String fullCommand) {
        String[] args = fullCommand.split(" ");
        nStateMap serverState = nServer.instance().masterStateMap;
        if(args.length < 2)
            return serverState.toString();
        for(int i = 1; i < args.length; i++) {
            if(args[i].startsWith("$")) {
                if(cServerVars.instance().contains(args[i].substring(1)))
                    args[i] = cServerVars.instance().get(args[i].substring(1));
                else if(sVars.get(args[i]) != null)
                    args[i] = sVars.get(args[i]);
            }
        }
        String pid = args[1];
        nState clientState = serverState.get(pid);
        if(args.length < 3)
            return clientState.toString();
        String tk = args[2];
        StringBuilder tvb = new StringBuilder();
        for(int i = 3; i < args.length; i++) {
            tvb.append(" ").append(args[i]);
        }
        String tv = tvb.substring(1);
        clientState.put(tk, tv);
        return clientState.get(tk);
    }
}
