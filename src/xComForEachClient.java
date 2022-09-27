public class xComForEachClient extends xCom {
    //usage: foreachclient $id <script to execute where $id is preloaded>
    public String doCommand(String fullCommand) {
        if(eUtils.argsLength(fullCommand) < 3)
            return "usage: foreachclient $id <script where $id is preloaded>";
        String[] args = eUtils.parseScriptArgsServer(fullCommand);
        String varname = args[1];
        for(String id : nServer.instance().masterStateMap.keys()) {
            xCon.ex(String.format("setvar %s %s", varname, id));
            String[] cargs = eUtils.parseScriptArgsServer(fullCommand);
            StringBuilder esb = new StringBuilder();
            for(int i = 2; i < cargs.length; i++) {
                esb.append(" ").append(cargs[i]);
            }
            String es = esb.substring(1);
            xCon.ex(es);
            cServerVars.instance().args.remove(varname); //why is this needed here and not in foreachthing???
        }
        return "usage: foreachclient $id <script to execute where $id is preloaded>";
    }
}
