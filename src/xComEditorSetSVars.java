import javax.swing.*;

public class xComEditorSetSVars extends xCom {
    public String doCommand(String fullCommand) {
        JTextField[] jfields = new JTextField[sVars.vars().keySet().size()];
        int i = 0;
        for(String s : sVars.vars().keySet()) {
            JTextField nf = new JTextField(sVars.get(s));
            nf.setToolTipText(s);
            jfields[i] = nf;
            i++;
        }
        if(JOptionPane.showInputDialog(oDisplay.instance(), jfields, "Settings Variables",
                JOptionPane.OK_CANCEL_OPTION) != null) {
            i = 0;
            for (String s : sVars.vars().keySet()) {
                sVars.put(s, jfields[i].getText());
                i++;
            }
        }
        return "";
    }
}
