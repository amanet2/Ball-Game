import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class nSend {
    public static String focus_id = ""; //for when you need to operate on a response to a specific id
    static HashMap<String, String> sendMap = null;
    private static String[] constantFields = {"map", "mode", "teams", "armed", "tick", "powerups", "scoremap", "scorelimit",
//            "timeleft", "timelimit", "topscore", "state", "win", "vels", "fire", "dirs", "x", "y", "msg", "kick",
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
            if(sendDataString.toString().contains("fireweapon"))
                System.out.println(sendDataString);
            nServer.newClientIds.remove(nSend.focus_id);
            for(int i = 0; i < nServer.clientIds.size(); i++) {
                String idload2 = nServer.clientIds.get(i);
                if(nServer.clientArgsMap.get(idload2) != null)
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
//        cGameLogic.userPlayer().put("sendshot", "0");
        xCon.ex("cv_sendsafezone 0");
        return sendDataString.toString();
    }
}
