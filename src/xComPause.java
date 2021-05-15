import java.awt.*;

public class xComPause extends xCom {
    public String doCommand(String fullCommand) {
        uiInterface.inplay = !uiInterface.inplay;
        oDisplay.instance().frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        if(uiInterface.inplay)
            oDisplay.instance().frame.setCursor(oDisplay.instance().blankCursor);
        if(sSettings.show_mapmaker_ui) {
            if(uiInterface.inplay)
                xCon.ex("gounspectate");
            else
                xCon.ex("gospectate");
        }
        return fullCommand;
    }
}
