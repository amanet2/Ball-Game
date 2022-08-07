import java.util.HashMap;

public class xComSetThing extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        gScene scene;
        if(toks.length < 2)
            return "null";
        String tenv = toks[1];
        if(tenv.equalsIgnoreCase("server"))
            scene = cServerLogic.scene;
        else if(tenv.equalsIgnoreCase("client"))
            scene = cClientLogic.scene;
        else
            return "null";
        if(toks.length < 3)
            return "null";
        String ttype = toks[2];
        if(scene.getThingMap(ttype) == null)
            return "null";
        HashMap<String, gThing> thingMap = scene.getThingMap(ttype);
        if(toks.length < 4)
            return thingMap.toString();
        String tid = toks[3];
        if(!thingMap.containsKey(tid))
            return "null";
        gThing thing = thingMap.get(tid);
        if(toks.length < 5)
            return thing.toString();
        String tk = toks[4];
        if(thing.get(tk) == null)
            return "null";
        if(toks.length < 6)
            return thing.get(tk);
        StringBuilder tvb = new StringBuilder();
        for(int i = 5; i < toks.length; i++) {
            tvb.append(" ").append(toks[i]);
        }
        String tv = tvb.substring(1);
        String tvr = xCon.ex("setthing " + tv);
        if(!tvr.equals("null"))
            thing.put(tk, tvr);
        else
            thing.put(tk, tv);
        return thing.get(tk);
    }
}
