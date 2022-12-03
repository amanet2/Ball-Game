public class xComGiveWin extends xCom {
    public String doCommand(String fullCommand) {
        String[] args = eUtils.parseScriptArgsServer(fullCommand);
        if(args.length < 2)
            return "null";
        String id = args[1];
        gScoreboard.incrementScoreFieldById(id, "wins");
        return id;
    }
}
