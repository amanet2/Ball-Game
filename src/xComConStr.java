public class xComConStr extends xCom {
    //concatenate two strings with optional joining char
    //usage: constr $newvarname <disparate elements to combine and store in newvarname>
    public String doCommand(String fullCommand) {
        if(eUtils.argsLength(fullCommand) < 3)
            return "null";
        String[] args = eUtils.parseScriptArgsServer(fullCommand);
        String tk = args[1];
        StringBuilder esb = new StringBuilder();
        for(int i = 2; i < args.length; i++) {
            esb.append(args[i]);
        }
        String es = esb.toString();
        cServerVars.instance().put(tk, es);
        return es;
    }
}
