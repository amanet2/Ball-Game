import javax.swing.*;

public class xComEditorChangeJoinPort extends xCom {
    public String doCommand(String fullCommand) {
        Thread t = new Thread(new Runnable(){
            public void run(){
                String s = (String)JOptionPane.showInputDialog(
                        oDisplay.instance().frame, null,
                        "Change Join Port",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        null);
                if(s != null && s.strip().replace(",", "").length() > 0) {
                    sVars.put("joinport", s.replace(",", ""));
                }
            }
        });
        t.start();
        return "";
    }
}
