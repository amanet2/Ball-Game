public class xComGetSnapshot extends xCom {
    //usage: getsnap $id $key
    public String doCommand(String fullCommand) {
        if(eUtils.argsLength(fullCommand) < 2)
            return nServer.instance().clientStateSnapshots.toString();
        String[] args = eUtils.parseScriptArgsServer(fullCommand);
        String cid = args[1];
        if(!nServer.instance().clientStateSnapshots.containsKey(cid))
            return "null";
        nStateMap clientSnapshot = new nStateMap(nServer.instance().clientStateSnapshots.get(cid));
        if(args.length < 3)
            return clientSnapshot.toString();
        String tk = args[2];
        nState clientState = clientSnapshot.get(cid);
        if(!clientState.contains(tk))
            return "null";
        return clientState.get(tk);
    }
}
