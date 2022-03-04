import java.awt.*;
import java.awt.font.FontRenderContext;

public class dFonts {
    static FontRenderContext fontrendercontext =
            new FontRenderContext(null, false, true);
    static Stroke defaultStroke = new BasicStroke(1);
    static Stroke thickStroke = new BasicStroke(eUtils.scaleInt(16));
    static Stroke hudStroke = new BasicStroke(eUtils.scaleInt(10));
    static Stroke waypointStroke = new BasicStroke(eUtils.scaleInt(8));

    public static int getStringWidth(Graphics g, String s) {
        return (int)g.getFont().getStringBounds(s, fontrendercontext).getWidth();
    }

    public static void drawCenteredString(Graphics g, String s, int x, int y) {
        Color savedColor = g.getColor();
        g.setColor(Color.BLACK);
        g.drawString(s,x-(int)g.getFont().getStringBounds(s, fontrendercontext).getWidth()/2+3,y+3);
        g.setColor(savedColor);
        g.drawString(s,x-(int)g.getFont().getStringBounds(s, fontrendercontext).getWidth()/2,y);
    }

    public static void drawPlayerNameHud(Graphics g, String s, int x, int y, Color color) {
        g.setColor(Color.BLACK);
        g.drawString(s,x-(int)g.getFont().getStringBounds(s, fontrendercontext).getWidth()/2+3,y+3);
        g.setColor(color);
        g.drawString(s,x-(int)g.getFont().getStringBounds(s, fontrendercontext).getWidth()/2,y);
        int[] bounds = {
                x-(int)g.getFont().getStringBounds(s, fontrendercontext).getWidth()/2
                        - eUtils.unscaleInt(5*sSettings.height/128),
                y - eUtils.unscaleInt(sSettings.height/32),
                eUtils.unscaleInt(sSettings.height/32),
                eUtils.unscaleInt(sSettings.height/32)
        };
        g.fillOval(bounds[0], bounds[1], bounds[2], bounds[3]);
        g.setColor(gColors.getFontColorFromName("normaltransparent"));
        g.drawOval(bounds[0], bounds[1], bounds[2], bounds[3]);

    }

    public static void drawRightJustifiedString(Graphics g, String s, int x, int y) {
        g.drawString(s,x-(int)g.getFont().getStringBounds(s, fontrendercontext).getWidth(),y);
    }

    public static void setFontColorNormal(Graphics g) {
        g.setColor(gColors.getFontColorFromName("normal"));
    }

    public static void setFontColorNormalTransparent(Graphics g) {
        g.setColor(gColors.getFontColorFromName("normaltransparent"));
    }

    public static void setFontColorHighlight(Graphics g) {
        g.setColor(gColors.getFontColorFromName("highlight"));
    }

    public static void setFontColorAlert(Graphics g) {
        g.setColor(gColors.getFontColorFromName("alert"));
    }

    public static void setFontColorBonus(Graphics g) {
        g.setColor(gColors.getFontColorFromName("bonus"));
    }

    public static void setFontNormal(Graphics g) {
        setFontColorNormal(g);
        g.setFont(
                new Font(sVars.get("fontnameui"), Font.PLAIN,
                        sVars.getInt("fontsize") * sSettings.height / sSettings.gamescale
                )
        );
    }
    public static void setFontGNormal(Graphics g) {
        setFontColorNormal(g);
        g.setFont(
                new Font(sVars.get("fontnameui"), Font.PLAIN, sVars.getInt("fontsize")
                )
        );
    }
    public static Font getFontNormal() {
        return new Font(sVars.get("fontnameui"), Font.PLAIN,
                sVars.getInt("fontsize") * sSettings.height / sSettings.gamescale);
    }
    public static void setFontSmall(Graphics g) {
        setFontColorNormal(g);
        g.setFont(new Font(sVars.get("fontnameui"), Font.PLAIN,
                sVars.getInt("fontsize")*sSettings.height/sSettings.gamescale/2));
    }
    public static Font getFontSmall() {
        return new Font(sVars.get("fontnameui"), Font.PLAIN,
                sVars.getInt("fontsize") * sSettings.height / sSettings.gamescale/2);
    }

    public static void setFontConsole(Graphics g) {
        g.setFont(new Font(sVars.get("fontnameconsole"), Font.PLAIN,
                sVars.getInt("fontsize")*sSettings.height/sSettings.gamescale/2));
    }
}
