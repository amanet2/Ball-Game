public class xComChat extends xCom {
    public String doCommand(String fullCommand) {
        String[] args = fullCommand.split(" ");
        gMessages.enteringMessage = true;
        if(args.length > 1) {
            StringBuilder sb = new StringBuilder();
            for(int i = 1; i < args.length; i++) {
                sb.append(" ").append(args[i]);
            }
            gMessages.prompt = sb.substring(1);
        }
        else if(!gMessages.prompt.equals("SAY"))
            gMessages.prompt = "SAY";
        return fullCommand;
    }
}
