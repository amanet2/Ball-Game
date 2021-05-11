import java.awt.*;

public class xComPause extends xCom {
    public String doCommand(String fullCommand) {
        uiInterface.inplay = !uiInterface.inplay;
        if(uiInterface.inplay) {
            oDisplay.instance().frame.setCursor(oDisplay.instance().blankCursor);
        }
        else {
            oDisplay.instance().frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
        return fullCommand;
    }
}
