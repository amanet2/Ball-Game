public class xComSetVar extends xCom {
    public String doCommand(String fullCommand) {
        //usage setvar $key $val
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
        cServerVars.instance().put(tk, tv);
        return cServerVars.instance().get(tk);
    }
}
