public class xComUnbind extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length < 2)
            return "cannot unbind";
        String k = toks[1];
        if(k.equalsIgnoreCase("all")) {
            xCon.instance().pressBinds.clear();
            xCon.instance().releaseBinds.clear();
            return "unbound all";
        }
        Integer kc = iKeyboard.getCodeForKey(k);
        if(kc == null)
            return "cannot unbind";
        xCon.instance().pressBinds.remove(kc);
        xCon.instance().releaseBinds.remove(kc);
        return String.format("unbound %s", k);
    }
}

