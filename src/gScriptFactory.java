import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

public class gScriptFactory {
    HashMap<String, gScript> scriptMap;
    private static gScriptFactory instance = null;

    private gScriptFactory() {
        scriptMap = new HashMap<>();
    }

    public void init() {
        for(String fileName : eManager.scriptFilesSelection) {
            String scriptMapKey = "scripts/" + fileName;
            try (BufferedReader br = new BufferedReader(new FileReader(scriptMapKey))) {
                StringBuilder fileContents = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    if(line.trim().length() > 0 && line.trim().charAt(0) != '#')
                        fileContents.append("\n").append(line);
                }
                scriptMap.put(scriptMapKey, new gScript(scriptMapKey, fileContents.substring(1)));
            }
            catch (Exception e) {
                eLogging.logException(e);
                e.printStackTrace();
            }
        }
//        System.out.println(scriptMap.toString());
    }

    public gScript getScript(String id) {
        if(scriptMap.containsKey(id))
            return scriptMap.get(id);
        return null;
    }

    public static gScriptFactory instance() {
        if(instance == null)
            instance = new gScriptFactory();
        return instance;
    }
}
