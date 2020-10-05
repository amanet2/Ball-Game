public class xComClientlist extends xCom {
    public String doCommand(String fullCommand) {
        StringBuilder s = new StringBuilder("");
        for(String k : nServer.clientArgsMap.keySet()) {
            s.append(String.format("%s/%s,", nServer.clientArgsMap.get(k).get("name"), k));
        }
        return s.substring(0, s.length()-1);
    }
}