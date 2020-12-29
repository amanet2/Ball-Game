import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

public class dScreenFX {
    public static void drawNavPointer(Graphics2D g2, int dx, int dy, String message) {
        if(dx < -9000 && dy < -9000) {
            return;
        }
        double[] deltas = new double[]{dx - cGameLogic.getPlayerByIndex(0).getInt("coordx")
                + cGameLogic.getPlayerByIndex(0).getInt("dimw")/2,
                dy - cGameLogic.getPlayerByIndex(0).getInt("coordy")
                + cGameLogic.getPlayerByIndex(0).getInt("dimh")/2};
        g2.setColor(new Color(255,200,50,150));
        int[][] polygondims = new int[][]{
                new int[]{
                        eUtils.scaleInt(dx - cVars.getInt("camx")) - sSettings.height/16,
                        eUtils.scaleInt(dx - cVars.getInt("camx")),
                        eUtils.scaleInt(dx - cVars.getInt("camx")) + sSettings.height/16,
                        eUtils.scaleInt(dx - cVars.getInt("camx"))
                },
                new int[]{
                        eUtils.scaleInt(dy - cVars.getInt("camy")),
                        eUtils.scaleInt(dy - cVars.getInt("camy")) - sSettings.height/16,
                        eUtils.scaleInt(dy - cVars.getInt("camy")),
                        eUtils.scaleInt(dy - cVars.getInt("camy")) + sSettings.height/16
                }
        };
        g2.fillPolygon(polygondims[0],polygondims[1], 4);
        g2.setStroke(new BasicStroke(eUtils.scaleInt(16)));
        g2.setColor(new Color(255,100,50,220));
        g2.drawPolygon(polygondims[0], polygondims[1],4);
        //big font
        String waypointdistance = String.format("%dm",(int)Math.sqrt((deltas[0]*deltas[0])+(deltas[1]*deltas[1])));
        cScripts.setFontNormal(g2);
        dScreenMessages.drawCenteredString(g2,
                waypointdistance,
                eUtils.scaleInt(dx - cVars.getInt("camx")),
                eUtils.scaleInt(dy - cVars.getInt("camy")));
        FontRenderContext frc =
                new FontRenderContext(null, false, true);
        g2.drawString(message,
                eUtils.scaleInt(dx - cVars.getInt("camx"))
                        -(int)g2.getFont().getStringBounds(message,frc).getWidth()/2,
                eUtils.scaleInt(dy - cVars.getInt("camy"))
                        -(int)g2.getFont().getStringBounds(waypointdistance,frc).getHeight());
        if(cVars.getInt("gamemode") != cGameMode.VIRUS && cVars.getInt("gamemode") != cGameMode.VIRUS_SINGLE
                && (Math.abs(deltas[0]) > sSettings.width || Math.abs(deltas[1]) > sSettings.height)) {
            double angle = Math.atan2(deltas[1], deltas[0]);
            if (angle < 0)
                angle += 2 * Math.PI;
            angle += Math.PI / 2;
            AffineTransform backup = g2.getTransform();
            AffineTransform a = g2.getTransform();
            a.rotate(angle,
                    sSettings.width / 2,
                    sSettings.height / 2);
            g2.setTransform(a);
            int[][] arrowpolygon = new int[][]{
                    new int[]{sSettings.width / 2 - sSettings.width / 32,
                            sSettings.width / 2 + sSettings.width / 32, sSettings.width / 2},
                    new int[]{sSettings.height / 32, sSettings.height / 32, 0}
            };
            g2.setColor(new Color(255,100,50,180));
            g2.fillPolygon(arrowpolygon[0],arrowpolygon[1], 3);
            g2.setColor(new Color(255,100,50,220));
            g2.setStroke(new BasicStroke(eUtils.scaleInt(8)));
            g2.drawPolygon(arrowpolygon[0],arrowpolygon[1], 3);
            g2.setTransform(backup);
        }
    }

