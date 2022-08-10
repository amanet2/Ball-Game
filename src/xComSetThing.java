import java.util.HashMap;

public class xComSetThing extends xCom {
    //usage: setthing $THING_TYPE $id $key $val
    public String doCommand(String fullCommand) {
        String[] args = fullCommand.split(" ");
        if(args.length < 2)
            return "null";
        for(int i = 1; i < args.length; i++) {
            if(args[i].startsWith("$")) {
                if(cServerVars.instance().contains(args[i].substring(1)))
                    args[i] = cServerVars.instance().get(args[i].substring(1));
                else if(sVars.get(args[i]) != null)
                    args[i] = sVars.get(args[i]);
            }
        }
        String ttype = args[1];
        if(cServerLogic.scene.getThingMap(ttype) == null)
            return "null";
        HashMap<String, gThing> thingMap = cServerLogic.scene.getThingMap(ttype);
        if(args.length < 3)
            return thingMap.toString();
        String tid = args[2];
        if(!thingMap.containsKey(tid))
            return "null";
        gThing thing = thingMap.get(tid);
        if(args.length < 4)
            return thing.toString();
        String tk = args[3];
        if(args.length < 5) {
            if(thing.get(tk) == null)
                return "null";
            return thing.get(tk);
        }
        StringBuilder tvb = new StringBuilder();
        for(int i = 4; i < args.length; i++) {
            tvb.append(" ").append(args[i]);
        }
        String tv = tvb.substring(1);
        thing.put(tk, tv);
        return thing.get(tk);
    }
}
