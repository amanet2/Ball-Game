import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class xComEditorShowFlares extends xCom {
    public String doCommand(String fullCommand) {
        HashMap flaresMap = eManager.currentMap.scene.getThingMap("THING_FLARE");
        Object[] titles = new Object[flaresMap.size()];
        int i = 0;
        for(Object id : flaresMap.keySet()) {
            gFlare t = (gFlare) flaresMap.get(id);
            titles[i] = String.format("%d. x%d y%d w%d h%d r%d g%d b%d a%d r%d g%d b%d a%d\n",
                    t.getInt("tag"), t.getInt("coordx"),t.getInt("coordy"), t.getInt("dimw"), t.getInt("dimh"),
                    t.getInt("r1"), t.getInt("g1"), t.getInt("b1"), t.getInt("a1"), t.getInt("r2"),
                    t.getInt("g2"), t.getInt("b2"), t.getInt("a2"));
            i++;
        }
        JList list = new JList(titles);
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(500,500));
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        JOptionPane.showMessageDialog(null, scrollPane, titles.length + " Props in Scene", JOptionPane.OK_OPTION);
        if(list.getSelectedIndex() >= 0) {
            xCon.ex(String.format("e_selectflare %d", list.getSelectedIndex()));
            gFlare selectedFlare = (gFlare) eManager.currentMap.scene.getThingMap("THING_FLARE").get(
                    cEditorLogic.state.selectedFlareId);
            cVars.putInt("camx", (selectedFlare.getInt("coordx") - eUtils.unscaleInt(sSettings.width)/2)
                    + selectedFlare.getInt("dimw")/2);
            cVars.putInt("camy", (selectedFlare.getInt("coordy") - eUtils.unscaleInt(sSettings.height)/2)
                    + selectedFlare.getInt("dimh")/2);
        }
        return "";
    }
}
