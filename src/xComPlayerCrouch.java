public class xComPlayerCrouch extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if (toks.length > 1) {
            String id = toks[1];
            gPlayer p = cGameLogic.getPlayerById(id);
            if (cVars.getInt("maptype") == gMap.MAP_SIDEVIEW) {
                if (p != null) {
                    p.put("dimh", "75");
                    p.setSpriteFromPath(p.get("pathsprite"));
                    p.put("crouch", "1");
                }
            }
        }
        return "crouch";
    }

    public String undoCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if (toks.length > 1) {
            String id = toks[1];
            gPlayer p = cGameLogic.getPlayerById(id);
            if (cVars.getInt("maptype") == gMap.MAP_SIDEVIEW) {
                if (p != null) {
                    p.put("dimh", "150");
                    p.setSpriteFromPath(p.get("pathsprite"));
                    p.put("crouch", "0");
                }
            }
        }
        return "-crouch";
    }
}
