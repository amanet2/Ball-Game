import java.util.Arrays;
import java.util.List;

public class xComGivePoint extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if (toks.length > 1) {
            String id = toks[1];
            if (cVars.isOne("gameteam")) {
                String color = cGameLogic.getPlayerById(id).get("color");
                for (String mapid : nServer.scoresMap.keySet()) {
                    if (color.equals(cGameLogic.getPlayerById(mapid).get("color"))) {
                        nServer.incrementScoreFieldById(mapid, "score");
                        gPlayer givePointPlayer = cGameLogic.getPlayerById(mapid);
                        if(givePointPlayer != null) {
                            cScripts.createScorePopup(givePointPlayer, 1);
                        }
                    }
                }
            }
            else {
                nServer.incrementScoreFieldById(id, "score");
                gPlayer givePointPlayer = cGameLogic.getPlayerById(id);
                if(givePointPlayer != null) {
                    cScripts.createScorePopup(givePointPlayer, 1);
                }
            }
            String[] quietGameModesArray = {
                    Integer.toString(cGameMode.KING_OF_FLAGS),
                    Integer.toString(cGameMode.CHOSENONE),
                    Integer.toString(cGameMode.VIRUS_SINGLE),
                    Integer.toString(cGameMode.VIRUS),
                    Integer.toString(cGameMode.FLAG_MASTER),
                    Integer.toString(cGameMode.DEATHMATCH)
            };
            List<String> quietGameModesList = Arrays.asList(quietGameModesArray);
            if(!quietGameModesList.contains(cVars.get("gamemode")))
                xCon.ex("say " + cGameLogic.getPlayerById(id).get("name") + " scored");
            return "gave point to " + id;
        }
        return "usage: givepoint <player_id>";
    }
}
