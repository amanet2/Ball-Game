import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class nSend {
    static String focus_id = ""; //for when you need to operate on a response to a specific id
    static HashMap<String, String> sendMap = null;
    static String[] constantFields = {"map", "mode", "teams", "armed", "tick", "powerups", "scoremap", "scorelimit",
            "timeleft", "timelimit", "topscore", "state", "win", "vels", "dirs", "x", "y", "msg", "kick",
            "weapon", "spawnprotectionmaxtime"};
    private static List<String> constantsList = Arrays.asList(constantFields);
    static String createSendDataString() {
        StringBuilder sendDataString;
        nVars.update();
        if(sendMap != null) {
            for(String k : nVars.keySet()) {
                if((sSettings.net_server && constantsList.contains(k)) || k.equals("id") || !sendMap.containsKey(k)
                        || !sendMap.get(k).equals(nVars.get(k))) {
                    sendMap.put(k, nVars.get(k));
                }
                else {
                    sendMap.remove(k);
                }
            }
        }
        else {
            sendMap = nVars.copy();
        }
        if(sSettings.net_server) {
            nServer.clientArgsMap.put(uiInterface.uuid, nVars.copy());
            sendDataString = new StringBuilder(nVars.dump());
            for(int i = 0; i < nServer.clientIds.size(); i++) {
                String idload2 = nServer.clientIds.get(i);
                if(nServer.clientArgsMap.get(idload2) != null)
                    sendDataString.append(String.format("@%s", nServer.clientArgsMap.get(idload2).toString()));
            }
        }
        else {
            System.out.println("YOU SHOULD NOT SEE THIS");
            if(nClient.msgreceived != 0) {
                sendMap.put("netmsgrcv","");
                nClient.msgreceived = 0;
            }
            else
                sendMap.remove("netmsgrcv");

            if(nClient.sfxreceived != 0) {
                sendMap.put("netsfxrcv","");
                nClient.sfxreceived = 0;
            }
            else
                sendMap.remove("netsfxrcv");

            if(nClient.cmdreceived != 0) {
                sendMap.put("netcmdrcv","");
                nClient.cmdreceived = 0;
            }
            else
                sendMap.remove("netcmdrcv");

            sendDataString = new StringBuilder(sendMap.toString());
            cVars.put("quitconfirmed", cVars.get("quitting"));
            cVars.put("disconnectconfirmed", cVars.get("disconnecting"));
        }
        if(cGameLogic.userPlayer() != null && cGameLogic.userPlayer().contains("spawnprotectiontime"))
            sendMap.put("spawnprotected","");
        else
            sendMap.remove("spawnprotected");
        cVars.put("exploded", "1");
        cVars.put("sendsafezone", "0");
        return sendDataString.toString();
    }
}
