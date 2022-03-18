public class xComDebug extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 1) {
            int mode = Integer.parseInt(toks[1]);
            sSettings.debug = mode > 0;
        }
        return "usage: debug <int>";
    }
}

