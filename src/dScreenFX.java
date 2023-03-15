import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class dScreenFX {
    public static void drawScreenFX(Graphics g) {
        gPlayer userPlayer = cClientLogic.getUserPlayer();
        Graphics2D g2 = (Graphics2D) g;
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
        if(userPlayer != null) {
            //threshold to turn on screen fx
            int userhp = Math.max((int)userPlayer.getDouble("stockhp"), 0);
            if (userhp < cClientLogic.maxhp) {
                int factors = sSettings.vfxfactor;
                int maxl = gColors.hpAlpha;
                Color color = gColors.getColorFromName("clrp_" + cClientVars.instance().get("playercolor"));
                for (int i = 0; i < sSettings.width; i += sSettings.width / factors) {
                    for (int j = 0; j < sSettings.height; j += sSettings.height / factors) {
                        int w = sSettings.width / factors;
                        int h = sSettings.height / factors;
                        if (Math.random() > 0.95 && Math.random() > 0.95) {
                            g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), maxl
                                    - maxl * userhp / cClientLogic.maxhp
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
                                    * (cClientLogic.maxhp - userhp) / cClientLogic.maxhp / 2);
                    g.setColor(hpvfxColor);
                    g.fillRect(sSettings.width / factorsw * i, 0, sSettings.width / factorsw, sSettings.height);
                }
                for (int i = 0; i < factorsh; i++) {
                    Color hpvfxColor = new Color(color.getRed(), color.getGreen(), color.getBlue(),
                            Math.abs(Math.abs((maxl / (factorsh / 2)) * (Math.abs(((factorsh / 2) - i)) - (factorsh / 2))) - maxl)
                                    * (cClientLogic.maxhp - userhp) / cClientLogic.maxhp / 2);
                    g.setColor(hpvfxColor);
                    g.fillRect(0, sSettings.height / factorsh * i, sSettings.width, sSettings.height / factorsh);
                }
            }
        }
        // -- aimer
        if(uiInterface.inplay) {
            int aimerx = eUtils.unscaleInt(uiInterface.getMouseCoordinates()[0]);
            int aimery = eUtils.unscaleInt(uiInterface.getMouseCoordinates()[1]);
            int cx = eUtils.unscaleInt(gCamera.getX());
            int cy = eUtils.unscaleInt(gCamera.getY());
            int snapX = aimerx + cx;
            int snapY = aimery + cy;
            snapX -= eUtils.unscaleInt(gCamera.getX());
            snapY -= eUtils.unscaleInt(gCamera.getY());
            snapX = eUtils.scaleInt(snapX);
            snapY = eUtils.scaleInt(snapY);
            int setw = sSettings.height / 64;
            g2.setColor(gColors.getColorFromName("clrp_" + cClientLogic.playerColor));
            g2.fillOval(snapX - setw / 2, snapY - setw / 2, setw, setw);
        }
    }
}
