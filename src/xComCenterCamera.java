public class xComCenterCamera extends xCom {
    public String doCommand(String fullCommand) {
        gThing p = cClientLogic.getPlayerById(xCon.ex("setcam trackingid"));
        if(p != null)
            xCon.ex(String.format("setcamcoords %d %d",
                    p.getInt("coordx") + p.getInt("dimw")/2 - eUtils.unscaleInt(sSettings.width)/2,
                    p.getInt("coordy") + p.getInt("dimh")/2 - eUtils.unscaleInt(sSettings.height)/2));
        return "1";
    }
}