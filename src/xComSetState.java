public class xComSetState extends xCom {
    //usage: setnstate $id $key $value
    public String doCommand(String fullCommand) {
        nStateMap serverState = nServer.instance().masterStateMap;
        if(eUtils.argsLength(fullCommand) < 2)
            return serverState.toString();
        String[] args = eUtils.parseScriptArgsServer(fullCommand);
        String pid = args[1];
        nState clientState = serverState.get(pid);
        if(clientState == null)
            return "null";
        if(args.length < 3)
            return clientState.toString();
        String tk = args[2];
        if(args.length < 4) {
            if(!clientState.contains(tk))
                return "null";
            return clientState.get(tk);
        }
        StringBuilder tvb = new StringBuilder();
        for(int i = 3; i < args.length; i++) {
            tvb.append(" ").append(args[i]);
        }
        String tv = tvb.substring(1);
        clientState.put(tk, tv);
        return clientState.get(tk);
    }
}
