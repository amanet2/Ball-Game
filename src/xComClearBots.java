import java.util.LinkedHashMap;

public class xComClearBots extends xCom {
    public String doCommand(String fullCommand) {
        for(String id : nServer.instance().clientIds) {
            if(id.contains("bot"))
                nServer.instance().addQuitClient(id);
            if(sSettings.IS_SERVER)
                cServerLogic.scene.objectMaps.put("THING_BOTPLAYER", new LinkedHashMap());
            if(sSettings.IS_CLIENT)
                cClientLogic.scene.objectMaps.put("THING_BOTPLAYER", new LinkedHashMap());
        }
        return "bot removed";
    }
}
