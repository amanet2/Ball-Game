public class xComCentercamera extends xCom {
    public String doCommand(String fullCommand) {
        gThing p = cGameLogic.getPlayerById(cVars.get("camplayertrackingid"));
        if(p == null)
            p = cGameLogic.getUserPlayer();
        if(p != null) {
            cVars.putInt("cammode", gCamera.MODE_TRACKING);
            cVars.putInt("camx",
                    ((p.getInt("coordx") - eUtils.unscaleInt(sSettings.width)/2) + p.getInt("dimw")/2));
            cVars.putInt("camy",
                    ((p.getInt("coordy") - eUtils.unscaleInt(sSettings.height)/2) + p.getInt("dimh")/2));
        }
        return "camera centered";
    }
}
