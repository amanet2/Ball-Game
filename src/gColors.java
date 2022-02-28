import java.awt.*;
import java.util.HashMap;

public class gColors {
    private final HashMap<String, Color> fontColorMap;
    private final HashMap<String, Color> playerHudColors;
    private static gColors instance = null;

    private gColors() {
        fontColorMap = new HashMap<>();
        fontColorMap.put("alert", new Color(200,0,50,200));
        fontColorMap.put("bonus", new Color(0,200,0,200));
        fontColorMap.put("highlight", new Color(220,175,0,255));
        fontColorMap.put("normal", new Color(255,255,255,200));
        fontColorMap.put("normaltransparent", new Color(255,255,255,100));
        fontColorMap.put("background", Color.BLACK);
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

    public static Color getFontColorFromName(String name) {
        return instance().getFontColor(name);
    }

    public static Color getPlayerHudColorFromName(String name) {
        return instance().getPlayerHudColor(name);
    }

    private Color getPlayerHudColor(String name) {
        return getColor(playerHudColors, name);
    }

    private Color getFontColor(String name) {
        return getColor(fontColorMap, name);
    }

    private Color getColor(HashMap<String, Color> map, String name) {
        Color c = map.get(name);
        if(c == null)
            return Color.WHITE;
        return c;
    }

    public static gColors instance() {
        if(instance == null)
            instance = new gColors();
        return instance;
    }
}
