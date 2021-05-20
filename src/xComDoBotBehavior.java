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
            gPlayer botPlayer = cServerLogic.getPlayerById(botid);
            if(botPlayer == null)
                return "botid does not exist: " + botid;
            gDoableThing behavior = cBotsLogic.getBehavior(behaviorString);
            if(behavior != null)
                behavior.doItem(botPlayer);
            else
                return "botbehavior does not exist: " + botbehavior.toString();
        }
        return fullCommand;
    }
}
