import java.util.HashMap;

public class xComSetServerArgs extends xCom {
    //usage: setnargs $key $value
    public String doCommand(String fullCommand) {
        if(eUtils.argsLength(fullCommand) < 2)
            return nServer.instance().serverVars.toString();
        String[] args = eUtils.parseScriptArgsServer(fullCommand);
        HashMap<String, String> cliMap = nServer.instance().serverVars;
        String tk = args[1];
        if(args.length < 3)
            return cliMap.get(tk);
        StringBuilder tvb = new StringBuilder();
        for(int i = 2; i < args.length; i++) {
            tvb.append(" ").append(args[i]);
        }
        String tv = tvb.substring(1);
        cliMap.put(tk, tv);
        return cliMap.get(tk);
    }
}
