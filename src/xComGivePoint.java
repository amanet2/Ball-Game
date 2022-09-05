public class xComGivePoint extends xCom {
    public String doCommand(String fullCommand) {
        String[] args = fullCommand.split(" ");
        if(args.length < 2)
            return "usage: givepoint <playerid> <points#optional>";
        String id = args[1];
        int score = 100;
        if(args.length > 2)
            score = Integer.parseInt(args[2]);
        gScoreboard.addToScoreField(id, "score", score);
        String color = nServer.instance().masterStateMap.get(id).get("color");
        nServer.instance().addExcludingNetCmd("server",
                String.format("cl_spawnpopup %s +%d#%s", id, score, color));
        return "gave point to " + id;
    }
}
