public class xComDoBotBehavior extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 2) {
            String botid = toks[1];
            String botbehavior = toks[2];
            gPlayer botPlayer = gScene.getPlayerById(botid);
            if(botPlayer == null)
                return "botid does not exist: " + botid;
            if(cVars.get("winnerid").length() < 1) {
                gDoableThing behavior = cBotsLogic.getBehavior(botbehavior);
                if(behavior != null)
                    behavior.doItem(botPlayer);
                else
                    return "botbehavior does not exist: " + botbehavior;
            }
            else {
                botPlayer.put("mov0", "0");
                botPlayer.put("mov1", "0");
                botPlayer.put("mov2", "0");
                botPlayer.put("mov3", "0");
                botPlayer.put("vel0", "0");
                botPlayer.put("vel1", "0");
                botPlayer.put("vel2", "0");
                botPlayer.put("vel3", "0");
            }
        }
        return fullCommand;
    }
}
