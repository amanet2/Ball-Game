public class xComUserPlayer extends xCom {
    public String doCommand(String fullCommand) {
        gPlayer userPlayer = gClientLogic.getUserPlayer();
        if(userPlayer == null)
            return "no user player";
        String[] toks = fullCommand.split(" ");
        if (toks.length > 2) {
            String userVar = toks[1];
            String userVal = toks[2];
            userPlayer.put(userVar, userVal);
            return userPlayer.get(userVar);
        }
        else if (toks.length > 1) {
            String userVar = toks[1];
            return userPlayer.get(userVar);
        }
        else
            return userPlayer.toString();
    }
}