public class xComSpawnPopup extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if (toks.length > 2) {
            gPlayer p = gScene.getPlayerById(toks[1]);
            if(p == null)
                return "no player for id: " + toks[1];
            String msg = toks[2];
            eManager.currentMap.scene.getThingMap("THING_POPUP").put(eManager.createId(),
                    new gPopup(p.getInt("coordx") + (int)(Math.random()*(p.getInt("dimw")+1)),
                            p.getInt("coordy") + (int)(Math.random()*(p.getInt("dimh")+1)),
                            msg, 0.0));
            return "spawned popup " + msg + " for player_id " + toks[1];
        }
        return "usage: spawnpopup <player_id> <points>";
    }
}
