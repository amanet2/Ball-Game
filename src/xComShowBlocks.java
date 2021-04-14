import javax.swing.*;
import java.awt.*;

public class xComShowBlocks extends xCom {
    public String doCommand(String fullCommand) {
        Object[] titles = new Object[eManager.currentMap.scene.getThingMap("THING_BLOCK").size()];
        int i = 0;
        for(String id : eManager.currentMap.scene.getThingMap("THING_BLOCK").keySet()) {
            gBlock block = (gBlock) eManager.currentMap.scene.getThingMap("THING_BLOCK").get(id);
            titles[i] = (id + ". " + block.toString());
            i++;
        }
        JList list = new JList(titles);
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(500,500));
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        JOptionPane.showMessageDialog(null, scrollPane, titles.length + " Blocks in Scene", JOptionPane.OK_OPTION);
//        if(list.getSelectedIndex() >= 0) {
//            xCon.ex(String.format("e_selecttile %d", list.getSelectedIndex()));
//            cVars.putInt("camx",
//                    (eManager.currentMap.scene.tiles().get(cEditorLogic.state.selectedTileId).getInt("coordx")
//                    - eUtils.unscaleInt(sSettings.width)/2)
//                    + eManager.currentMap.scene.tiles().get(cEditorLogic.state.selectedTileId).getInt("dimw")/2);
//            cVars.putInt("camy",
//                    (eManager.currentMap.scene.tiles().get(cEditorLogic.state.selectedTileId).getInt("coordy")
//                            - eUtils.unscaleInt(sSettings.height)/2)
//                            + eManager.currentMap.scene.tiles().get(cEditorLogic.state.selectedTileId).getInt("dimh")/2);
//        }
        return "";
    }
}
