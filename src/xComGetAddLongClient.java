public class xComGetAddLongClient extends xCom {
    public String doCommand(String fullCommand) {
        //usage: getadd $result $num1 $num2
        String[] args = fullCommand.split(" ");
        if(args.length < 4)
            return "null";
        for(int i = 1; i < args.length; i++) {
            if(args[i].startsWith("$")) {
                if(cClientVars.instance().contains(args[i].substring(1)))
                    args[i] = cClientVars.instance().get(args[i].substring(1));
                else if(sVars.get(args[i]) != null)
                    args[i] = sVars.get(args[i]);
            }
        }
        String tk = args[1];
        long n1 = Long.parseLong(args[2]);
        long n2 = Long.parseLong(args[3]);
        cClientVars.instance().put(tk, Long.toString(n1+n2));
        return cClientVars.instance().get(tk);
    }
}
