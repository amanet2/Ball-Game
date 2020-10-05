public class xComBind extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 2) {
            String key = toks[1];
            StringBuilder comm = new StringBuilder();
            for(int i = 2; i < toks.length; i++) {
                comm.append(toks[i]).append(" ");
            }
            for(int i = 0; i < iKeyboard.keyCodeSubTexts.length; i++) {
                if(iKeyboard.keyCodeSubTexts[i].equalsIgnoreCase(key)) {
                    xCon.instance().pressBinds.put(iKeyboard.keyCodeSubCodes[i], comm.substring(0,comm.length()-1));
                    return "";
                }
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
            for(int i = 0; i < iKeyboard.keyCodeSubTexts.length; i++) {
                if(iKeyboard.keyCodeSubTexts[i].equalsIgnoreCase(key)) {
                    xCon.instance().releaseBinds.put(iKeyboard.keyCodeSubCodes[i],
                            comm.substring(0,comm.length()-1));
                    return "";
                }
            }
        }
        return "cannot bindrelease ";
    }
}
