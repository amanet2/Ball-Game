public class xComSetCamCoords extends xCom {
    public String doCommand(String fullCommand) {
        if(eUtils.argsLength(fullCommand) < 3)
            return "0";
        String[] args = eUtils.parseScriptArgsAllSources(fullCommand);
        gCamera.put("coordx", args[1]);
        gCamera.put("coordy", args[2]);
        return "1";
    }
}