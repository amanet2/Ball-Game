import java.util.ArrayList;

public class xComGetRandomClientId extends xCom {
    // usage: getrandclid
    public String doCommand(String fullCommand) {
        if(nServer.instance().masterStateMap.keys().size() < 1)
            return "null";
        int randomClientIndex = (int) (Math.random() * nServer.instance().masterStateMap.keys().size());
        ArrayList<String> clientIds = new ArrayList<>(nServer.instance().masterStateMap.keys());
        return clientIds.get(randomClientIndex);
    }
}
