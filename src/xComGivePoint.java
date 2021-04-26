import java.util.Arrays;
import java.util.List;

public class xComGivePoint extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if (toks.length > 1) {
            String id = toks[1];
            cScoreboard.incrementScoreFieldById(id, "score");
            gPlayer givePointPlayer = gScene.getPlayerById(id);
            if(givePointPlayer != null) {
                nServer.instance().addNetCmd("spawnpopup " + givePointPlayer.get("id") + " 1");
            }
            String[] quietGameModesArray = {
                    Integer.toString(cGameMode.KING_OF_FLAGS),
                    Integer.toString(cGameMode.VIRUS),
                    Integer.toString(cGameMode.FLAG_MASTER),
                    Integer.toString(cGameMode.DEATHMATCH)
            };
            List<String> quietGameModesList = Arrays.asList(quietGameModesArray);
            if(!quietGameModesList.contains(cVars.get("gamemode"))) {
                nServer.instance().addNetCmd("echo " + nServer.instance().clientArgsMap.get(id).get("name") + " scored");
            }
            return "gave point to " + id;
        }
        return "usage: givepoint <player_id>";
    }
}
