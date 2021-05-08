import java.awt.event.KeyEvent;
import java.util.Collections;

public class xComBindList extends xCom {
    public String doCommand(String fullCommand) {
        xCon.instance().stringLines.add("Current Bindings: ");
        int size = xCon.instance().pressBinds.keySet().size() + xCon.instance().releaseBinds.keySet().size();
        for(Integer j : xCon.instance().releaseBinds.keySet()) {
            if(xCon.instance().pressBinds.containsKey(j))
                size--;
        }
        String[] items = new String[size];
        int ctr = 0;
        for (Integer j : xCon.instance().pressBinds.keySet()) {
            items[ctr] = KeyEvent.getKeyText(j)+" : "+ xCon.instance().pressBinds.get(j);
            ctr++;
        }
        for (Integer j : xCon.instance().releaseBinds.keySet()) {
            if(!xCon.instance().pressBinds.containsKey(j)) {
                items[ctr] = KeyEvent.getKeyText(j)+ " : " + xCon.instance().releaseBinds.get(j);
                ctr++;
            }
        }
        Collections.addAll(xCon.instance().stringLines, items);
        return "";
    }
}
