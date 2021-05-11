import javax.swing.*;
import java.awt.*;

public class xComEditorChangePlayerName extends xCom {
    public String doCommand(String fullCommand) {
        Thread t = new Thread(new Runnable(){
            public void run(){
                String s = (String)JOptionPane.showInputDialog(
                        oDisplay.instance().frame, null,
                        "Change Player Name",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        null);
                if(s != null && s.strip().replace(",", "").length() > 0) {
                    sVars.put("playername", s.replace(",", ""));
                }
            }
        });
        t.start();
        return "";
    }
}
