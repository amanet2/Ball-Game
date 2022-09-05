public class xComGetAdd extends xCom {
    public String doCommand(String fullCommand) {
        //usage: getadd $result $num1 $num2
        if(eUtils.argsLength(fullCommand) < 4)
            return "null";
        String[] args = eUtils.parseScriptArgsServer(fullCommand);
        String tk = args[1];
        double n1 = Double.parseDouble(args[2]);
        double n2 = Double.parseDouble(args[3]);
        cServerVars.instance().put(tk, Double.toString(n1+n2));
        return cServerVars.instance().get(tk);
    }
}
