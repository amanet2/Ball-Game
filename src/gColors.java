import java.awt.*;
import java.util.HashMap;

public class gColors {
    private final HashMap<String, Color> colorMap;
    private final HashMap<String, Color> playerHudColors;
    private static gColors instance = null;

    private gColors() {
        colorMap = new HashMap<>();
        colorMap.put("alert", new Color(200,0,50,200));
        colorMap.put("bonus", new Color(0,200,0,200));
        colorMap.put("highlight", new Color(220,175,0,255));
        colorMap.put("normal", new Color(255,255,255,200));
        colorMap.put("normaltransparent", new Color(255,255,255,100));
        colorMap.put("background", Color.BLACK);
        playerHudColors = new HashMap<>();
        playerHudColors.put("red", new Color(200,50,50));
        playerHudColors.put("orange", new Color(200,100,50));
        playerHudColors.put("yellow", new Color(200,180,0));
        playerHudColors.put("green", new Color(0,180,50));
        playerHudColors.put("blue", new Color(50,160,200));
        playerHudColors.put("teal", new Color(0,200,160));
        playerHudColors.put("pink", new Color(200,0,200));
        playerHudColors.put("purple", new Color(100,0,200));
    }

    public static Color getColorFromName(String name) {
        return instance().getColorByName(name);
    }

    public static Color getPlayerHudColorFromName(String name) {
        return instance().getPlayerHudColorByName(name);
    }

    private Color getPlayerHudColorByName(String name) {
        Color c = playerHudColors.get(name);
        if(c == null)
            return Color.WHITE;
        return c;
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
