import java.util.HashMap;

public class cGameLogic {

    /**
     * executed at every game tick
     */
    public static void customLoop() {
        try {
//            checkHatStatus();
//            checkColorStatus();
//            cScripts.checkBulletSplashes();
        }
        catch(Exception e) {
            eUtils.echoException(e);
            e.printStackTrace();
        }
    }

    public static void resetGameState() {
        cScoreboard.resetScoresMap();
        cVars.putLong("starttime", System.currentTimeMillis());
        cVars.put("gamewon", "0");
        cVars.put("winnerid","");
        switch (cVars.getInt("gamemode")) {
            case cGameMode.VIRUS:
                cGameMode.resetVirusPlayers();
                break;
            default:
                break;
        }
    }



    public static void checkHatStatus(){
        //player0
        gPlayer userPlayer = gClientLogic.getUserPlayer();
        if(userPlayer != null && !userPlayer.get("pathspritehat").contains(sVars.get("playerhat"))) {
            userPlayer.setHatSpriteFromPath(eUtils.getPath(String.format("animations/hats/%s/a.png",
                    sVars.get("playerhat"))));
        }
        //others
        for(String id : nServer.instance().clientArgsMap.keySet()) {
            gPlayer p = gScene.getPlayerById(id);
            String chat = nServer.instance().clientArgsMap.get(id).get("hat");
            if(p == null || chat == null)
                continue;
            if(!p.get("pathspritehat").contains(chat)) {
                p.setHatSpriteFromPath(eUtils.getPath(String.format("animations/hats/%s/a.png",chat)));
                p.put("hat", chat);
            }
        }
    }

    public static void checkColorStatus(){
        //check all id colors, including yours
        for(String id : nServer.instance().clientArgsMap.keySet()) {
            gPlayer p = gScene.getPlayerById(id);
            String ccol = nServer.instance().clientArgsMap.get(id).get("color");
            if(p == null || ccol == null)
                continue;
            if(!p.get("pathsprite").contains(ccol)) {
                p.setSpriteFromPath(eUtils.getPath(String.format("animations/player_%s/%s", ccol,
                        p.get("pathsprite").substring(p.get("pathsprite").lastIndexOf('/')))));
            }
        }
    }

    public static void checkWeaponsStatus() {
        //other players
        for(String id : nServer.instance().clientArgsMap.keySet()) {
            if(!id.equals(uiInterface.uuid)) {
                gPlayer p = gScene.getPlayerById(id);
                int cweap = Integer.parseInt(nServer.instance().clientArgsMap.get(id).get("weapon"));
                if(!p.isInt("weapon", cweap))
                    p.putInt("weapon", cweap);
            }
        }
    }

    public static void doCommand(String cmd) {
        nClient.instance().addNetCmd(cmd);
    }

    public static void rechargePlayersHealth() {
        HashMap playersMap = eManager.currentMap.scene.getThingMap("THING_PLAYER");
        for(Object id : playersMap.keySet()) {
            gPlayer p = (gPlayer) playersMap.get(id);
            if(p.getInt("stockhp") < cVars.getInt("maxstockhp") &&
                    p.getLong("hprechargetime")+cVars.getInt("delayhp") < System.currentTimeMillis()) {
                if(p.getInt("stockhp")+cVars.getInt("rechargehp") > cVars.getInt("maxstockhp"))
                    p.put("stockhp", cVars.get("maxstockhp"));
                else
                    p.putInt("stockhp", p.getInt("stockhp") + cVars.getInt("rechargehp"));
            }
        }
    }

    public static void checkHealthStatus() {
        HashMap<String, HashMap<String, String>> argsMap = nServer.instance().clientArgsMap;
        Long currentTime = System.currentTimeMillis();
        for(String id : argsMap.keySet()) {
            if(!id.equals("server") && argsMap.get(id).containsKey("respawntime")
            && Long.parseLong(argsMap.get(id).get("respawntime")) < currentTime) {
                nServer.instance().addNetCmd("respawnnetplayer " + id);
                argsMap.get(id).remove("respawntime");
            }
        }
        rechargePlayersHealth();
    }


}
