public class xComGetRes extends xCom {
    public String doCommand(String fullCommand) {
        if(eUtils.argsLength(fullCommand) < 2)
            return "null";
        String[] args = eUtils.parseScriptArgsServer(fullCommand);
        String tk = args[1];
        if(args.length < 3) {
            if (!cServerVars.instance().contains(tk))
                return "null";
            return cServerVars.instance().get(tk);
        }
        StringBuilder tvb = new StringBuilder();
        for(int i = 2; i < args.length; i++) {
            tvb.append(" ").append(args[i]);
        }
        String tv = tvb.substring(1);
        String res = xCon.ex(tv);
        cServerVars.instance().put(tk, res);
        return cServerVars.instance().get(tk);
    }
}
