import java.util.HashMap;

public class xComGetServerArgs extends xCom {
    //usage: getserverargs $id $varkey
    public String doCommand(String fullCommand) {
        HashMap<String, String> cliMap = nServer.instance().serverVars;
        if(eUtils.argsLength(fullCommand) < 2)
            return cliMap.toString();
        String[] args = eUtils.parseScriptArgsServer(fullCommand);
        StringBuilder tvb = new StringBuilder();
        for(int i = 1; i < args.length; i++) {
            tvb.append(" ").append(args[i]);
        }
        String tv = tvb.substring(1);
        if(cliMap.containsKey(tv))
            return cliMap.get(tv);
        return "null";
    }
}
