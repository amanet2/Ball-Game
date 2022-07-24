import java.util.HashMap;

public class nVarsBot {
    static int botthinkdelay = 500;
    private static HashMap<String, HashMap<String, String>> map = null;

    public static HashMap<String, String> copyArgsForId(String id) {
        return new HashMap<>(nVarsBot.map.get(id));
    }

    //for multiple bots
    public static String dumpArgsForId(String id) {
        return map.get(id).toString();
    }

    public static void update(gPlayer p, long gameTimeMillis) {
        refreshForId(p.get("id"));
        if(p.getLong("botthinktime") < gameTimeMillis) {
            xCon.ex("dobotbehavior " + p.get("id") + " "+ cGameLogic.net_gamemode_texts[cClientLogic.gamemode]);
            int rd = (int)(Math.random()*botthinkdelay-botthinkdelay/2);
            p.putLong("botthinktime", System.currentTimeMillis() + botthinkdelay + rd);
        }

        for(String s : new String[]{"id","fv"}) {
            map.get(p.get("id")).put(s, p.get(s));
        }
        map.get(p.get("id")).put("x", p.get("coordx"));
        map.get(p.get("id")).put("y", p.get("coordy"));
        map.get(p.get("id")).put("vels",
                String.format("%s-%s-%s-%s", p.get("botvel0"), p.get("botvel1"), p.get("botvel2"), p.get("botvel3")));
        map.get(p.get("id")).put("netmsgrcv", "");
    }

    private static void refreshForId(String id) {
        if(map == null)
            map = new HashMap<>();
        if(!map.containsKey(id))
            map.put(id, new HashMap<>());
    }
}
