import java.util.HashMap;

public class xComClearThingMap extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if (toks.length > 1) {
            String thing_title = toks[1];
            if(eManager.currentMap.scene.objectMaps.containsKey(thing_title))
                eManager.currentMap.scene.setThingMap(thing_title, new HashMap<>());
        }
        return "usage: clearthingmap <thing_title>";
    }
}