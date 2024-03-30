import java.awt.*;

public class dMenus {
    private static Image logoimg = gTextures.getGScaledImage(eManager.getPath("misc/logo.png"),
                                                            sSettings.width, sSettings.height/3);

    public static void refreshLogos() {
        logoimg = gTextures.getGScaledImage(eManager.getPath("misc/logo.png"), sSettings.width, sSettings.height/3);
    }

    public static void showPauseMenu(Graphics g) {
        xMain.shellLogic.getUIMenuItemUnderMouse();
        dFonts.setFontColor(g, "clrf_scoreboardbg");
        g.fillRect(0,0,sSettings.width,sSettings.height);
        g.drawImage(logoimg,0,0,null);
        g.setColor(Color.GRAY);
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
                if(uiMenus.selectedMenu == uiMenus.MENU_COLOR && !xMain.shellLogic.console.ex("cl_setvar clrp_" + i.text).contains("null"))
                    dFonts.setFontColor(g, "clrp_" + i.text);
                else
                    g.setColor(Color.WHITE);
                dFonts.drawCenteredString(g,i.text,
                    sSettings.width/2,12*sSettings.height/30+ctr*sSettings.height/30);
                dFonts.setFontColor(g, "clrf_normal");
                if(xMain.shellLogic.frame.getCursor() != Cursor.getPredefinedCursor(Cursor.HAND_CURSOR))
                    xMain.shellLogic.frame.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
            else {
                dFonts.drawCenteredString(g,i.text,
                    sSettings.width/2,12*sSettings.height/30+ctr*sSettings.height/30);
            }
            ctr++;
        }
        if(sel == 0 && xMain.shellLogic.frame.getCursor() != Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR))
            xMain.shellLogic.frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
}
