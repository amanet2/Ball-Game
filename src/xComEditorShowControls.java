import javax.swing.*;
import java.awt.*;

public class xComEditorShowControls extends xCom {
    public String doCommand(String fullCommand) {
        Object[] titles = new Object[]{
                "MOUSE_LEFT : throw rock",
                "W : move up",
                "S : move down",
                "A : move left",
                "D : move right",
                "TAB : show scoreboard",
                "Y : chat",
                "= : zoom in",
                "- : zoom out",
                "~ : console"
        };
        JList list = new JList(titles);
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(list.getFixedCellWidth(),240));
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        JOptionPane.showMessageDialog(null, scrollPane, "Controls", JOptionPane.OK_OPTION);
        return "";
    }
}
