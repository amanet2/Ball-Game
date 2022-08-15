import java.awt.*;

public class xComPause extends xCom {
    public String doCommand(String fullCommand) {
        cClientVars.instance().put("inplay", uiInterface.inplay ? "0" : "1");
        oDisplay.instance().frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        if(uiInterface.inplay)
            oDisplay.instance().frame.setCursor(oDisplay.instance().blankCursor);
        xCon.ex("exec scripts/editorspawn");
        return fullCommand;
    }
}
