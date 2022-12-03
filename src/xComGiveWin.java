public class xComGiveWin extends xCom {
    public String doCommand(String fullCommand) {
        String[] args = eUtils.parseScriptArgsServer(fullCommand);
        if(args.length < 2)
            return "null";
        String id = args[1];
        gScoreboard.incrementScoreFieldById(id, "wins");
        nServer.instance().addExcludingNetCmd("server", String.format("cl_spawnpopup %s WINNER#%s",
                id, nServer.instance().masterStateMap.get(id).get("color")));
        return id;
    }
}
