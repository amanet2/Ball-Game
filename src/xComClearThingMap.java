import java.util.*;

public class xComClearThingMap extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if (toks.length > 1) {
            String thing_title = toks[1];
            if(sSettings.IS_SERVER) {
                ArrayList<String> toRemoveIds = new ArrayList<>();
                if(thing_title.contains("ITEM_")) {
                    if(cServerLogic.scene.objectMaps.containsKey(thing_title))
                        toRemoveIds.addAll(cServerLogic.scene.getThingMap(thing_title).keySet());
                    for(String id : toRemoveIds) {
                        cServerLogic.scene.getThingMap("THING_ITEM").remove(id);
                    }
                }
                if(cServerLogic.scene.objectMaps.containsKey(thing_title))
                    cServerLogic.scene.objectMaps.put(thing_title, new LinkedHashMap<>());
            }
            if(sSettings.IS_CLIENT) {
                ArrayList<String> toRemoveIds = new ArrayList<>();
                if(thing_title.contains("ITEM_")) {
                    if(cClientLogic.scene.objectMaps.containsKey(thing_title))
                        toRemoveIds.addAll(cClientLogic.scene.getThingMap(thing_title).keySet());
                    for(String id : toRemoveIds) {
                        cClientLogic.scene.getThingMap("THING_ITEM").remove(id);
                    }
                }
                if(cClientLogic.scene.objectMaps.containsKey(thing_title)) {
                    cClientLogic.scene.objectMaps.put(thing_title, new LinkedHashMap<>());
                    if(thing_title.equals("THING_PLAYER"))
                        cClientLogic.setUserPlayer(null);
                }
            }
        }
        return "usage: clearthingmap <thing_title>";
    }
}