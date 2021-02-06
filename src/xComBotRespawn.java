import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class xComBotRespawn extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        int botIndex = 0;
        if(toks.length > 1)
            botIndex = Integer.parseInt(toks[1]);
        int tries = 0;
        while(tries < 100) {
            tries++;
            int randomSpawnpointIndex = ThreadLocalRandom.current().nextInt(0,
                    eManager.currentMap.scene.getThingMap("PROP_SPAWNPOINT").size());
            ArrayList<String> spawnpointids =
                    new ArrayList<>(eManager.currentMap.scene.getThingMap("PROP_SPAWNPOINT").keySet());
            String randomId = spawnpointids.get(randomSpawnpointIndex);
            gPropSpawnpoint spawnpoint =
                    (gPropSpawnpoint) eManager.currentMap.scene.getThingMap("PROP_SPAWNPOINT").get(randomId);

            gPlayer bot = null;
            HashMap botMap = eManager.currentMap.scene.getThingMap("THING_BOTPLAYER");
            for(Object id : botMap.keySet()) {
                gPlayer tbot = (gPlayer) botMap.get(id);
                if(tbot.isInt("bottag", botIndex))
                    bot = tbot;
            }
            boolean pass = true;
            for (gPlayer target : eManager.currentMap.scene.players()) {
                if (target.getInt("tag") != bot.getInt("tag") &&
                        bot.willCollideWithPlayerAtCoords(target, spawnpoint.getInt("coordx"),
                                spawnpoint.getInt("coordy"))) {
                    pass = false;
                    break;
                }
            }
            if(pass) {
                bot.putInt("coordx", spawnpoint.getInt("coordx") - bot.getInt("dimw") / 2);
                bot.putInt("coordy", spawnpoint.getInt("coordy") - bot.getInt("dimh") / 2);
                bot.put("stockhp", cVars.get("maxstockhp"));
                if(cVars.contains("spawnprotectionmaxtime") && cVars.getInt("spawnprotectionmaxtime") > 0) {
                    bot.putLong("spawnprotectiontime",
                            System.currentTimeMillis() + cVars.getInt("spawnprotectionmaxtime"));
                }
                break;
            }
        }
        return "respawned";
    }
}
