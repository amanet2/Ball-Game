import java.awt.*;

public class dScreenFX {
    public static void drawScreenFX(Graphics g) {
        if(!sSettings.IS_CLIENT)
            return;
        nState userState = new nStateMap(xMain.shellLogic.clientNetThread.clientStateSnapshot).get(sSettings.uuid);
        if(userState == null)
            return;
//        Graphics2D g2 = (Graphics2D) g;
        //spawn protection shine
//        if(cGameLogic.drawLocalSpawnProtection()) {
//            int factors = sVars.getInt("vfxfactor");
//            int maxl = sVars.getInt("vfxuialphaflashlight");
//            for (int i = 0; i < factors; i++) {
//                g.setColor(new Color(128, 128, 28,
//                        Math.abs((maxl /(factors/2)) * (Math.abs(((factors / 2) - i)) - (factors / 2)))));
//                g.fillRect(sSettings.width/factors * i, 0,sSettings.width/factors, sSettings.height);
//                g.fillRect(0, sSettings.height/factors * i, sSettings.width, sSettings.height/factors);
//            }
//        }
//        // sprint overlay
//        if(sVars.isOne("sprint")) {
//            int maxl = sVars.getInt("vfxuialphasprint");
//            int factorsw = sSettings.width/sVars.getInt("vfxfactordiv");
//            int factorsh = sSettings.height/sVars.getInt("vfxfactordiv");
//            for (int i = 0; i < factorsw; i++) {
//                g.setColor(new Color(50, 220, 100,
//                        Math.abs(Math.abs((maxl / (factorsw/2)) * (Math.abs(((factorsw/2) - i))-(factorsw/2))) - maxl)
//                                * sVars.getInt("stockspeed")/sVars.getInt("maxstockspeed")/2));
//                g.fillRect(sSettings.width/factorsw * i, 0,sSettings.width/factorsw, sSettings.height);
//            }
//            for (int i = 0; i < factorsh; i++) {
//                g.setColor(new Color(50, 220, 100,
//                        Math.abs(Math.abs((maxl / (factorsh/2)) * (Math.abs(((factorsh/2) - i))-(factorsh/2))) - maxl)
//                                * sVars.getInt("stockspeed")/sVars.getInt("maxstockspeed")/2));
//                g.fillRect(0, sSettings.height/factorsh * i, sSettings.width, sSettings.height/factorsh);
//            }
//
//        }
        // health overlay
        int userhp = Math.max(Integer.parseInt(userState.get("hp")), 0);
        if (userhp < sSettings.clientMaxHP) {
            int factors = sSettings.vfxfactor;
            int maxl = gColors.hpAlpha;
            Color color = gColors.getColorFromName("clrp_" + xMain.shellLogic.clientVars.get("playercolor"));
            for (int i = 0; i < sSettings.width; i += sSettings.width / factors) {
                for (int j = 0; j < sSettings.height; j += sSettings.height / factors) {
                    int w = sSettings.width / factors;
                    int h = sSettings.height / factors;
                    if (Math.random() > 0.95 && Math.random() > 0.95) {
                        g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), maxl
                                - maxl * userhp / sSettings.clientMaxHP
                                + (int) (Math.random() * (-25) + 25)));
                        g.fillRect(i, j, w, h);
                    }
                }
            }
            int factorsdiv = sSettings.vfxfactordiv;
            int factorsw = sSettings.width / factorsdiv;
            int factorsh = sSettings.height / factorsdiv;
            for (int i = 0; i < factorsw; i++) {
                Color hpvfxColor = new Color(color.getRed(), color.getGreen(), color.getBlue(),
                        Math.abs(Math.abs((maxl / (factorsw / 2)) * (Math.abs(((factorsw / 2) - i))
                                - (factorsw / 2))) - maxl)
                                * (sSettings.clientMaxHP - userhp) / sSettings.clientMaxHP / 2);
                g.setColor(hpvfxColor);
                g.fillRect(sSettings.width / factorsw * i, 0, sSettings.width / factorsw, sSettings.height);
            }
            for (int i = 0; i < factorsh; i++) {
                Color hpvfxColor = new Color(color.getRed(), color.getGreen(), color.getBlue(),
                        Math.abs(Math.abs((maxl / (factorsh / 2)) * (Math.abs(((factorsh / 2) - i)) - (factorsh / 2))) - maxl)
                                * (sSettings.clientMaxHP - userhp) / sSettings.clientMaxHP / 2);
                g.setColor(hpvfxColor);
                g.fillRect(0, sSettings.height / factorsh * i, sSettings.width, sSettings.height / factorsh);
            }
        }
    }

    public static void drawFlareFromColor(Graphics2D g2, int x, int y, int w, int h, int mode, Color c1, Color c2) {
        RadialGradientPaint df = new RadialGradientPaint(new Point(x + w/2, y + h/2),
                mode == 1 ? Math.max(w/2, h/2) : Math.min(w/2, h/2),
                new float[]{0f, 1f}, new Color[]{c1, c2}
        );
        g2.setPaint(df);
        g2.fillRect(x, y, w, h);
    }
}
