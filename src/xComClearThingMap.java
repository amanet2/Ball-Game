import java.util.*;

public class xComClearThingMap extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if (toks.length > 1) {
            String thing_title = toks[1];
            ArrayList<String> toRemoveIds = new ArrayList<>();
            if(thing_title.contains("ITEM_")) {
                if(eManager.currentMap.scene.objectMaps.containsKey(thing_title)) {
                    toRemoveIds.addAll(eManager.currentMap.scene.getThingMap(thing_title).keySet());
                }
                for(String id : toRemoveIds) {
                    eManager.currentMap.scene.getThingMap("THING_ITEM").remove(id);
                }
            }
            if(eManager.currentMap.scene.objectMaps.containsKey(thing_title)) {
                eManager.currentMap.scene.objectMaps.put(thing_title, new LinkedHashMap<>());
                if(thing_title.equals("THING_PLAYER"))
                    cGameLogic.setUserPlayer(null);
            }
        }
        return "usage: clearthingmap <thing_title>";
    }
}