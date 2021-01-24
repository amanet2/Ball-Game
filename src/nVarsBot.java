import java.util.HashMap;

public class nVarsBot {
    private static HashMap<String, HashMap<String, String>> map = null;

    public static HashMap<String, String> copyArgsForId(String id) {
        return new HashMap<>(nVarsBot.map.get(id));
    }

    //for multiple bots
    public static String dumpArgsForId(String id) {
        return map.get(id).toString();
    }

    public static void update(gPlayer p) {
        refreshForId(p.get("id"));
        if(p.getLong("botthinktime") < uiInterface.gameTime) {
            xCon.ex("dobotbehavior " + p.get("id") + " "+cVars.get("botbehavior"));
            int rd = (int)(Math.random()*cVars.getInt("botthinkdelay")-cVars.getInt("botthinkdelay")/2);
            p.putLong("botthinktime", System.currentTimeMillis() + cVars.getInt("botthinkdelay") + rd);
        }

        for(String s : new String[]{"id","fv","name","color","crouch","hat"}) {
            map.get(p.get("id")).put(s, p.get(s));
        }
        if(p.getInt("vel1") > 0 && p.getInt("vel3") > 0)
            map.get(p.get("id")).put("fv", Double.toString(3*Math.PI/4));
        else if(p.getInt("vel1") > 0 && p.getInt("vel2") > 0)
            map.get(p.get("id")).put("fv", Double.toString(3*Math.PI/2));
        else if(p.getInt("vel1") > 0)
            map.get(p.get("id")).put("fv", Double.toString(Math.PI));
        else
            map.get(p.get("id")).put("fv", Double.toString(2*Math.PI));
        map.get(p.get("id")).put("x", p.get("coordx"));
        map.get(p.get("id")).put("y", p.get("coordy"));
//        map.get(p.get("id")).put("fire", p.get("firing"));
        map.get(p.get("id")).put("dirs", "1111");
        map.get(p.get("id")).put("vels",
                String.format("%s-%s-%s-%s", p.get("vel0"), p.get("vel1"), p.get("vel2"), p.get("vel3")));
        map.get(p.get("id")).put("weapon", p.get("weapon"));
        map.get(p.get("id")).put("netmsgrcv", "");
        map.get(p.get("id")).put("time", Long.toString(System.currentTimeMillis()));
        map.get(p.get("id")).put("fire", p.get("sendshot").equals("1")? "1" : "0");
        if(p.contains("spawnprotectiontime"))
            map.get(p.get("id")).put("spawnprotected","");
        else
            map.get(p.get("id")).remove("spawnprotected");
    }

    private static void refreshForId(String id) {
        if(map == null)
            map = new HashMap<>();
        if(!map.containsKey(id))
            map.put(id, new HashMap<>());
    }
}
