public class xComSay extends xCom {
    public String doCommand(String fullCommand) {
        if(fullCommand.length() > 0) {
            gMessages.msgInProgress = fullCommand.substring(fullCommand.indexOf(" ")+1);
            gMessages.messageSend = true;
            if(!gMessages.optionSet
                && gMessages.enteringOptionText.length() > 0)
                gMessages.optionSet = true;
        }
        if(fullCommand.length() > 0) {
            gMessages.sayMessages.add(fullCommand.substring(fullCommand.indexOf(" ")+1));
        }
        return fullCommand;
    }
}
