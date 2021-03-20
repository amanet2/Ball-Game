public class xComSpawnPopup extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if (toks.length > 2) {
            gPlayer p = gScene.getPlayerById(toks[1]);
            int points = Integer.parseInt(toks[2]);
            eManager.currentMap.scene.getThingMap("THING_POPUP").put(cScripts.createId(),
                    new gPopup(p.getInt("coordx") + (int)(Math.random()*(p.getInt("dimw")+1)),
                            p.getInt("coordy") + (int)(Math.random()*(p.getInt("dimh")+1)),
                            String.format("+%d", points), 0.0));
            return "spawned score popup worth " + points + " for player_id " + toks[1];
        }
        return "usage: spawnpopup <player_id> <points>";
    }
}
