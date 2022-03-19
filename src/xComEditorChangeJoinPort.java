import javax.swing.*;

public class xComEditorChangeJoinPort extends xCom {
    public String doCommand(String fullCommand) {
        Thread t = new Thread(() -> {
            String s = (String)JOptionPane.showInputDialog(
                    oDisplay.instance().frame, null,
                    "Change Join Port",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    null);
            if(s != null && s.strip().replace(",", "").length() > 0) {
                cClientLogic.joinport = Integer.parseInt(s.replace(",", ""));
                uiMenus.menuSelection[uiMenus.MENU_JOINGAME].refresh();
                if(sSettings.show_mapmaker_ui)
                    uiEditorMenus.menus.get("Multiplayer").getItem(2).setText("Port: " + cClientLogic.joinport);
            }
        });
        t.start();
        return "";
    }
}
