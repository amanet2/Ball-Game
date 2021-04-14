public class xComJump extends xCom {
    public String doCommand(String fullCommand) {
        if(cGameLogic.userPlayer() != null) {
            if(cVars.getLong("jumptime") < System.currentTimeMillis()) {
                xCon.ex("crouch");
                cVars.put("jumping", "1");
                cVars.putLong("jumptime", System.currentTimeMillis() + cVars.getInt("delayjump"));
                cGameLogic.userPlayer().putInt("vel0", cVars.getInt("velocityplayer")/2);
                cGameLogic.userPlayer().putInt("vel1", 0);
                cVars.put("stockspeed", "0");
            }
        }
        return fullCommand;
    }
}
