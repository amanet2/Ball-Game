import javax.swing.*;
import java.awt.*;

public class xComEditorChangePlayerName extends xCom {
    public String doCommand(String fullCommand) {
        Thread t = new Thread(() -> {
            JLabel label = new JLabel("Change Player Name");
            label.setFont(dFonts.getFontNormal());
            String s = (String)JOptionPane.showInputDialog(
                    oDisplay.instance().frame, null,
                    "Change Player Name",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    null);
//            String s = (String)JOptionPane.showInputDialog(null, label);
            if(s != null && s.strip().replace(",", "").length() > 0) {
                cClientVars.instance().put("playername", s.replace(",", ""));
//                cClientLogic.playerName = s.replace(",", "");
                uiMenus.menuSelection[uiMenus.MENU_PROFILE].refresh();
                if(sSettings.show_mapmaker_ui)
                    uiEditorMenus.menus.get("Settings").getItem(0).setText("Name: " + cClientLogic.playerName);
            }
        });
        t.start();
        return "";
    }
}
