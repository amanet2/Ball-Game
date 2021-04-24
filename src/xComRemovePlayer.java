import java.util.ArrayList;

public class xComRemovePlayer extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 1) {
            String id = toks[1];
            eManager.currentMap.scene.getThingMap("THING_PLAYER").remove(id);
            if(id.equals(uiInterface.uuid)) {
                cGameLogic.setUserPlayer(null);
            }
            if(id.contains("bot"))
                eManager.currentMap.scene.getThingMap("THING_BOTPLAYER").remove(id);
            if(sSettings.net_server) {
                nServer.instance().addExcludingNetCmd("server", fullCommand);
                //remove vars related to player obj
//                ArrayList<String> npvars = new ArrayList<>();
//                npvars.add("cmd");
//                npvars.add("id");
//                npvars.add("name");
//                ArrayList<String> pvars = new ArrayList<>();
//                for(String var : nServer.instance().clientArgsMap.get(id).keySet()) {
//                    if(!npvars.contains(var))
//                        pvars.add(var);
//                }
//                for(String var : pvars) {
//                    nServer.instance().clientArgsMap.get(id).remove(var);
//                }
            }
        }
        return "usage: removeplayer <id>";
    }
}
