public class xComClientPlayer extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if (toks.length > 1) {
            String clientId = toks[1];
            gPlayer clientPlayer = gScene.getPlayerById(clientId);
            if(clientPlayer == null)
                return "no player with id: " + clientId;
            if(toks.length > 2) {
                String clientPlayerVar = toks[2];
                if(toks.length > 3) {
                    String clientPlayerVal = toks[3];
                    clientPlayer.put(clientPlayerVar, clientPlayerVal);
                    return clientPlayer.get(clientPlayerVar);
                }
                else
                    return clientPlayer.get(clientPlayerVar);
            }
            else
                return clientPlayer.toString();

        }
        return "usage: clientplayer <player_id> <player_var> <player_val>";
    }
}