import javax.swing.*;

public class xComEditorSetNewTileDims extends xCom {
    public String doCommand(String fullCommand) {
        gThingTile p = cEditorLogic.state.newTile;
        if(p != null) {
            JTextField[] jfields = new JTextField[p.vars().keySet().size()];
            int i = 0;
            for(String s : p.vars().keySet()) {
                JTextField nf = new JTextField(p.get(s));
                nf.setToolTipText(s);
                jfields[i] = nf;
                i++;
            }
            if(JOptionPane.showInputDialog(oDisplay.instance(), jfields, "New Tile Settings",
                    JOptionPane.OK_CANCEL_OPTION) != null) {
                i = 0;
                for (String s : p.vars().keySet()) {
                    p.put(s, jfields[i].getText());
                    i++;
                }
            }
            for(int j = 0; j < 5; j++) {
                if(p.getInt(String.format("dim%dh", j)) != 0)
                    p.put(String.format("dim%dw", j), p.get("dimw"));
                else
                    p.put(String.format("dim%dw", j), "0");
            }
            for(int j = 5; j < 7; j++) {
                if(p.getInt(String.format("dim%dw", j)) != 0)
                    p.putInt(String.format("dim%dh", j),
                            p.getInt("dimh")-p.getInt("dim0h")-p.getInt("dim3h")-p.getInt("dim4h"));
                else
                    p.putInt(String.format("dim%dh", j), 0);
            }
        }
        return "";
    }
}

