public class xComRespawnClientBotPlayer extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if (toks.length > 1) {
            String id = toks[1];
            gPlayer player = new gPlayer(-6000, -6000,150,150,
                    eUtils.getPath("animations/player_red/a03.png"));
            player.put("id", id);
            eManager.currentMap.scene.playersMap().put(id, player);
        }
        return "created user player";
    }
}
