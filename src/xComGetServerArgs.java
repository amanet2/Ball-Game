import java.util.HashMap;

public class xComGetServerArgs extends xCom {
    //usage: getserverargs $id $varkey
    public String doCommand(String fullCommand) {
        String[] args = fullCommand.split(" ");
        HashMap<String, String> cliMap = nServer.instance().serverVars;
        if(args.length < 2)
            return cliMap.toString();
        for(int i = 1; i < args.length; i++) {
            if(args[i].startsWith("$")) {
                if(cServerVars.instance().contains(args[i].substring(1)))
                    args[i] = cServerVars.instance().get(args[i].substring(1));
                else if(sVars.get(args[i]) != null)
                    args[i] = sVars.get(args[i]);
            }
        }
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
