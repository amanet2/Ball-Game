public class xComGetAddLongClient extends xCom {
    public String doCommand(String fullCommand) {
        //usage: getadd $result $num1 $num2
        if(eUtils.argsLength(fullCommand) < 4)
            return "null";
        String[] args = eUtils.parseScriptArgsClient(fullCommand);
        String tk = args[1];
        long n1 = Long.parseLong(args[2]);
        long n2 = Long.parseLong(args[3]);
        cClientVars.instance().put(tk, Long.toString(n1+n2));
        return cClientVars.instance().get(tk);
    }
}
