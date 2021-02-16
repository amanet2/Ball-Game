public class xComJoingame extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 1) {
            String[] comps = toks[1].split(":");
            sVars.put("joinip", comps[0]);
            if(comps.length > 1)
                sVars.put("joinport", comps[1]);
        }
        uiMenus.selectedMenu = uiMenus.MENU_MAIN;
        sSettings.net_client = true;
        sSettings.net_server = false;
        sSettings.NET_MODE = sSettings.NET_MODE_CLIENT;
        nClient.hasDisconnected = 0;
        cVars.put("quitconfirmed", "0");
        cVars.put("quitting", "0");
        cVars.put("disconnectconfirmed", "0");
        cVars.put("disconnecting", "0");
        nSend.sendMap = null;
        nVars.reset();
        cScripts.setupGame();
        return "joined game";
    }
}
