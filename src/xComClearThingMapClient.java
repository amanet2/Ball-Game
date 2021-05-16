import java.util.ArrayList;
import java.util.LinkedHashMap;

public class xComClearThingMapClient extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if (toks.length > 1) {
            String thing_title = toks[1];
            ArrayList<String> toRemoveIds = new ArrayList<>();
            if(thing_title.contains("ITEM_")) {
                if(cClientLogic.scene.objectMaps.containsKey(thing_title))
                    toRemoveIds.addAll(cClientLogic.scene.getThingMap(thing_title).keySet());
                for(String id : toRemoveIds) {
                    cClientLogic.scene.getThingMap("THING_ITEM").remove(id);
                }
            }
            if(cClientLogic.scene.objectMaps.containsKey(thing_title))
                cClientLogic.scene.objectMaps.put(thing_title, new LinkedHashMap<>());
        }
        return "usage: clearthingmap <thing_title>";
    }
}