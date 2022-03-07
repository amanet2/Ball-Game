import javax.swing.*;
import java.awt.*;

public class dMenus {
    private static Image coverimg = gTextures.getGScaledImage(eUtils.getPath("misc/cover.png"),
                                                             sSettings.width, sSettings.height);
    private static Image logoimg = gTextures.getGScaledImage(eUtils.getPath("misc/logo.png"),
                                                            sSettings.width, sSettings.height/3);

    private static void refreshLogos() {
        coverimg = gTextures.getGScaledImage(eUtils.getPath("misc/cover.png"), sSettings.width, sSettings.height);
        logoimg = gTextures.getGScaledImage(eUtils.getPath("misc/logo.png"), sSettings.width, sSettings.height/3);
    }

    public static void showPauseMenu(Graphics g) {
        if(eUtils.resolutionChanged()) {
            refreshLogos();
        }
        uiInterface.getUIMenuItemUnderMouse();
        g.setColor(gColors.getFontColorFromName("scoreboardbg"));
        g.fillRect(0,0,sSettings.width,sSettings.height);
        g.drawImage(logoimg,0,0,null);
        dFonts.setFontColorHighlight(g);
        dFonts.drawCenteredString(g, uiMenus.menuSelection[uiMenus.selectedMenu].title,
            sSettings.width/2,10*sSettings.height/30);
        dFonts.setFontColorNormal(g);
        dFonts.drawCenteredString(g, "_________",sSettings.width/2,21*sSettings.height/60);
        int ctr = 0;
        int sel = 0;
        for(uiMenuItem i : uiMenus.menuSelection[uiMenus.selectedMenu].items){
            if(ctr == uiMenus.menuSelection[uiMenus.selectedMenu].selectedItem) {
                sel = 1;
                dFonts.setFontColorBonus(g);
                dFonts.drawCenteredString(g,i.text,
                    sSettings.width/2,12*sSettings.height/30+ctr*sSettings.height/30);
                dFonts.setFontColorNormal(g);
                if(oDisplay.instance().frame.getCursor() != Cursor.getPredefinedCursor(Cursor.HAND_CURSOR))
                    oDisplay.instance().frame.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
            else {
                dFonts.drawCenteredString(g,i.text,
                    sSettings.width/2,12*sSettings.height/30+ctr*sSettings.height/30);
            }
            ctr++;
        }
        if(sel == 0 && oDisplay.instance().frame.getCursor() != Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR))
            oDisplay.instance().frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    public static void showControlsMenu(Graphics g) {
        uiInterface.getUIMenuItemUnderMouse();
        g.setColor(gColors.getFontColorFromName("scoreboardbg"));
        g.fillRect(0,0,sSettings.width,sSettings.height);
        g.drawImage(logoimg,0,0,null);
        dFonts.setFontColorHighlight(g);
        dFonts.drawCenteredString(g, uiMenus.menuSelection[uiMenus.selectedMenu].title,
            sSettings.width/2,10*sSettings.height/30);
        dFonts.setFontColorNormal(g);
        dFonts.drawCenteredString(g, "_________",sSettings.width/2,21*sSettings.height/60);
        int ctr = 0;
        for(uiMenuItem i : uiMenus.menuSelection[uiMenus.selectedMenu].items){
            String action = i.text.split(":")[0];
            String input = i.text.split(":")[1];
            if(ctr == uiMenus.menuSelection[uiMenus.selectedMenu].selectedItem) {
                dFonts.drawRightJustifiedString(g,">"+action,
                        sSettings.width/2, 12*sSettings.height/30+ctr%16*sSettings.height/30);
                g.drawString(">"+input,
                        sSettings.width/2, 12*sSettings.height/30+ctr%16*sSettings.height/30);
            }
            else {
                dFonts.drawRightJustifiedString(g," "+action,
                    sSettings.width/2, 12*sSettings.height/30+ctr%16*sSettings.height/30);
                g.drawString(" "+input,
                        sSettings.width/2, 12*sSettings.height/30+ctr%16*sSettings.height/30);
            }
            ctr++;
        }
    }

    public static void showCreditsMenu(Graphics g) {
        if(eUtils.resolutionChanged()) {
            refreshLogos();
        }
        uiInterface.getUIMenuItemUnderMouse();
        g.setColor(gColors.getFontColorFromName("scoreboardbg"));
        g.fillRect(0,0,sSettings.width,sSettings.height);
        g.drawImage(coverimg,0,0,null);
        //copyright notice
        dFonts.setFontColorNormal(g);
        g.drawString("by Anthony Manetti",sSettings.width - sSettings.width / 3, sSettings.height - sSettings.height / 15);
        g.drawString("Venmo @Anthony-Manetti",sSettings.width - sSettings.width / 3, sSettings.height - sSettings.height / 30);
    }
}
