public class xComGetResClient extends xCom {
    public String doCommand(String fullCommand) {
        if(eUtils.argsLength(fullCommand) < 2)
            return "null";
        String[] args = eUtils.parseScriptArgsClient(fullCommand);
        String tk = args[1];
        if(args.length < 3) {
            if (!cClientVars.instance().contains(tk))
                return "null";
            return cClientVars.instance().get(tk);
        }
        StringBuilder tvb = new StringBuilder();
        for(int i = 2; i < args.length; i++) {
            tvb.append(" ").append(args[i]);
        }
        String tv = tvb.substring(1);
        String res = xCon.ex(tv);
        cClientVars.instance().put(tk, res);
        return cClientVars.instance().get(tk);
    }
}
