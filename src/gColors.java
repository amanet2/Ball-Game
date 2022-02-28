import java.awt.*;
import java.util.HashMap;

public class gColors {
    private HashMap<String, Color> colorMap;
    private static gColors instance = null;

    private gColors() {
        colorMap = new HashMap<>();
        colorMap.put("alert", new Color(200,0,50,200));
        colorMap.put("bonus", new Color(0,200,0,200));
        colorMap.put("highlight", new Color(220,175,0,255));
        colorMap.put("normal", new Color(255,255,255,200));
        colorMap.put("normaltransparent", new Color(255,255,255,100));
    }

    public Color getColorByName(String name) {
        return colorMap.get(name);
    }

    public static gColors instance() {
        if(instance == null)
            instance = new gColors();
        return instance;
    }
}
