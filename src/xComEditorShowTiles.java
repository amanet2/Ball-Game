import javax.swing.*;
import java.awt.*;

public class xComEditorShowTiles extends xCom {
    public String doCommand(String fullCommand) {
        Object[] titles = new Object[eManager.currentMap.scene.tiles().size()];
        for(int i=0;i < titles.length;i++) {
            gTile t = eManager.currentMap.scene.tiles().get(i);
            titles[i] = String.format("%s. %s %s %s %s %s %d %d %d %d %d %d %d %d %d %s %s\n",
                t.get("id"),
                t.get("sprite0").replace(xCon.ex("datapath")+"/",""),
                t.get("sprite1").replace(xCon.ex("datapath")+"/",""),
                t.get("sprite2").replace(xCon.ex("datapath")+"/",""),
                t.get("coordx"), t.get("coordy"), t.getInt("dimw"), t.getInt("dimh"), t.getInt("dim0h"),
                t.getInt("dim1h"), t.getInt("dim2h"), t.getInt("dim3h"), t.getInt("dim4h"),
                t.getInt("dim5w"), t.getInt("dim6w"), t.get("brightness"), t.get("canspawn")
            );
        }
        JList list = new JList(titles);
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(500,500));
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        JOptionPane.showMessageDialog(null, scrollPane, titles.length + " Tiles in Scene", JOptionPane.OK_OPTION);
        if(list.getSelectedIndex() >= 0) {
            xCon.ex(String.format("e_selecttile %d", list.getSelectedIndex()));
            cVars.putInt("camx",
                    (eManager.currentMap.scene.tiles().get(cEditorLogic.state.selectedTileId).getInt("coordx")
                    - eUtils.unscaleInt(sSettings.width)/2)
                    + eManager.currentMap.scene.tiles().get(cEditorLogic.state.selectedTileId).getInt("dimw")/2);
            cVars.putInt("camy",
                    (eManager.currentMap.scene.tiles().get(cEditorLogic.state.selectedTileId).getInt("coordy")
                            - eUtils.unscaleInt(sSettings.height)/2)
                            + eManager.currentMap.scene.tiles().get(cEditorLogic.state.selectedTileId).getInt("dimh")/2);
        }
        return "";
    }
}
