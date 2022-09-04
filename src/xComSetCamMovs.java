public class xComSetCamMovs extends xCom {
    public String doCommand(String fullCommand) {
        if(eUtils.argsLength(fullCommand) < 5)
            return "0";
        String[] args = eUtils.parseScriptArgsAllSources(fullCommand);
        gCamera.put("mov0", args[1]);
        gCamera.put("mov1", args[2]);
        gCamera.put("mov2", args[3]);
        gCamera.put("mov3", args[4]);
        return "1";
    }
}