public class xComUnbind extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 1) {
            String key = toks[1];
            if(key.toLowerCase().equals("all")) {
                xCon.instance().pressBinds.clear();
                xCon.instance().releaseBinds.clear();
                return fullCommand;
            }
            for(int i = 0; i < iKeyboard.keyCodeSubTexts.length; i++) {
                if(iKeyboard.keyCodeSubTexts[i].equalsIgnoreCase(key)) {
                    xCon.instance().pressBinds.remove(iKeyboard.keyCodeSubCodes[i]);
                    xCon.instance().releaseBinds.remove(iKeyboard.keyCodeSubCodes[i]);
                    return String.format("unbind %s ",key);
                }
            }
        }
        return "cannot unbind ";
    }
}

