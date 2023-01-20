public class xComJoingame extends xCom {
    public String doCommand(String fullCommand) {
        nClient.instance().reset();
        sSettings.IS_CLIENT = true;
//        nClient.instance().start();
        return "joined game";
    }
}