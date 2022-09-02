public class xComTestResLteClient extends xCom {
    //usage: testreslte $res $val <string that will exec if res <= val>
    public String doCommand(String fullCommand) {
        String[] args = fullCommand.split(" ");
        if(args.length < 3)
            return "0";
        for(int i = 1; i < args.length; i++) {
            if(args[i].startsWith("$")) {
                if(cClientVars.instance().contains(args[i].substring(1)))
                    args[i] = cClientVars.instance().get(args[i].substring(1));
                else if(sVars.get(args[i]) != null)
                    args[i] = sVars.get(args[i]);
            }
        }
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
