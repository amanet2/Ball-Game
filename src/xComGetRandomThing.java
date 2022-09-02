import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class xComGetRandomThing extends xCom {
    // usage: getrandthing $type
    public String doCommand(String fullCommand) {
        String[] args = fullCommand.split(" ");
        if(args.length < 2)
            return "null";
        for(int i = 1; i < args.length; i++) {
            if(args[i].startsWith("$")) {
                if(cServerVars.instance().contains(args[i].substring(1)))
                    args[i] = cServerVars.instance().get(args[i].substring(1));
                else if(sVars.get(args[i]) != null)
                    args[i] = sVars.get(args[i]);
            }
        }
        String type = args[1];
        if(!cServerLogic.scene.objectMaps.containsKey(type) || cServerLogic.scene.objectMaps.get(type).size() < 1)
            return "null";
        List<String> keysAsArray = new ArrayList<>(cServerLogic.scene.objectMaps.get(type).keySet());
        return keysAsArray.get(ThreadLocalRandom.current().nextInt(0, keysAsArray.size()));
    }
}
