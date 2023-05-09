import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;

public class dMenus {
    private static Image logoimg = gTextures.getGScaledImage(eManager.getPath("misc/logo.png"),
                                                            sSettings.width, sSettings.height/3);

    public static void refreshLogos() {
        logoimg = gTextures.getGScaledImage(eManager.getPath("misc/logo.png"), sSettings.width, sSettings.height/3);
    }

    public static void showPauseMenu(Graphics g) {
        uiInterface.getUIMenuItemUnderMouse();
        dFonts.setFontColor(g, "clrf_scoreboardbg");
        g.fillRect(0,0,sSettings.width,sSettings.height);
        g.drawImage(logoimg,0,0,null);
        dFonts.setFontColor(g, "clrf_highlight");
        dFonts.drawCenteredString(g, uiMenus.menuSelection[uiMenus.selectedMenu].title,
            sSettings.width/2,10*sSettings.height/30);
        dFonts.setFontColor(g, "clrf_normal");
        dFonts.drawCenteredString(g, "_________",sSettings.width/2,21*sSettings.height/60);
        int ctr = 0;
        int sel = 0;
        for(uiMenuItem i : uiMenus.menuSelection[uiMenus.selectedMenu].items){
            if(uiMenus.selectedMenu == uiMenus.MENU_CONTROLS) {
                String action = i.text.split(":")[0];
                String input = i.text.split(":")[1];
                dFonts.drawRightJustifiedString(g," "+action,
                        sSettings.width/2, 12*sSettings.height/30+ctr%16*sSettings.height/30);
                g.drawString(" "+input,
                        sSettings.width/2, 12*sSettings.height/30+ctr%16*sSettings.height/30);
            }
            else if(uiMenus.selectedMenu != uiMenus.MENU_CREDITS && ctr == uiMenus.menuSelection[uiMenus.selectedMenu].selectedItem) {
                sel = 1;
                dFonts.setFontColor(g, "clrf_bonus");
                dFonts.drawCenteredString(g,i.text,
                    sSettings.width/2,12*sSettings.height/30+ctr*sSettings.height/30);
                dFonts.setFontColor(g, "clrf_normal");
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
}
