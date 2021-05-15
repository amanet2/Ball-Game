import java.util.*;

public class xComClearThingMap extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if (toks.length > 1) {
            String thing_title = toks[1];
            if(sSettings.IS_SERVER)
                clearThingMapDelegate(thing_title, cServerLogic.scene);
            if(sSettings.IS_CLIENT)
                clearThingMapDelegate(thing_title, cClientLogic.scene);
        }
        return "usage: clearthingmap <thing_title>";
    }
    
    private void clearThingMapDelegate(String thing_title, gScene scene) {
        ArrayList<String> toRemoveIds = new ArrayList<>();
        if(thing_title.contains("ITEM_")) {
            if(scene.objectMaps.containsKey(thing_title))
                toRemoveIds.addAll(scene.getThingMap(thing_title).keySet());
            for(String id : toRemoveIds) {
                scene.getThingMap("THING_ITEM").remove(id);
            }
        }
        if(scene.objectMaps.containsKey(thing_title))
            scene.objectMaps.put(thing_title, new LinkedHashMap<>());
    }
}