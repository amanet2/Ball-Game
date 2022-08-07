import java.util.HashMap;

public class xComSetThing extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length < 4)
            return "null";
        String ttype = toks[1];
        String tid = toks[2];
        String tk = toks[3];
        if(cServerLogic.scene.getThingMap(ttype) == null)
            return "null";
        HashMap<String, gThing> thingMap = cServerLogic.scene.getThingMap(ttype);
        if(!thingMap.containsKey(tid))
            return "null";
        gThing thing = thingMap.get(tid);
        if(toks.length > 4)
            thing.put(tk, toks[4]);
        if(thing.get(tk) == null)
            return "null";
        return thing.get(tk);
    }
}