    public static void drawScreenFX(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        //spawn protection shine
        if(cVars.contains("spawnprotectiontime")
                && cVars.getLong("spawnprotectiontime") > System.currentTimeMillis()) {
            int factors = sVars.getInt("vfxfactor");
            int maxl = cVars.getInt("vfxuialphaflashlight");
            for (int i = 0; i < factors; i++) {
                g.setColor(new Color(128, 128, 28,
                        Math.abs((maxl /(factors/2)) * (Math.abs(((factors / 2) - i)) - (factors / 2)))));
                g.fillRect(sSettings.width/factors * i, 0,sSettings.width/factors, sSettings.height);
                g.fillRect(0, sSettings.height/factors * i, sSettings.width, sSettings.height/factors);
            }
        }
        // sprint overlay
        if(cVars.isOne("sprint")) {
            int maxl = cVars.getInt("vfxuialphasprint");
            int factorsw = sSettings.width/sVars.getInt("vfxfactordiv");
            int factorsh = sSettings.height/sVars.getInt("vfxfactordiv");
            for (int i = 0; i < factorsw; i++) {
                g.setColor(new Color(50, 220, 100,
                        Math.abs(Math.abs((maxl / (factorsw/2)) * (Math.abs(((factorsw/2) - i))-(factorsw/2))) - maxl)
                                * cVars.getInt("stockspeed")/cVars.getInt("maxstockspeed")/2));
                g.fillRect(sSettings.width/factorsw * i, 0,sSettings.width/factorsw, sSettings.height);
            }
            for (int i = 0; i < factorsh; i++) {
                g.setColor(new Color(50, 220, 100,
                        Math.abs(Math.abs((maxl / (factorsh/2)) * (Math.abs(((factorsh/2) - i))-(factorsh/2))) - maxl)
                                * cVars.getInt("stockspeed")/cVars.getInt("maxstockspeed")/2));
                g.fillRect(0, sSettings.height/factorsh * i, sSettings.width, sSettings.height/factorsh);
            }

        }
        // health overlay
        if(cVars.getInt("stockhp") < cVars.getInt("maxstockhp")-60) {
            int factors = sVars.getInt("vfxfactor");
            int maxl = cVars.getInt("vfxuialphahp");
            for(int i = 0; i < sSettings.width; i += sSettings.width/factors) {
                for(int j = 0; j < sSettings.height; j += sSettings.height/factors) {
                    int w = sSettings.width/factors;
                    int h = sSettings.height/factors;
                    if(Math.random() > 0.95 && Math.random() > 0.95) {
                        g.setColor(new Color(200, 0, 0, maxl
                                - maxl*cVars.getInt("stockhp")/cVars.getInt("maxstockhp")
                                + (int) (Math.random() * (-25) + 25)));
                        g.fillRect(i, j, w, h);
                    }
                }
            }
            int factorsw = sSettings.width/sVars.getInt("vfxfactordiv");
            int factorsh = sSettings.height/sVars.getInt("vfxfactordiv");
            for (int i = 0; i < factorsw; i++) {
                g.setColor(new Color(100, 0, 0,
                        Math.abs(Math.abs((maxl / (factorsw/2)) * (Math.abs(((factorsw/2) - i))-(factorsw/2))) - maxl)
                                *  (cVars.getInt("maxstockhp")
                                - cVars.getInt("stockhp"))/cVars.getInt("maxstockhp")/2));
                g.fillRect(sSettings.width/factorsw * i, 0,sSettings.width/factorsw, sSettings.height);
            }
            for (int i = 0; i < factorsh; i++) {
                g.setColor(new Color(100, 0, 0,
                        Math.abs(Math.abs((maxl / (factorsh/2)) * (Math.abs(((factorsh/2) - i))-(factorsh/2))) - maxl)
                                *  (cVars.getInt("maxstockhp")
                                - cVars.getInt("stockhp"))/cVars.getInt("maxstockhp")/2));
                g.fillRect(0, sSettings.height/factorsh * i, sSettings.width, sSettings.height/factorsh);
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
            int setw = sSettings.height / 128;
            g2.setColor(Color.RED);
            g2.fillOval(snapX - setw / 2, snapY - setw / 2, setw, setw);
        }
    }
}
