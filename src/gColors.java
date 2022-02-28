import java.awt.*;
import java.util.HashMap;

public class gColors {
    private HashMap<String, Color> colorMap;
    private static gColors instance = null;

    private gColors() {
        colorMap = new HashMap<>();
        colorMap.put("alert", new Color(200,0,50,200));
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
