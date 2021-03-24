import java.awt.*;
import java.awt.font.FontRenderContext;

public class dFonts {
    static FontRenderContext fontrendercontext =
            new FontRenderContext(null, false, true);

    public static void drawCenteredString(Graphics g, String s, int x, int y) {
        Color savedColor = g.getColor();
        g.setColor(Color.BLACK);
        g.drawString(s,x-(int)g.getFont().getStringBounds(s, fontrendercontext).getWidth()/2+3,y+3);
        g.setColor(savedColor);
        g.drawString(s,x-(int)g.getFont().getStringBounds(s, fontrendercontext).getWidth()/2,y);
    }

    public static void drawRightJustifiedString(Graphics g, String s, int x, int y) {
        g.drawString(s,x-(int)g.getFont().getStringBounds(s, fontrendercontext).getWidth(),y);
    }

    public static void setFontColorByTitleWithTransparancy(Graphics g, String fonttitle, int transparency) {
        String[] fontStrings = sVars.get(fonttitle).split(",");
        g.setColor(new Color(Integer.parseInt(fontStrings[0]),
                Integer.parseInt(fontStrings[1]),
                Integer.parseInt(fontStrings[2]),
                transparency));
    }

    public static void setFontColorByTitle(Graphics g, String fonttitle) {
        String[] fontStrings = sVars.get(fonttitle).split(",");
        g.setColor(new Color(Integer.parseInt(fontStrings[0]),
                Integer.parseInt(fontStrings[1]),
                Integer.parseInt(fontStrings[2]),
                Integer.parseInt(fontStrings[3])));
    }

    public static void setFontColorNormal(Graphics g) {
        setFontColorByTitle(g, "fontcolornormal");
    }

    public static void setFontColorHighlight(Graphics g) {
        setFontColorByTitle(g, "fontcolorhighlight");
    }

    public static void setFontColorAlert(Graphics g) {
        setFontColorByTitle(g, "fontcoloralert");
    }

    public static void setFontColorBonus(Graphics g) {
        setFontColorByTitle(g, "fontcolorbonus");
    }

    public static void setFontNormal(Graphics g) {
        setFontColorNormal(g);
        g.setFont(
                new Font(sVars.get("fontnameui"), sVars.getInt("fontmode"),
                        sVars.getInt("fontsize") * sSettings.height / cVars.getInt("gamescale")
                )
        );
    }
    public static void setFontSmall(Graphics g) {
        setFontColorNormal(g);
        g.setFont(new Font(sVars.get("fontnameui"), sVars.getInt("fontmode"),
                sVars.getInt("fontsize")*sSettings.height/cVars.getInt("gamescale")/2));
    }

    public static void setFontConsole(Graphics g) {
        g.setFont(new Font(sVars.get("fontnameconsole"), sVars.getInt("fontmode"),
                sVars.getInt("fontsize")*sSettings.height/cVars.getInt("gamescale")/2));
    }
}
