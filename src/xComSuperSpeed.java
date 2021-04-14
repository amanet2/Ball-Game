public class xComSuperSpeed extends xCom {
    public String doCommand(String fullCommand) {
        if(cGameLogic.userPlayer() != null && uiInterface.inplay && cVars.isZero("sprint")
                && cVars.getInt("stockspeed") == cVars.getInt("maxstockspeed")) {
//            cVars.put("stockspeed", "0");
            xCon.ex("cv_sprint 1");
            xCon.ex("cv_sprinttime " + (System.currentTimeMillis()+cVars.getInt("stockspeed")));
            xCon.ex("playsound sounds/goodwork.wav");
            gPlayer userPlayer = cGameLogic.userPlayer();
            for(int i = 0; i < 4; i++) {
                if(userPlayer.getInt("vel0") > userPlayer.getInt("vel1")) {
                    userPlayer.put("vel0", cVars.get("velocitysuperspeed"));
                }
                else if(userPlayer.getInt("vel0") < userPlayer.getInt("vel1")) {
                    userPlayer.put("vel1", cVars.get("velocitysuperspeed"));
                }
                if(userPlayer.getInt("vel2") > userPlayer.getInt("vel3")) {
                    userPlayer.put("vel2", cVars.get("velocitysuperspeed"));
                }
                else if(userPlayer.getInt("vel2") < userPlayer.getInt("vel3")) {
                    userPlayer.put("vel3", cVars.get("velocitysuperspeed"));
                }
//                if(!userPlayer.isZero("mov"+i)) {
//                    userPlayer.addVal("vel" + i, cVars.getInt("velocitysuperspeed"));
//                }
            }
        }
        return fullCommand;
    }
}
