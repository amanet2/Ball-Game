public class xComCreateServerPlayer extends xCom {
    public String doCommand(String fullCommand) {
        gPlayer player0 = new gPlayer(-10000, -10000,150,150,
                eUtils.getPath(String.format("animations/player_%s/a03.png", sVars.get("playercolor"))));
        cGameLogic.setUserPlayer(player0);
        player0.put("id", uiInterface.uuid);
        player0.put("color", sVars.get("playercolor"));
        nServer.instance().isPlaying = true;
        player0.putInt("tag", eManager.currentMap.scene.playersMap().size());
        player0.put("stockhp", cVars.get("maxstockhp"));
        cScoreboard.addId(uiInterface.uuid);
//        nServer.instance().clientIds.add(uiInterface.uuid);
        eManager.currentMap.scene.playersMap().put(player0.get("id"), player0);
        cScripts.centerCamera();
        return "created user player";
    }
}
