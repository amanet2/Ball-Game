public class xComCrouch extends xCom {
    public String doCommand(String fullCommand) {
        gPlayer p = cGameLogic.userPlayer();
        if (p != null && p.isZero("crouch")) {
            p.putInt("dimh", 75);
            if(p.wontClipOnMove(1,p.getInt("coordy") + 80))
                p.putInt("coordy", p.getInt("coordy") + 75);
            p.setSpriteFromPath(p.get("pathsprite"));
            p.setHatSpriteFromPath(p.get("pathspritehat"));
            p.putInt("crouch", 1);
        }
        return "crouch";
    }

    public String undoCommand(String fullCommand) {
        gPlayer p = cGameLogic.userPlayer();
        if (p != null && p.isOne("crouch")) {
            p.putInt("dimh", 150);
            if(p.wontClipOnMove(1,p.getInt("coordy")-80))
                p.putInt("coordy", p.getInt("coordy") - 75);
            p.setSpriteFromPath(p.get("pathsprite"));
            p.setHatSpriteFromPath(p.get("pathspritehat"));
            p.putInt("crouch", 0);
        }
        return "-crouch";
    }
}
