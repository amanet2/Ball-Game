public class xComGetRes extends xCom {
    public String doCommand(String fullCommand) {
        String[] args = fullCommand.split(" ");
        if(args.length < 2)
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
