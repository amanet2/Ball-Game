import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class xComClearThingMap extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if (toks.length > 1) {
            String thing_title = toks[1];
            if(eManager.currentMap.scene.objectMaps.containsKey(thing_title)) {
                eManager.currentMap.scene.setThingMap(thing_title, new HashMap<>());
                Queue<gProp> toremove = new LinkedList<>();
                for(gProp p : eManager.currentMap.scene.props()) {
                    if(p.isInt("code", gProps.getCodeForTitle(thing_title)))
                        toremove.add(p);
                }
                while (toremove.size() > 0) {
                    eManager.currentMap.scene.props().remove(toremove.remove());
                }
            }
        }
        return "usage: clearthingmap <thing_title>";
    }
}