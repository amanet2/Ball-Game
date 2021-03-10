public class xComCreateServerPlayer extends xCom {
    public String doCommand(String fullCommand) {
        gPlayer player = new gPlayer(-10000, -10000,150,150,
                eUtils.getPath(String.format("animations/player_%s/a03.png", sVars.get("playercolor"))));
        cGameLogic.setUserPlayer(player);
        player.put("tag", Integer.toString(eManager.currentMap.scene.playersMap().size()));
        player.put("id", uiInterface.uuid);
        eManager.currentMap.scene.playersMap().put(player.get("id"), player);
        xCon.ex("centercamera");
        return "created user player";
    }
}
