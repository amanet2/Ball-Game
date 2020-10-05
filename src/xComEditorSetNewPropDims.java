import javax.swing.*;

public class xComEditorSetNewPropDims extends xCom {
    public String doCommand(String fullCommand) {
        gProp p = cEditorLogic.state.newProp;
        if(p != null) {
            JTextField[] jfields = new JTextField[p.vars().keySet().size()];
            int i = 0;
            for(String s : p.vars().keySet()) {
                JTextField nf = new JTextField(p.get(s));
                nf.setToolTipText(s);
                jfields[i] = nf;
                i++;
            }
            if(JOptionPane.showInputDialog(oDisplay.instance(), jfields, "New Prop Settings",
                    JOptionPane.OK_CANCEL_OPTION) != null) {
                i = 0;
                for (String s : p.vars().keySet()) {
                    p.put(s, jfields[i].getText());
                    i++;
                }
            }
        }
        return "";
    }
}

