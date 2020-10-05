public class xComSuperSpeed extends xCom {
    public String doCommand(String fullCommand) {
        if(uiInterface.inplay && cVars.isZero("sprint") && cVars.getInt("stockspeed") == cVars.getInt("maxstockspeed")) {
            xCon.ex("cv_sprint 1");
            xCon.ex("cv_sprinttime " + (System.currentTimeMillis()+cVars.getInt("stockspeed")));
            xCon.ex("playsound sounds/goodwork.wav");
            xCon.ex("cv_speedbonus " + ((int)((double)cVars.getInt("velocityplayer")
                    *((double)cVars.getInt("stockspeed")/(double)cVars.getInt("maxstockspeed")))));
        }
        return fullCommand;
    }
}
