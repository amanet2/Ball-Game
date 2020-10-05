public class xComZoom extends xCom {
    public String doCommand(String fullCommand) {
        double newzoomlevel = eUtils.zoomLevel + 0.10;
        if(newzoomlevel <= 2.0 ) {
            eUtils.zoomLevel = newzoomlevel;
        }
//        int newscalelevel = eUtils.gameScale - xCon.getInt("gamescale")/48;
//        if(newscalelevel >= xCon.getInt("gamescale")/2)
//            eManager.changeScaleLevel(newscalelevel);
        return "+zoom";
    }

    public String undoCommand(String fullCommand) {
        double newzoomlevel = eUtils.zoomLevel - 0.10;
        if(newzoomlevel > 0.2) {
            eUtils.zoomLevel = newzoomlevel;
        }
//        int newscalevel = eUtils.gameScale + xCon.getInt("gamescale")/48;
//        if(newscalevel <= sVars.getInt("gamescale")*4)
//            eManager.changeScaleLevel(newscalevel);
        return "-zoom";
    }
}
