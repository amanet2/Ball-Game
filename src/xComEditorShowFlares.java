import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.TreeMap;

public class xComEditorShowFlares extends xCom {
    public String doCommand(String fullCommand) {
        ArrayList<gFlare> flareList = eManager.currentMap.scene.flares();
        Object[] titles = new Object[flareList.size()];
        int i = 0;
        for(gFlare t : flareList) {
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
        JOptionPane.showMessageDialog(null, scrollPane, titles.length + " Flares in Scene",
                JOptionPane.OK_OPTION);
//        if(list.getSelectedIndex() >= 0) {
//            xCon.ex(String.format("e_selectflare %d", list.getSelectedIndex()));
//            gFlare selectedFlare = eManager.currentMap.scene.flares().get(
//                    cEditorLogic.state.selectedFlareTag);
//            cVars.putInt("camx", (selectedFlare.getInt("coordx") - eUtils.unscaleInt(sSettings.width)/2)
//                    + selectedFlare.getInt("dimw")/2);
//            cVars.putInt("camy", (selectedFlare.getInt("coordy") - eUtils.unscaleInt(sSettings.height)/2)
//                    + selectedFlare.getInt("dimh")/2);
//        }
        return "";
    }
}
