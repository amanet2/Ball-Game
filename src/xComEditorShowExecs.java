import javax.swing.*;
import java.awt.*;

public class xComEditorShowExecs extends xCom {
    public String doCommand(String fullCommand) {
        Object[] titles = new Object[eManager.currentMap.execLines.size()];
        for(int i = 0; i < titles.length; i++) {
            titles[i] = eManager.currentMap.execLines.get(i);
        }
        JList list = new JList(titles);
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(500,500));
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        JOptionPane.showMessageDialog(null, scrollPane, titles.length + " Execs on Load", JOptionPane.OK_OPTION);
        if(list.getSelectedIndex() >= 0) {
            JTextField tf = new JTextField(eManager.currentMap.execLines.get(list.getSelectedIndex()));
            if(JOptionPane.showInputDialog(oDisplay.instance(), tf, "Exec on Load", JOptionPane.OK_CANCEL_OPTION)
                    != null) {
                eManager.currentMap.execLines.set(list.getSelectedIndex(), tf.getText());
            }
        }
        return "";
    }

    public String undoCommand(String fullCommand) {
        JTextField tf = new JTextField("");
        if(JOptionPane.showInputDialog(oDisplay.instance(), tf, "Exec on Load", JOptionPane.OK_CANCEL_OPTION)
                != null) {
            eManager.currentMap.execLines.add(tf.getText());
        }
        return "";
    }
}
