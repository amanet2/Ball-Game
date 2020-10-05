import javax.swing.*;
import java.awt.*;

public class xComEditorShowFlares extends xCom {
    public String doCommand(String fullCommand) {
        Object[] titles = new Object[eManager.currentMap.scene.flares().size()];
        for(int i=0;i < titles.length;i++) {
            gFlare t = eManager.currentMap.scene.flares().get(i);
            titles[i] = String.format("%d. x%d y%d w%d h%d r%d g%d b%d a%d r%d g%d b%d a%d\n",
                t.getInt("tag"), t.getInt("coordx"),t.getInt("coordy"), t.getInt("dimw"), t.getInt("dimh"),
                t.getInt("r1"), t.getInt("g1"), t.getInt("b1"), t.getInt("a1"), t.getInt("r2"),
                    t.getInt("g2"), t.getInt("b2"), t.getInt("a2"));
        }
        JList list = new JList(titles);
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(500,500));
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        JOptionPane.showMessageDialog(null, scrollPane, titles.length + " Props in Scene", JOptionPane.OK_OPTION);
        if(list.getSelectedIndex() >= 0) {
            xCon.ex(String.format("e_selectflare %d", list.getSelectedIndex()));
            cVars.putInt("camx",
                    (eManager.currentMap.scene.flares().get(cEditorLogic.state.selectedFlareId).getInt("coordx")
                    - eUtils.unscaleInt(sSettings.width)/2)
                    + eManager.currentMap.scene.flares().get(cEditorLogic.state.selectedFlareId).getInt("dimw")/2);
            cVars.putInt("camy",
                    (eManager.currentMap.scene.flares().get(cEditorLogic.state.selectedFlareId).getInt("coordy")
                    - eUtils.unscaleInt(sSettings.height)/2)
                    + eManager.currentMap.scene.flares().get(cEditorLogic.state.selectedFlareId).getInt("dimh")/2);
        }
        return "";
    }
}
