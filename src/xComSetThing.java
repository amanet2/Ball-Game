import java.util.HashMap;

public class xComSetThing extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length < 2)
            return "null";
        String ttype = toks[1];
        if(cServerLogic.scene.getThingMap(ttype) == null)
            return "null";
        HashMap<String, gThing> thingMap = cServerLogic.scene.getThingMap(ttype);
        if(toks.length < 3)
            return thingMap.toString();
        String tid = toks[2];
        if(!thingMap.containsKey(tid))
            return "null";
        gThing thing = thingMap.get(tid);
        if(toks.length < 4)
            return thing.toString();
        String tk = toks[3];
        if(toks.length < 5) {
            if(thing.get(tk) == null)
                return "null";
            return thing.get(tk);
        }
        StringBuilder tvb = new StringBuilder();
        for(int i = 4; i < toks.length; i++) {
            tvb.append(" ").append(toks[i]);
        }
        String tv = tvb.substring(1);
//        String tvr = xCon.ex("setthing " + tv);
//        if(!tvr.equals("null"))
//            tv = tvr;
        if(tv.charAt(0) == '$' && cServerVars.instance().contains(tv.substring(1)))
            thing.put(tk, cServerVars.instance().get(tv.substring(1)));
        else
            thing.put(tk, tv);
        return thing.get(tk);
    }
}
