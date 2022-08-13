import java.awt.*;
import java.util.HashMap;

public class gColors {
    private final HashMap<String, Color> colorMap = new HashMap<>();
    private final HashMap<String, Color> playerHudColors;

    private static gColors instance = null;

    private gColors() {
        xCon.ex("exec config/colors.cfg");
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

    public static int hpAlpha = 230;

    public Color getColorFromName(String name) {
        //return color if in the map
        if(colorMap.containsKey(name))
            return colorMap.get(name);
        //not in the map, check our cvars for color info
        String s = cClientVars.instance().get(name);
        //if not in cvars, return default white
        if(s == null)
            return Color.WHITE;
        String[] args = s.split(" ");
        //return white if there arent enough args to build a color object
        if(args.length < 3)
            return Color.WHITE;
        //color without alpha
        if(args.length < 4) {
            Color c = new Color(
                    Integer.parseInt(args[0]),
                    Integer.parseInt(args[1]),
                    Integer.parseInt(args[2])
            );
            colorMap.put(name, c);
            return colorMap.get(name);
        }
        //color with alpha
        Color c = new Color(
                Integer.parseInt(args[0]),
                Integer.parseInt(args[1]),
                Integer.parseInt(args[2]),
                Integer.parseInt(args[3])
        );
        colorMap.put(name, c);
        return colorMap.get(name);
    }

    public static Color getPlayerHudColorFromName(String name) {
        return instance().getPlayerHudColor(name);
    }

    private Color getPlayerHudColor(String name) {
        return getColor(playerHudColors, name);
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
