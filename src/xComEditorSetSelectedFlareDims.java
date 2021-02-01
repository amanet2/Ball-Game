import javax.swing.*;

public class xComEditorSetSelectedFlareDims extends xCom {
    public String doCommand(String fullCommand) {
        gFlare t = (gFlare) eManager.currentMap.scene.getThingMap("THING_FLARE").get(
                cEditorLogic.state.selectedFlareId);
        if(t != null) {
            JTextField[] jfields = new JTextField[t.vars().keySet().size()];
            int i = 0;
            for(String s : t.vars().keySet()) {
                JTextField nf = new JTextField(t.get(s));
                nf.setToolTipText(s);
                jfields[i] = nf;
                i++;
            }
            if(JOptionPane.showInputDialog(oDisplay.instance(), jfields, "Selected Flare Settings",
                    JOptionPane.OK_CANCEL_OPTION) != null) {
                i = 0;
                for (String s : t.vars().keySet()) {
                    t.put(s, jfields[i].getText());
                    i++;
                }
            }
        }
        return "";
    }
}
