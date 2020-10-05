public class xComCentercamera extends xCom {
    public String doCommand(String fullCommand) {
        if(eManager.currentMap.scene.players().size() > cVars.getInt("camplayertrackingindex")) {
            cVars.putInt("cammode", gCamera.MODE_TRACKING);
            cVars.putInt("camx",
                    ((cGameLogic.getPlayerByIndex(cVars.getInt("camplayertrackingindex")).getInt("coordx")
                            - eUtils.unscaleInt(sSettings.width)/2)
                    + cGameLogic.getPlayerByIndex(cVars.getInt("camplayertrackingindex")).getInt("dimw")/2));
            cVars.putInt("camy",
                    ((cGameLogic.getPlayerByIndex(cVars.getInt("camplayertrackingindex")).getInt("coordy")
                            - eUtils.unscaleInt(sSettings.height)/2)
                    + cGameLogic.getPlayerByIndex(cVars.getInt("camplayertrackingindex")).getInt("dimh")/2));
        }
        return "camera centered";
    }
}
