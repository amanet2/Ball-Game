import java.util.HashMap;

public class xComSetServerArgs extends xCom {
    //usage: setnargs $key $value
    public String doCommand(String fullCommand) {
        String[] args = fullCommand.split(" ");
        if(args.length < 2)
            return nServer.instance().serverVars.toString();
        for(int i = 1; i < args.length; i++) {
            if(args[i].startsWith("$")) {
                if(cServerVars.instance().contains(args[i].substring(1)))
                    args[i] = cServerVars.instance().get(args[i].substring(1));
                else if(sVars.get(args[i]) != null)
                    args[i] = sVars.get(args[i]);
            }
        }
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
