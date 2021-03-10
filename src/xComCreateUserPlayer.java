import java.util.Arrays;

public class xComCreateUserPlayer extends xCom {
    public String doCommand(String fullCommand) {
        gPlayer player0 = new gPlayer(-10000, -10000,150,150,
                eUtils.getPath(String.format("animations/player_%s/a03.png", sVars.get("playercolor"))));
        cGameLogic.setUserPlayer(player0);
        player0.put("tag", "0");
        player0.put("id", uiInterface.uuid);
        eManager.currentMap.scene.playersMap().put(player0.get("id"), player0);
        xCon.ex("centercamera");
        return Arrays.toString(eManager.winClipSelection);
    }
}
