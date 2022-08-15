public class xComSetPlayerCoords extends xCom {
    public String doCommand(String fullCommand) {
        String[] args = fullCommand.split(" ");
        if(args.length < 4)
            return "null";
        gPlayer p = cClientLogic.getPlayerById(args[1]);
        if(p == null)
            return "null";
        p.put("coordx", args[2]);
        p.put("coordy", args[3]);
        return fullCommand;
    }
}