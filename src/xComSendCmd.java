public class xComSendCmd extends xCom {
    public String doCommand(String fullCommand) {
        if(sSettings.net_server) {
            String[] toks = fullCommand.split(" ");
            if(toks.length > 1) {
                String cmd = fullCommand.replace(toks[0]+" ", "");
                try {
                    int id = 0;
                    if(!toks[1].equalsIgnoreCase("server"))
                        id = Integer.parseInt(toks[1]); //check if this is a valid int by triggering exception if not
                    nServer.addSendCmd(toks[1], cmd);
                }
                catch (Exception e) {
                    nServer.addSendCmd(cmd);
                }
            }
        }
        return fullCommand;
    }
}