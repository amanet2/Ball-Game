import javax.swing.*;

public class xComMouseRight extends xCom {
    public String doCommand(String fullCommand) {
        if (uiInterface.inplay ) {
            xCon.ex("sspeed");
        }
        else {
            if(sSettings.show_mapmaker_ui) {
//                int o = cEditorLogic.state.selectedTileId;
                cScripts.selectThingUnderMouse(cEditorLogic.getEditorState().createObjCode);
//                int n = cEditorLogic.state.selectedTileId;
//                if(o == n) {
//                    JPopupMenu cm = new JPopupMenu();
//                    cm.add(new JMenuItem("Copy"));
//                    cm.show(oDisplay.instance().frame, iMouse.px, iMouse.py);
//                }
            }
        }
        return fullCommand;
    }

    public String undoCommand(String fullCommand) {
        iMouse.holdingMouseRight = false;
        return fullCommand;
    }
}
