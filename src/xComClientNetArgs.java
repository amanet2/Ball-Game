import java.util.HashMap;

public class xComClientNetArgs extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if (toks.length > 1) {
            String clientId = toks[1];
            HashMap<String, String> clientMap = nServer.instance().clientArgsMap.get(clientId);
            if(clientMap == null)
                return "no client argmap with id: " + clientId;
            if(toks.length > 2) {
                String clientNetVar = toks[2];
                if(toks.length > 3) {
                    String clientNetVal = toks[3];
                    clientMap.put(clientNetVar, clientNetVal);
                    return clientMap.get(clientNetVar);
                }
                else
                    return clientMap.get(clientNetVar);
            }
            else
                return clientMap.toString();

        }
        return "usage: clientnetargs <client_id> <client_var> <client_val>";
    }
}