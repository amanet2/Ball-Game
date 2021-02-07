public class xComSuperSpeed extends xCom {
    public String doCommand(String fullCommand) {
        if(uiInterface.inplay && cVars.isZero("sprint")
                && cVars.getInt("stockspeed") == cVars.getInt("maxstockspeed")) {
            xCon.ex("cv_sprint 1");
            xCon.ex("cv_sprinttime " + (System.currentTimeMillis()+cVars.getInt("stockspeed")));
            xCon.ex("playsound sounds/goodwork.wav");
            gPlayer userPlayer = cGameLogic.userPlayer();
            for(int i = 0; i < 4; i++) {
                if(userPlayer.isOne("mov"+i)) {
                    userPlayer.addVal("vel"+i, cVars.getInt("velocitysuperspeed"));
                }
            }
//            cVars.putInt("stockspeed", 0);
        }
        return fullCommand;
    }
}
