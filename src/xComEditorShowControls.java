import javax.swing.*;
import java.awt.*;

public class xComEditorShowControls extends xCom {
    public String doCommand(String fullCommand) {
        uiMenuItem[] controls = uiMenusControls.getBindsAsMenuItems();
        Object[] titles = new Object[controls.length];
        for(int i=0;i < titles.length;i++) {
            titles[i] = controls[i].text;
        }
        JList list = new JList(titles);
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(500,500));
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        JOptionPane.showMessageDialog(null, scrollPane, "Controls", JOptionPane.OK_OPTION);
        return "";
    }
}
