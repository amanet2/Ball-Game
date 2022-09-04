public class xComSetCamera extends xCom {
    //usage: setcamera $key $val
    public String doCommand(String fullCommand) {
        if(eUtils.argsLength(fullCommand) < 2)
            return "null";
        String[] args = eUtils.parseScriptArgsAllSources(fullCommand);
        String ck = args[1];
        String sv = "null";
        if(gCamera.contains(ck))
            sv = gCamera.get(ck);
        if(args.length < 3)
            return sv;
        gCamera.put(ck, args[2]);
        return gCamera.get(ck);
    }
}
