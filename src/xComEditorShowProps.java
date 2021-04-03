import javax.swing.*;
import java.awt.*;

public class xComEditorShowProps extends xCom {
    public String doCommand(String fullCommand) {
        Object[] titles = new Object[eManager.currentMap.scene.props().size()];
        for(int i=0;i < titles.length;i++) {
            gProp t = eManager.currentMap.scene.props().get(i);
            titles[i] = String.format("%d. %s %d %d %d %d %d %d\n",
                i, gProps.titles[t.getInt("code")], t.getInt("coordx"), t.getInt("coordy"),
                    t.getInt("dimw"), t.getInt("dimh"), t.getInt("int0"), t.getInt("int1")
            );
        }
        JList list = new JList(titles);
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(500,500));
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        JOptionPane.showMessageDialog(null, scrollPane, titles.length + " Props in Scene", JOptionPane.OK_OPTION);
//        if(list.getSelectedIndex() >= 0) {
//            xCon.ex(String.format("e_selectprop %d", list.getSelectedIndex()));
//            cVars.putInt("camx",
//                    (eManager.currentMap.scene.props().get(cEditorLogic.state.selectedPropId).getInt("coordx")
//                    - eUtils.unscaleInt(sSettings.width)/2)
//                    + eManager.currentMap.scene.props().get(cEditorLogic.state.selectedPropId).getInt("dimw")/2);
//            cVars.putInt("camy",
//                    (eManager.currentMap.scene.props().get(cEditorLogic.state.selectedPropId).getInt("coordy")
//                    - eUtils.unscaleInt(sSettings.height)/2)
//                    + eManager.currentMap.scene.props().get(cEditorLogic.state.selectedPropId).getInt("dimw")/2);
//        }
        return "";
    }
}
