import java.awt.*;

public class dScreenFX {
    public static void drawScreenFX(Graphics g) {
        gPlayer userPlayer = gClientLogic.getUserPlayer();
        Graphics2D g2 = (Graphics2D) g;
        //spawn protection shine
//        if(cGameLogic.drawLocalSpawnProtection()) {
//            int factors = sVars.getInt("vfxfactor");
//            int maxl = cVars.getInt("vfxuialphaflashlight");
//            for (int i = 0; i < factors; i++) {
//                g.setColor(new Color(128, 128, 28,
//                        Math.abs((maxl /(factors/2)) * (Math.abs(((factors / 2) - i)) - (factors / 2)))));
//                g.fillRect(sSettings.width/factors * i, 0,sSettings.width/factors, sSettings.height);
//                g.fillRect(0, sSettings.height/factors * i, sSettings.width, sSettings.height/factors);
//            }
//        }
//        // sprint overlay
//        if(cVars.isOne("sprint")) {
//            int maxl = cVars.getInt("vfxuialphasprint");
//            int factorsw = sSettings.width/sVars.getInt("vfxfactordiv");
//            int factorsh = sSettings.height/sVars.getInt("vfxfactordiv");
//            for (int i = 0; i < factorsw; i++) {
//                g.setColor(new Color(50, 220, 100,
//                        Math.abs(Math.abs((maxl / (factorsw/2)) * (Math.abs(((factorsw/2) - i))-(factorsw/2))) - maxl)
//                                * cVars.getInt("stockspeed")/cVars.getInt("maxstockspeed")/2));
//                g.fillRect(sSettings.width/factorsw * i, 0,sSettings.width/factorsw, sSettings.height);
//            }
//            for (int i = 0; i < factorsh; i++) {
//                g.setColor(new Color(50, 220, 100,
//                        Math.abs(Math.abs((maxl / (factorsh/2)) * (Math.abs(((factorsh/2) - i))-(factorsh/2))) - maxl)
//                                * cVars.getInt("stockspeed")/cVars.getInt("maxstockspeed")/2));
//                g.fillRect(0, sSettings.height/factorsh * i, sSettings.width, sSettings.height/factorsh);
//            }
//
//        }
        // health overlay
        if(userPlayer != null) {
            //threshold to turn on screen fx
            int userhp = Math.max(userPlayer.getInt("stockhp"), 0);
            if (userhp < cVars.getInt("maxstockhp")) {
                int factors = sVars.getInt("vfxfactor");
                int maxl = cVars.getInt("vfxuialphahp");
                for (int i = 0; i < sSettings.width; i += sSettings.width / factors) {
                    for (int j = 0; j < sSettings.height; j += sSettings.height / factors) {
                        int w = sSettings.width / factors;
                        int h = sSettings.height / factors;
                        if (Math.random() > 0.95 && Math.random() > 0.95) {
                            g.setColor(new Color(200, 0, 0, maxl
                                    - maxl * userhp / cVars.getInt("maxstockhp")
                                    + (int) (Math.random() * (-25) + 25)));
                            g.fillRect(i, j, w, h);
                        }
                    }
                }
                int factorsw = sSettings.width / sVars.getInt("vfxfactordiv");
                int factorsh = sSettings.height / sVars.getInt("vfxfactordiv");
                for (int i = 0; i < factorsw; i++) {
                    g.setColor(new Color(100, 0, 0,
                            Math.abs(Math.abs((maxl / (factorsw / 2)) * (Math.abs(((factorsw / 2) - i)) - (factorsw / 2))) - maxl)
                                    * (cVars.getInt("maxstockhp") - userhp) / cVars.getInt("maxstockhp") / 2));
                    g.fillRect(sSettings.width / factorsw * i, 0, sSettings.width / factorsw, sSettings.height);
                }
                for (int i = 0; i < factorsh; i++) {
                    g.setColor(new Color(100, 0, 0,
                            Math.abs(Math.abs((maxl / (factorsh / 2)) * (Math.abs(((factorsh / 2) - i)) - (factorsh / 2))) - maxl)
                                    * (cVars.getInt("maxstockhp") - userhp) / cVars.getInt("maxstockhp") / 2));
                    g.fillRect(0, sSettings.height / factorsh * i, sSettings.width, sSettings.height / factorsh);
                }
            }
        }
        // -- aimer
        if(uiInterface.inplay) {
            int aimerx = eUtils.unscaleInt(cScripts.getMouseCoordinates()[0]);
            int aimery = eUtils.unscaleInt(cScripts.getMouseCoordinates()[1]);
            int cx = eUtils.unscaleInt(cVars.getInt("camx"));
            int cy = eUtils.unscaleInt(cVars.getInt("camy"));
            int snapX = aimerx + cx;
            int snapY = aimery + cy;
            snapX -= eUtils.unscaleInt(cVars.getInt("camx"));
            snapY -= eUtils.unscaleInt(cVars.getInt("camy"));
            snapX = eUtils.scaleInt(snapX);
            snapY = eUtils.scaleInt(snapY);
            int setw = sSettings.height / 96;
            g2.setColor(Color.RED);
            g2.fillOval(snapX - setw / 2, snapY - setw / 2, setw, setw);
        }
    }
}
