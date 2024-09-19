import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

public class gScriptFactory {
    private HashMap<String, gScript> scriptMap;

    public gScriptFactory() {
        scriptMap = new HashMap<>();
    }

    public void init() {
        initScriptSelectionDelegate(eManager.scriptFilesSelection, "scripts");
        initScriptSelectionDelegate(eManager.configFileSelection, "config");
        initScriptSelectionDelegate(eManager.mapsFileSelection, "maps");
        initScriptSelectionDelegate(eManager.prefabFileSelection, "prefabs");
//        System.out.println("SCRIPTS LOADED: " + scriptMap.toString());
    }

    private void initScriptSelectionDelegate(String[] filesSelection, String prefix) {
        for(String fileName : filesSelection) {
            String scriptMapKey = prefix + "/" + fileName;
            try (BufferedReader br = new BufferedReader(new FileReader(scriptMapKey))) {
                StringBuilder fileContents = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    if(line.trim().length() > 0)
                        fileContents.append("\n").append(line);
                }
                scriptMap.put(scriptMapKey, new gScript(scriptMapKey, fileContents.substring(1)));
            }
            catch (Exception e) {
                xMain.shellLogic.console.logException(e);
            }
        }
    }

    public gScript getScript(String id) {
        if(scriptMap.containsKey(id))
            return scriptMap.get(id);
        return null;
    }
}
