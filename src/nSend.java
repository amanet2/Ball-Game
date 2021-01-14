import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class nSend {
    public static String focus_id = ""; //for when you need to operate on a response to a specific id
    static HashMap<String, String> sendMap = null;
    private static String[] constantFields = {"map", "mode", "teams", "armed", "tick", "powerups", "scores", "scorelimit",
            "timeleft", "timelimit", "topscore", "state", "win", "vels", "fire", "dirs", "x", "y", "msg", "kick",
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
            if(nSend.focus_id.length() > 0 && nServer.clientIds.contains(nSend.focus_id)
                    && nServer.newClientIds.contains(nSend.focus_id)) {
                sendDataString = new StringBuilder(nVars.dump());
                nServer.newClientIds.remove(nSend.focus_id);
            }
            else {
                sendDataString = new StringBuilder(nVars.dump());
            }
            for(int i = 0; i < nServer.clientIds.size(); i++) {
                String idload2 = nServer.clientIds.get(i);
                sendDataString.append(String.format("@%s", nServer.clientArgsMap.get(idload2).toString()));
            }
        }
        else {
            if(nClient.msgreceived != 0) {
                sendMap.put("netmsgrcv","");
                nClient.msgreceived = 0;
            }
            else {
                sendMap.remove("netmsgrcv");
            }

            if(nClient.sfxreceived != 0) {
                sendMap.put("netsfxrcv","");
                nClient.sfxreceived = 0;
            }
            else {
                sendMap.remove("netsfxrcv");
            }

            if(nClient.cmdreceived != 0) {
                sendMap.put("netcmdrcv","");
                nClient.cmdreceived = 0;
            }
            else {
                sendMap.remove("netcmdrcv");
            }

            sendDataString = new StringBuilder(sendMap.toString());
            xCon.ex("cv_quitconfirmed cv_quitting");
            xCon.ex("cv_disconnectconfirmed cv_disconnecting");
        }
        if(cVars.contains("spawnprotectiontime")) {
            sendMap.put("spawnprotected","");
        }
        else {
            sendMap.remove("spawnprotected");
        }
        xCon.ex("cv_reportedkiller cv_exploded");
        xCon.ex("cv_exploded 1");
        xCon.ex("THING_PLAYER.0.sendshot 0");
        xCon.ex("cv_lapcomplete 0");
        xCon.ex("cv_sendsafezone 0");
        return sendDataString.toString();
    }
}
