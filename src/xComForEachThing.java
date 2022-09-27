public class xComForEachThing extends xCom {
    //usage: foreachthing $var $THING_TYPE <script to execute where $var is preloaded>
    public String doCommand(String fullCommand) {
        if(eUtils.argsLength(fullCommand) < 4)
            return "usage: foreach $var $THING_TYPE <script where $var is preloaded>";
        String[] args = eUtils.parseScriptArgsServer(fullCommand);
        gScene scene = cServerLogic.scene;
        String varname = args[1];
        String thingtype = args[2];
        if(!scene.objectMaps.containsKey(thingtype))
            return "no thing type in scene: " + thingtype;
        for(String id : scene.getThingMapIds(thingtype)) {
            xCon.ex(String.format("setvar %s %s", varname, id));
            String[] cargs = eUtils.parseScriptArgsServer(fullCommand);
            StringBuilder esb = new StringBuilder();
            for(int i = 3; i < cargs.length; i++) {
                esb.append(" ").append(cargs[i]);
            }
            String es = esb.substring(1);
            xCon.ex(es);
        }
        return "usage: foreach $var $THING_TYPE <script to execute where $var is preloaded>";
    }
}
