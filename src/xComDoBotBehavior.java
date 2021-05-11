public class xComDoBotBehavior extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 2) {
            String botid = toks[1];
            StringBuilder botbehavior = new StringBuilder();
            for(int i = 2; i < toks.length; i++) {
                botbehavior.append(" " + toks[i]);
            }
            String behaviorString = botbehavior.toString().substring(1);
            gPlayer botPlayer = eManager.getPlayerById(botid);
            if(botPlayer == null)
                return "botid does not exist: " + botid;
            if(cVars.get("winnerid").length() < 1) {
                System.out.println(botbehavior.toString());
                gDoableThing behavior = cBotsLogic.getBehavior(behaviorString);
                if(behavior != null)
                    behavior.doItem(botPlayer);
                else
                    return "botbehavior does not exist: " + botbehavior.toString();
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
