public class xComBind extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 2) {
            String key = toks[1];
            StringBuilder comm = new StringBuilder();
            for(int i = 2; i < toks.length; i++) {
                comm.append(toks[i]).append(" ");
            }
            Integer keycode = iKeyboard.getCodeForKey(key);
            if(keycode != null) {
                xCon.instance().pressBinds.put(keycode, comm.substring(0,comm.length()-1));
                return "";
            }
        }
        return "cannot bindpress ";
    }

    public String undoCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 2) {
            String key = toks[1];
            StringBuilder comm = new StringBuilder();
            for(int i = 2; i < toks.length; i++) {
                comm.append(toks[i]).append(" ");
            }
            Integer keycode = iKeyboard.getCodeForKey(key);
            if(keycode != null) {
                    xCon.instance().releaseBinds.put(keycode, comm.substring(0,comm.length()-1));
                    return "";
            }
        }
        return "cannot bindrelease ";
    }
}
