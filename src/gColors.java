import java.awt.*;
import java.util.HashMap;

public class gColors {
    private final HashMap<String, Color> colorMap = new HashMap<>();
    private final HashMap<String, Color> fontColorMap;
    private final HashMap<String, Color> playerHudColors;
    private final HashMap<String, Color> worldColorMap;

    private static gColors instance = null;

    private gColors() {
        xCon.ex("exec config/colors.cfg");
        fontColorMap = new HashMap<>();
        fontColorMap.put("background", Color.BLACK);
        fontColorMap.put("scoreboardbg", new Color(0,0,0,100));
        fontColorMap.put("spawnpoint", new Color(255, 150, 80, 150));
        fontColorMap.put("flooroutline", new Color(100, 100, 255));
        fontColorMap.put("selected", new Color(255, 100, 255));
        fontColorMap.put("preview", new Color(50, 255, 100));
        fontColorMap.put("console", new Color(100,100,150, 100));
        fontColorMap.put("mapmakergrid", new Color(255,255,255,125));
        fontColorMap.put("playerarrow1", new Color(0,150,50, 255));
        fontColorMap.put("playerarrow2", new Color(0,200,0, 100));
        fontColorMap.put("waypoint1", new Color(255,100,50,150));
        fontColorMap.put("waypoint2", new Color(255,100,50,220));
        playerHudColors = new HashMap<>();
        playerHudColors.put("red", new Color(200,50,50));
        playerHudColors.put("orange", new Color(200,100,50));
        playerHudColors.put("yellow", new Color(200,180,0));
        playerHudColors.put("green", new Color(0,180,50));
        playerHudColors.put("blue", new Color(50,160,200));
        playerHudColors.put("teal", new Color(0,200,160));
        playerHudColors.put("pink", new Color(200,0,200));
        playerHudColors.put("purple", new Color(100,0,200));
        worldColorMap = new HashMap<>();
        worldColorMap.put("flooroutline1", new Color(0,0,0,30));
        worldColorMap.put("flooroutline2", new Color(0,0,0,20));
        worldColorMap.put("floorshading1", new Color(0,0,0,30));
        worldColorMap.put("floorshading2", new Color(0,0,0,20));
        worldColorMap.put("shadow1", new Color(0,0,0,200));
        worldColorMap.put("shadow1half", new Color(0,0,0,100));
        worldColorMap.put("shadow2", new Color(0,0,0,0));
        worldColorMap.put("clear", new Color(0,0,0,0));
        worldColorMap.put("roofvertoutline1", new Color(0,0,0,20));
        worldColorMap.put("roofvertoutline2", new Color(0,0,0,90));
        worldColorMap.put("roofvertshading1",new Color(0,0,0,0));
        worldColorMap.put("roofvertshading2", new Color(0,0,0,20));
        worldColorMap.put("roofoutline1", new Color(0,0,0,100));
        worldColorMap.put("roofoutline2",new Color(0,0,0,0));
        worldColorMap.put("roofshading1", new Color(0,0,0,100));
        worldColorMap.put("roofshading2",new Color(0,0,0,0));
        worldColorMap.put("walloutline1", new Color(0,0,0,80));
        worldColorMap.put("walloutline2", new Color(0,0,0,255));
        worldColorMap.put("walllowoutline1", new Color(0,0,0,140));
        worldColorMap.put("walllowoutline2", new Color(0,0,0,255));
        worldColorMap.put("wallshading1", new Color(0,0,0,80));
        worldColorMap.put("wallshading2", new Color(0,0,0,200));
        worldColorMap.put("walllowshading1", new Color(0,0,0,120));
        worldColorMap.put("walllowshading2", new Color(0,0,0,200));
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

    public static Color getFontColorFromName(String name) {
        return instance().getFontColor(name);
    }

    public static Color getPlayerHudColorFromName(String name) {
        return instance().getPlayerHudColor(name);
    }

    public static Color getWorldColorFromName(String name) {
        return instance().getWorldColor(name);
    }

    private Color getWorldColor(String name) {
        return getColor(worldColorMap, name);
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
