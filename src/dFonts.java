import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.font.FontRenderContext;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Stroke;

public class dFonts {
    static FontRenderContext fontrendercontext =
            new FontRenderContext(null, false, true);
    static Stroke defaultStroke = new BasicStroke(1);
    static Stroke thickStroke = new BasicStroke(eUtils.scaleInt(16));
    static int size = 90;
    static String fontnameconsole = "monospaced";
    static Font fontNormal = new Font(xMain.shellLogic.clientVars.get("fontui"), Font.PLAIN,
            size * sSettings.height / sSettings.gamescale);
    static Font fontLarge = new Font(xMain.shellLogic.clientVars.get("fontui"), Font.PLAIN,
            (size * sSettings.height / sSettings.gamescale)*2);
    static Font fontGNormal = new Font(xMain.shellLogic.clientVars.get("fontui"), Font.PLAIN, size);
    static Font fontSmall = new Font(xMain.shellLogic.clientVars.get("fontui"), Font.PLAIN,
            size *sSettings.height/sSettings.gamescale/2);
    static Font fontConsole = new Font(fontnameconsole, Font.PLAIN, size *sSettings.height/sSettings.gamescale/2);

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

    public static void drawPlayerNameScoreboard(Graphics g, String s, int x, int y, Color color) {
        g.setColor(Color.BLACK);
        g.drawString(s,x+3,y+3);
        g.setColor(color);
        g.drawString(s,x,y);
    }

    public static void drawRightJustifiedString(Graphics g, String s, int x, int y) {
        g.drawString(s,x-(int)g.getFont().getStringBounds(s, fontrendercontext).getWidth(),y);
    }

    public static void setFontNormal(Graphics g) {
        setFontColor(g, "clrf_normal");
        g.setFont(fontNormal);
    }

    public static void setFontLarge(Graphics g) {
        setFontColor(g, "clrf_normal");
        g.setFont(fontLarge);
    }

    public static void setFontGNormal(Graphics g) {
        setFontColor(g, "clrf_normal");
        g.setFont(fontGNormal);
    }

    public static void setFontSmall(Graphics g) {
        setFontColor(g, "clrf_normal");
        g.setFont(fontSmall);
    }

    public static void setFontConsole(Graphics g) {
        g.setFont(fontConsole);
    }

    public static void setFontColor(Graphics g, String name) {
        g.setColor(gColors.getColorFromName(name));
    }

    public static void refreshFonts() {
        fontNormal = new Font(xMain.shellLogic.clientVars.get("fontui"), Font.PLAIN,
                size * sSettings.height / sSettings.gamescale);
        fontGNormal = new Font(xMain.shellLogic.clientVars.get("fontui"), Font.PLAIN, size);
        fontSmall = new Font(xMain.shellLogic.clientVars.get("fontui"), Font.PLAIN,
                size *sSettings.height/sSettings.gamescale/2);
        fontConsole = new Font(fontnameconsole, Font.PLAIN, size *sSettings.height/sSettings.gamescale/2);
        fontLarge = new Font(xMain.shellLogic.clientVars.get("fontui"), Font.PLAIN,
                (size * sSettings.height / sSettings.gamescale)*2);
    }
}
