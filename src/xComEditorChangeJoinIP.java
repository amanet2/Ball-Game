import javax.swing.*;

public class xComEditorChangeJoinIP extends xCom {
    public String doCommand(String fullCommand) {
        Thread t = new Thread(() -> {
            String s = (String)JOptionPane.showInputDialog(
                    oDisplay.instance().frame, null,
                    "Change Join Address",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    null);
            if(s != null && s.strip().replace(",", "").length() > 0) {
                sVars.put("joinip", s.replace(",", ""));
                uiEditorMenus.menus.get("Multiplayer").getItem(1).setText("Address: " + sVars.get("joinip"));
            }
        });
        t.start();
        return "";
    }
}
