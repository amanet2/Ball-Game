public class xComTestResLte extends xCom {
    //usage: testreslte $res $val <string that will exec if res <= val>
    public String doCommand(String fullCommand) {
        if(eUtils.argsLength(fullCommand) < 3)
            return "0";
        String[] args = eUtils.parseScriptArgsServer(fullCommand);
        String tk = args[1];
        String tv = args[2];
        StringBuilder esb = new StringBuilder();
        for(int i = 3; i < args.length; i++) {
            esb.append(" ").append(args[i]);
        }
        String es = esb.substring(1);
        if(Long.parseLong(tk) <= Long.parseLong(tv)) {
            xCon.ex(es);
            return "1";
        }
        return "0";
    }
}
