import java.awt.Color;
import java.util.HashMap;

public class gColors {
    private static final HashMap<String, Color> colorMap = new HashMap<>();

    public static int hpAlpha = 230;

    public static Color getColorFromName(String name) {
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
}
