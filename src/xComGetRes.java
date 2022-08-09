public class xComGetRes extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length < 2)
            return "null";
        String tk = toks[1];
        if(toks.length < 3) {
            if (!cServerVars.instance().contains(tk))
                return "null";
            return cServerVars.instance().get(tk);
        }
        StringBuilder tvb = new StringBuilder();
        for(int i = 2; i < toks.length; i++) {
            tvb.append(" ").append(toks[i]);
        }
        String tv = tvb.substring(1);
        String res = xCon.ex(tv);
        cServerVars.instance().put(tk, res);
        return cServerVars.instance().get(tk);
    }
}
