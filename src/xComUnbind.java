public class xComUnbind extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 1) {
            String key = toks[1];
            if(key.equalsIgnoreCase("all")) {
                xCon.instance().pressBinds.clear();
                xCon.instance().releaseBinds.clear();
                return fullCommand;
            }
            Integer keycode = iKeyboard.getCodeForKey(key);
            if(keycode != null) {
                xCon.instance().pressBinds.remove(keycode);
                xCon.instance().releaseBinds.remove(keycode);
                return String.format("unbind %s ",key);
            }
        }
        return "cannot unbind ";
    }
}

