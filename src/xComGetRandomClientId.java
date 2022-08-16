public class xComGetRandomClientId extends xCom {
    // usage: getrandclid
    public String doCommand(String fullCommand) {
        return nServer.instance().getRandomClientId();
    }
}
