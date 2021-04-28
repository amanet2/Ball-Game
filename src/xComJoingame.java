public class xComJoingame extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 1) {
            String[] comps = toks[1].split(":");
            sVars.put("joinip", comps[0]);
            if(comps.length > 1)
                sVars.put("joinport", comps[1]);
        }
        sSettings.net_client = true;
        sSettings.NET_MODE = sSettings.NET_CLIENT;
        nClient.instance().setDisconnected(0);
        cVars.put("quitconfirmed", "0");
        cVars.put("quitting", "0");
        nClient.instance().sendMap = null;
        cVars.putLong("starttime", System.currentTimeMillis());
        cVars.put("canvoteskip", "");
        return "joined game";
    }
}
