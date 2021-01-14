public class xComCrouch extends xCom {
    public String doCommand(String fullCommand) {
        if(cVars.getInt("maptype") == gMap.MAP_SIDEVIEW) {
            gPlayer p = cGameLogic.getUserPlayer();
            if (p != null && p.isZero("crouch")) {
                xCon.ex("THING_PLAYER.0.dimh 75");
                p.setSpriteFromPath(p.get("pathsprite"));
                p.setHatSpriteFromPath(p.get("pathspritehat"));
                xCon.ex("THING_PLAYER.0.crouch 1");
            }
        }
        return "crouch";
    }

    public String undoCommand(String fullCommand) {
        if(cVars.getInt("maptype") == gMap.MAP_SIDEVIEW) {
            gPlayer p = cGameLogic.getUserPlayer();
            if (p != null && p.isOne("crouch")) {
                xCon.ex("THING_PLAYER.0.dimh 150");
                if(p.wontClipOnMove(1,p.getInt("coordy")-80))
                    p.putInt("coordy", p.getInt("coordy")-80);
                p.setSpriteFromPath(p.get("pathsprite"));
                p.setHatSpriteFromPath(p.get("pathspritehat"));
                xCon.ex("THING_PLAYER.0.crouch 0");
            }
        }
        return "-crouch";
    }
}
