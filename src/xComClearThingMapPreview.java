import java.util.ArrayList;
import java.util.LinkedHashMap;

public class xComClearThingMapPreview extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if (toks.length > 1) {
            String thing_title = toks[1];
            ArrayList<String> toRemoveIds = new ArrayList<>();
            if(uiEditorMenus.previewScene.objectMaps.containsKey(thing_title))
                uiEditorMenus.previewScene.objectMaps.put(thing_title, new LinkedHashMap<>());
        }
        for(String thing_title : uiEditorMenus.previewScene.objectMaps.keySet()) {
            uiEditorMenus.previewScene.objectMaps.get(thing_title).clear();
        }
        return "usage: cl_clearthingmappreview";
    }
}