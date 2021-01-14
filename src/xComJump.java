public class xComJump extends xCom {
    public String doCommand(String fullCommand) {
        if(cGameLogic.getUserPlayer() != null && cVars.getInt("maptype") == gMap.MAP_SIDEVIEW) {
            if(cVars.getLong("jumptime") < System.currentTimeMillis() && cGameLogic.getUserPlayer().canJump()) {
                xCon.ex("crouch");
                xCon.ex("cv_jumping 1");
                cVars.putLong("jumptime", System.currentTimeMillis() + cVars.getInt("delayjump"));
                xCon.ex("THING_PLAYER.0.vel0 " + cVars.getInt("velocityplayer")/2);
                xCon.ex("THING_PLAYER.0.vel1 " + 1);
                xCon.ex("cv_stockspeed 0");
            }
        }
        return fullCommand;
    }
}
