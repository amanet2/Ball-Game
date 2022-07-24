public class xComJoingame extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 1) {
            String[] comps = toks[1].split(":");
            cClientLogic.joinip = comps[0];
            if(comps.length > 1)
                cClientLogic.joinport = Integer.parseInt(comps[1]);
        }
        nClient.instance().reset();
//        nClient.instance().start();
        sSettings.IS_CLIENT = true;
        return "joined game";
    }
}
