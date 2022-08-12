public class xComSetCamera extends xCom {
    //usage: setcamera $key $val
    public String doCommand(String fullCommand) {
        String[] args = fullCommand.split(" ");
        if(args.length < 2)
            return "null";
        for(int i = 1; i < args.length; i++) {
            if(args[i].startsWith("$")) {
                if(cServerVars.instance().contains(args[i].substring(1)))
                    args[i] = cServerVars.instance().get(args[i].substring(1));
                else if(cClientVars.instance().contains(args[i].substring(1)))
                    args[i] = cClientVars.instance().get(args[i].substring(1));
                else if(sVars.get(args[i]) != null)
                    args[i] = sVars.get(args[i]);
            }
        }
        String ck = args[1];
        String sv = "null";
        if(gCamera.argSet.contains(ck))
            sv = gCamera.argSet.get(ck);
        if(args.length < 3)
            return sv;
        gCamera.argSet.put(ck, args[2]);
        return gCamera.argSet.get(ck);
    }
}
