import java.awt.*;
import java.util.HashMap;

public class cServer {
    public static String getGameStateServer() {
        //ugly if else
        HashMap thingMap;
        switch(cVars.getInt("gamemode")) {
            case cGameMode.SAFE_ZONES:
                thingMap = eManager.currentMap.scene.getThingMap("PROP_SCOREPOINT");
                for(Object id : thingMap.keySet()) {
                    gProp p = (gProp) thingMap.get(id);
                    if(p.isInt("int0", 1))
                        return String.format("safezone-%s-%s", p.get("tag"), cVars.get("safezonetime"));
                }
                break;
            case cGameMode.WAYPOINTS:
                thingMap = eManager.currentMap.scene.getThingMap("PROP_SCOREPOINT");
                for(Object id : thingMap.keySet()) {
                    gProp p = (gProp) thingMap.get(id);
                    if(p.isInt("int0", 1))
                        return String.format("waypoints-%s", p.get("tag"));
                }
                break;
            case cGameMode.FLAG_MASTER:
                return cVars.get("flagmasterid");
            case cGameMode.CAPTURE_THE_FLAG:
            case cGameMode.KING_OF_FLAGS:
                StringBuilder s = new StringBuilder();
                HashMap<String, gThing> kofThingMap = eManager.currentMap.scene.getThingMap("PROP_FLAGRED");
                for(String id : kofThingMap.keySet()) {
                    s.append(String.format("%s-%s:", id, kofThingMap.get(id).get("str0")));
                }
                return String.format("kingofflags%s", s);
            case cGameMode.VIRUS:
//                //check
//                if(cVars.get("virusids").length() < 1)
//                    cGameMode.resetVirusPlayers();
//                //check if reset time
//                if(cVars.contains("virusresettime")
//                        && cVars.getLong("virusresettime") < System.currentTimeMillis()) {
//                    cGameMode.resetVirusPlayers();
//                    cVars.remove("virusresettime");
//                }
//                //check intersections
//                for(String id1 : gScene.getPlayerIds()) {
//                    for(String id2 : gScene.getPlayerIds()) {
//                        if(!id1.equals(id2)) {
//                            gPlayer p = gScene.getPlayerById(id1);
//                            gPlayer pp = gScene.getPlayerById(id2);
//                            Rectangle r = new Rectangle(p.getInt("coordx"), p.getInt("coordy"),
//                                    p.getInt("dimw"), p.getInt("dimh"));
//                            Rectangle rr = new Rectangle(pp.getInt("coordx"), pp.getInt("coordy"),
//                                    pp.getInt("dimw"), pp.getInt("dimh"));
//                            if (r.intersects(rr) && (
//                                    (p.getInt("tag") == 0 && cVars.get("virusids").contains("server"))
//                                            || (p.get("id").length() > 0 && cVars.get("virusids").contains(p.get("id")))
//                                            || (pp.get("id").length() > 0 && cVars.get("virusids").contains(pp.get("id")))
//                            )) {
//                                if(!cVars.get("virusids").contains(id1)) {
//                                    xCon.ex("say " + pp.get("name") + " infected " + p.get("name") + "!");
//                                    cVars.put("virusids", cVars.get("virusids")+id1+"-");
//                                }
//                                if(!cVars.get("virusids").contains(id2)) {
//                                    xCon.ex("say " + p.get("name") + " infected " + pp.get("name") + "!");
//                                    cVars.put("virusids", cVars.get("virusids")+id2+"-");
//                                }
//                            }
//                        }
//                    }
//                }
//                //check if thing if full, begin countdown to reset if it is
//                boolean isFull = true;
//                for(String id : gScene.getPlayerIds()) {
//                    if(!cVars.get("virusids").contains(id)) {
//                        isFull = false;
//                        break;
//                    }
//                }
//                if (isFull) {
//                    if(!cVars.contains("virusresettime")) {
//                        cVars.putLong("virusresettime",
//                                System.currentTimeMillis()+cVars.getInt("virusresetwaittime"));
//                    }
//                }
                return String.format("virus-%s", cVars.get("virusids"));
            default:
                break;
        }
        return "";
    }

    public static void processActionLoadServer(String packActions, String packName, String packId) {
        String[] actions = packActions.split("\\|");
        for(String action : actions) {
            if(action.contains("safezone") && cVars.getInt("gamemode") == cGameMode.SAFE_ZONES) {
                xCon.ex("givepoint " + packId);
            }
            if(action.contains("sendpowerup")) {
                String[] sptoks = action.replace("sendpowerup", "").split(":");
                gProp p = (gProp) eManager.currentMap.scene.getThingMap("PROP_POWERUP").get(sptoks[0]);
                p.put("int1", sptoks[1]);
                if(Integer.parseInt(p.get("int1")) < 1)
                    p.put("int0","0");
            }
        }
    }
}
