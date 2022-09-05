public class xComGetAddClient extends xCom {
    public String doCommand(String fullCommand) {
        //usage: cl_getadd $result $num1 $num2
        if(eUtils.argsLength(fullCommand) < 4)
            return "null";
        String[] args = eUtils.parseScriptArgsClient(fullCommand);
        String tk = args[1];
        double n1 = Double.parseDouble(args[2]);
        double n2 = Double.parseDouble(args[3]);
        String s = Double.toString(n1+n2);
        String ss = s.substring(0, s.indexOf('.')+2);
        cClientVars.instance().put(tk, ss);
        return ss;
    }
}
