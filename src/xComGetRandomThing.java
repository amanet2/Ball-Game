import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class xComGetRandomThing extends xCom {
    // usage: getrandthing $type
    public String doCommand(String fullCommand) {
        if(eUtils.argsLength(fullCommand) < 2)
            return "null";
        String[] args = eUtils.parseScriptArgsServer(fullCommand);
        String type = args[1];
        if(!cServerLogic.scene.objectMaps.containsKey(type) || cServerLogic.scene.objectMaps.get(type).size() < 1)
            return "null";
        List<String> keysAsArray = new ArrayList<>(cServerLogic.scene.objectMaps.get(type).keySet());
        return keysAsArray.get(ThreadLocalRandom.current().nextInt(0, keysAsArray.size()));
    }
}
