public class xComSetVarClient extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length < 2)
            return "null";
        String tk = toks[1];
        if(toks.length < 3) {
            if (!cClientVars.instance().contains(tk))
                return "null";
            return cClientVars.instance().get(tk);
        }
        StringBuilder tvb = new StringBuilder();
        for(int i = 2; i < toks.length; i++) {
            tvb.append(" ").append(toks[i]);
        }
        String tv = tvb.substring(1);
        if(tv.charAt(0) == '$' && cClientVars.instance().contains(tv.substring(1)))
            cClientVars.instance().put(tk, cClientVars.instance().get(tv.substring(1)));
        else
            cClientVars.instance().put(tk, tv);
        return cClientVars.instance().get(tk);
    }
}
