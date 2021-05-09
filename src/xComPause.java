import java.awt.*;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class xComPause extends xCom {
    public String doCommand(String fullCommand) {
        uiInterface.inplay = !uiInterface.inplay;
//        if (sSettings.NET_MODE == sSettings.NET_OFFLINE) {
//            if (uiInterface.inplay) {
//                oDisplay.instance().frame.setCursor(oDisplay.instance().blankCursor);
//                xCon.ex("playsound sounds/clampdown.wav");
//                if (sSettings.show_mapmaker_ui)
//                    xCon.ex("respawn");
//            } else {
//                //delete user player
//                xCon.ex("playsound sounds/grenpinpull.wav");
//                if (sSettings.show_mapmaker_ui) {
//                    cGameLogic.setUserPlayer(null);
//                    eManager.currentMap.scene.objectMaps.put("THING_PLAYER", new LinkedHashMap<>());
//                }
//                if (sSettings.show_mapmaker_ui)
//                    oDisplay.instance().frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
//            }
//            //            case sSettings.NET_CLIENT:
////                if (uiInterface.inplay) {
////                    oDisplay.instance().frame.setCursor(oDisplay.instance().blankCursor);
////                    xCon.ex("playsound sounds/clampdown.wav");
////                }
////                if(sSettings.show_mapmaker_ui) {
////                    if(cGameLogic.userPlayer() != null)
////                        xCon.ex("gospectate");
////                    else {
////                        xCon.ex("gounspectate");
////                    }
////                }
////                break;
////            case sSettings.NET_SERVER:
////                if (uiInterface.inplay) {
////                    oDisplay.instance().frame.setCursor(oDisplay.instance().blankCursor);
////                    xCon.ex("playsound sounds/clampdown.wav");
////                }
////                if(sSettings.show_mapmaker_ui) {
////                    if(cGameLogic.userPlayer() != null) {
////                        nServer.instance().isPlaying = false;
////                        xCon.ex("gospectate");
////                    }
////                    else {
////                        xCon.ex("respawn");
////                        nServer.instance().isPlaying = true;
////                    }
////                }
//        }
        return fullCommand;
    }
}
