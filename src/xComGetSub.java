public class xComGetSub extends xCom {
    public String doCommand(String fullCommand) {
        //usage: getsub $result $num1 $num2
        String[] args = fullCommand.split(" ");
        if(args.length < 4)
            return "null";
        for(int i = 1; i < args.length; i++) {
            if(args[i].startsWith("$")) {
                if(cServerVars.instance().contains(args[i].substring(1)))
                    args[i] = cServerVars.instance().get(args[i].substring(1));
                else if(sVars.get(args[i]) != null)
                    args[i] = sVars.get(args[i]);
            }
        }
        String tk = args[1];
        double n1 = Double.parseDouble(args[2]);
        double n2 = Double.parseDouble(args[3]);
        cServerVars.instance().put(tk, Double.toString(n1-n2));
        return cServerVars.instance().get(tk);
    }
}
