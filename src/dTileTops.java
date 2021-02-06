import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class dTileTops {
    public static void drawTops(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        for(gTile t : eManager.currentMap.scene.tiles()) {
            if(t.sprites[0] != null) {
                g2.drawImage(t.sprites[0],
                        eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                        eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                        null
                );
            }
            else {
                g2.setColor(Color.LIGHT_GRAY);
                g2.fillRect(eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                        eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                        eUtils.scaleInt(t.getInt("dim0w")),
                        eUtils.scaleInt(t.getInt("dim0h"))
                );
            }
            if(t.sprites[3] != null) {
                g2.drawImage(t.sprites[3],
                        eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                        eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dimh") - t.getInt("dim3h") - t.getInt("dim4h")),
                        null);
            }
            else {
                g2.setColor(Color.LIGHT_GRAY);
                g2.fillRect(eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                        eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dimh") - t.getInt("dim3h") - t.getInt("dim4h")),
                        eUtils.scaleInt(t.getInt("dim3w")),
                        eUtils.scaleInt(t.getInt("dim3h"))
                );
            }
            g2.setStroke(new BasicStroke(eUtils.scaleInt(16)));
            g2.setColor(new Color(0, 0, 0, 255));
            if(sVars.isOne("vfxenableshading")) {
                if(t.getInt("dim0h") > 0) {
                    GradientPaint gradient = new GradientPaint(
                            eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw")/2 - cVars.getInt("camx")),
                            eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                            new Color(0,0,0, cVars.getInt("vfxroofoutlinealpha1")),
                            eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw")/2 - cVars.getInt("camx")),
                            eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dim0h")),
                            new Color(0,0,0,cVars.getInt("vfxroofoutlinealpha2")));
                    g2.setPaint(gradient);
                    g2.drawRoundRect(eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                            eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                            eUtils.scaleInt(t.getInt("dim0w")),
                            eUtils.scaleInt(t.getInt("dim0h")),
                            eUtils.scaleInt(5),
                            eUtils.scaleInt(5)
                    );
                    gradient = new GradientPaint(
                            eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw")/2 - cVars.getInt("camx")),
                            eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                            new Color(0,0,0, cVars.getInt("vfxroofshadingalpha1")),
                            eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw")/2 - cVars.getInt("camx")),
                            eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dim0h")),
                            new Color(0,0,0,cVars.getInt("vfxroofshadingalpha2")));
                    g2.setPaint(gradient);
                    g2.fillRect(eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                            eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                            eUtils.scaleInt(t.getInt("dim0w")),
                            eUtils.scaleInt(t.getInt("dim0h"))
                    );
                }
                if(t.getInt("dim3h") > 0) {
                    GradientPaint gradient = new GradientPaint(
                            eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw")/2 - cVars.getInt("camx")),
                            eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")
                                    + t.getInt("dimh") - t.getInt("dim3h") - t.getInt("dim4h")),
                            new Color(0,0,0, cVars.getInt("vfxroofoutlinealpha1")),
                            eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw")/2 - cVars.getInt("camx")),
                            eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dim0h")
                                    + t.getInt("dimh") - t.getInt("dim4h")),
                            new Color(0,0,0,cVars.getInt("vfxroofoutlinealpha2")));
                    g2.setPaint(gradient);
                    g2.drawRoundRect(eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                            eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dimh") - t.getInt("dim3h") - t.getInt("dim4h")),
                            eUtils.scaleInt(t.getInt("dim3w")),
                            eUtils.scaleInt(t.getInt("dim3h")),
                            eUtils.scaleInt(5),
                            eUtils.scaleInt(5)
                    );
                    gradient = new GradientPaint(
                            eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw")/2 - cVars.getInt("camx")),
                            eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")
                                    + t.getInt("dimh") - t.getInt("dim3h") - t.getInt("dim4h")),
                            new Color(0,0,0, cVars.getInt("vfxroofshadingalpha1")),
                            eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw")/2 - cVars.getInt("camx")),
                            eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dim0h")
                                    + t.getInt("dimh") - t.getInt("dim4h")),
                            new Color(0,0,0,cVars.getInt("vfxroofshadingalpha2")));
                    g2.setPaint(gradient);
                    g2.fillRect(eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                            eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dimh") - t.getInt("dim3h") - t.getInt("dim4h")),
                            eUtils.scaleInt(t.getInt("dim3w")),
                            eUtils.scaleInt(t.getInt("dim3h"))
                    );
                }
            }
            if(t.sprites[5] != null) {
                g2.drawImage(t.sprites[5],
                        eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                        eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dim0h")),
                        null
                );
            }
            else {
                g2.setColor(Color.LIGHT_GRAY);
                g2.fillRect(eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                        eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dim0h")),
                        eUtils.scaleInt(t.getInt("dim5w")),
                        eUtils.scaleInt(t.getInt("dim5h"))
                );
            }
            int d6w = t.getInt("dim6w");
            if(t.sprites[6] != null) {
                if(d6w > -1)
                    g2.drawImage(t.sprites[6],
                        eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx") + t.getInt("dimw") - t.getInt("dim6w")),
                        eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dim0h")),
                        null
                    );
            }
            else {
                if(d6w > 0) {
                    g2.drawImage(t.sprites[6],
                            eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx") + t.getInt("dimw") - t.getInt("dim6w")),
                            eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dim0h")),
                            null
                    );
                }
                else if(d6w == -1) { //UR
                    Polygon p = new Polygon(
                            new int[]{
                                    eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                                    eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx") + t.getInt("dimw")),
                                    eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx") + t.getInt("dimw"))
                            },
                            new int[]{
                                    eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                                    eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                                    eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dimh"))
                            },
                            3);
                    Shape b = g2.getClip();
                    g2.setClip(p);
                    g2.drawImage(t.spriteT,
                            eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                            eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                            null
                    );
                    if(sVars.isOne("vfxenableshading")) {
                        GradientPaint gradient = new GradientPaint(
                                eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw")/2 - cVars.getInt("camx")),
                                eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                                new Color(0,0,0, cVars.getInt("vfxroofshadingalpha1")),
                                eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw")/2 - cVars.getInt("camx")),
                                eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dimh")),
                                new Color(0,0,0,cVars.getInt("vfxroofshadingalpha2")));
                        g2.setPaint(gradient);
                        g2.drawPolygon(p);
                        g2.fillRect(eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                                eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                                eUtils.scaleInt(t.getInt("dimw")),
                                eUtils.scaleInt(t.getInt("dimh"))
                        );
                    }
                    g2.setClip(b);
                }
                else if(d6w == -2) { //BR
                    Polygon p = new Polygon(
                            new int[]{
                                    eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx") + t.getInt("dimw")),
                                    eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx") + t.getInt("dimw")),
                                    eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx"))
                            },
                            new int[]{
                                    eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                                    eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dimh")),
                                    eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dimh"))
                            },
                            3);
                    Shape b = g2.getClip();
                    g2.setClip(p);
                    g2.drawImage(t.spriteT,
                            eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                            eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                            null
                    );
                    g2.setStroke(new BasicStroke(eUtils.scaleInt(16)));
                    if(sVars.isOne("vfxenableshading")) {
                        GradientPaint gradient = new GradientPaint(
                                eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw")/2 - cVars.getInt("camx")),
                                eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                                new Color(0,0,0, cVars.getInt("vfxroofshadingalpha1")),
                                eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw")/2 - cVars.getInt("camx")),
                                eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dimh")),
                                new Color(0,0,0,cVars.getInt("vfxroofshadingalpha2")));
                        g2.setPaint(gradient);
                        g2.drawPolygon(p);
                        g2.fillRect(eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                                eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                                eUtils.scaleInt(t.getInt("dimw")),
                                eUtils.scaleInt(t.getInt("dimh"))
                        );
                    }
                    g2.setClip(b);
                }
                else if(d6w == -3) { //BL
                    Polygon p = new Polygon(
                            new int[]{
                                    eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                                    eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx") + t.getInt("dimw")),
                                    eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx"))
                            },
                            new int[]{
                                    eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                                    eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dimh")),
                                    eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dimh"))
                            },
                            3);
                    Shape b = g2.getClip();
                    g2.setClip(p);
                    g2.drawImage(t.spriteT,
                            eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                            eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                            null
                    );
                    g2.setStroke(new BasicStroke(eUtils.scaleInt(16)));
                    if(sVars.isOne("vfxenableshading")) {
                        GradientPaint gradient = new GradientPaint(
                                eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw")/2 - cVars.getInt("camx")),
                                eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                                new Color(0,0,0, cVars.getInt("vfxroofshadingalpha1")),
                                eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw")/2 - cVars.getInt("camx")),
                                eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dimh")),
                                new Color(0,0,0,cVars.getInt("vfxroofshadingalpha2")));
                        g2.setPaint(gradient);
                        g2.drawPolygon(p);
                        g2.fillRect(eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                                eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                                eUtils.scaleInt(t.getInt("dimw")),
                                eUtils.scaleInt(t.getInt("dimh"))
                        );
                    }
                    g2.setClip(b);                }
                else if(d6w == -4) { //UL
                    Polygon p = new Polygon(
                            new int[]{
                                    eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                                    eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx") + t.getInt("dimw")),
                                    eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx"))
                            },
                            new int[]{
                                    eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                                    eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                                    eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dimh"))
                            },
                            3);
                    Shape b = g2.getClip();
                    g2.setClip(p);
                    g2.drawImage(t.spriteT,
                            eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                            eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                            null
                    );
                    g2.setStroke(new BasicStroke(eUtils.scaleInt(16)));
                    if(sVars.isOne("vfxenableshading")) {
                        GradientPaint gradient = new GradientPaint(
                                eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw")/2 - cVars.getInt("camx")),
                                eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                                new Color(0,0,0, cVars.getInt("vfxroofshadingalpha1")),
                                eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw")/2 - cVars.getInt("camx")),
                                eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dimh")),
                                new Color(0,0,0,cVars.getInt("vfxroofshadingalpha2")));
                        g2.setPaint(gradient);
                        g2.drawPolygon(p);
                        g2.fillRect(eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                                eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                                eUtils.scaleInt(t.getInt("dimw")),
                                eUtils.scaleInt(t.getInt("dimh"))
                        );
                    }
                    g2.setClip(b);
                }
            }
            if(sVars.isOne("vfxenableshading")) {
                if(t.getInt("dim5w") > 0) {
                    GradientPaint gradient = new GradientPaint(
                            eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw")/2 - cVars.getInt("camx")),
                            eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                            new Color(0,0,0, cVars.getInt("vfxroofvertoutlinealpha1")),
                            eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw")/2 - cVars.getInt("camx")),
                            eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dimh")),
                            new Color(0,0,0, cVars.getInt("vfxroofvertoutlinealpha2")));
                    g2.setPaint(gradient);
                    g2.drawRoundRect(eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                            eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dim0h")),
                            eUtils.scaleInt(t.getInt("dim5w")),
                            eUtils.scaleInt(t.getInt("dim5h")),
                            eUtils.scaleInt(5),
                            eUtils.scaleInt(5)
                    );
                    gradient = new GradientPaint(
                            eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw")/2 - cVars.getInt("camx")),
                            eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                            new Color(0,0,0, cVars.getInt("vfxroofvertshadingalpha1")),
                            eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw")/2 - cVars.getInt("camx")),
                            eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dimh")),
                            new Color(0,0,0, cVars.getInt("vfxroofvertshadingalpha2")));
                    g2.setPaint(gradient);
                    g2.fillRect(eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                            eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dim0h")),
                            eUtils.scaleInt(t.getInt("dim5w")),
                            eUtils.scaleInt(t.getInt("dim5h"))
                    );
                }
                if(t.getInt("dim6w") > 0) {
                    GradientPaint gradient = new GradientPaint(
                            eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw")/2 - cVars.getInt("camx")),
                            eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                            new Color(0,0,0, cVars.getInt("vfxroofvertoutlinealpha1")),
                            eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw")/2 - cVars.getInt("camx")),
                            eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dimh")),
                            new Color(0,0,0, cVars.getInt("vfxroofvertoutlinealpha2")));
                    g2.setPaint(gradient);
                    g2.drawRoundRect(eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx") + t.getInt("dimw") - t.getInt("dim6w")),
                            eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dim0h")),
                            eUtils.scaleInt(t.getInt("dim6w")),
                            eUtils.scaleInt(t.getInt("dim6h")),
                            eUtils.scaleInt(5),
                            eUtils.scaleInt(5)
                    );
                    gradient = new GradientPaint(
                            eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw")/2 - cVars.getInt("camx")),
                            eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                            new Color(0,0,0, cVars.getInt("vfxroofvertshadingalpha1")),
                            eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw")/2 - cVars.getInt("camx")),
                            eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dimh")),
                            new Color(0,0,0, cVars.getInt("vfxroofvertshadingalpha2")));
                    g2.setPaint(gradient);
                    g2.fillRect(eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx") + t.getInt("dimw") - t.getInt("dim6w")),
                            eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dim0h")),
                            eUtils.scaleInt(t.getInt("dim6w")),
                            eUtils.scaleInt(t.getInt("dim6h"))
                    );
                }
            }
        }
        /*
        * players and stuff
        * */
        for(gPlayer e : eManager.currentMap.scene.players()) {
            //player hat
            g2.drawImage(e.spriteHat,
                eUtils.scaleInt(e.getInt("coordx") - cVars.getInt("camx")),
                eUtils.scaleInt(e.getInt("coordy") - cVars.getInt("camy") - 150),
                    null);
            //forbidden sign for spawn protection
            if(nServer.clientArgsMap.containsKey(e.get("id"))
                    && nServer.clientArgsMap.get(e.get("id")).containsKey("spawnprotected")
            && (!cGameLogic.isUserPlayer(e) || cGameLogic.drawSpawnProtection())) {
                g2.drawImage(gTextures.getScaledImage(eUtils.getPath("misc/forbidden.png"), 150,150),
                        eUtils.scaleInt(e.getInt("coordx") - cVars.getInt("camx")),
                        eUtils.scaleInt(e.getInt("coordy") - cVars.getInt("camy")),
                        null);
            }
        }
        //flashlight overlay
        if(cVars.isOne("flashlight")) {
            int maxd = 900;
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
            for(gTile t : eManager.currentMap.scene.tiles()) {
                RadialGradientPaint df = new RadialGradientPaint(new Point(snapX, snapY),
                        eUtils.scaleInt(maxd/2), new float[]{0f, 1f},
                        new Color[]{new Color(0,0,0,0), new Color(0,0,0,255-t.getInt("brightness"))}
                );
                g2.setPaint(df);
                g2.fillRect(eUtils.scaleInt(t.getInt("coordx")-cVars.getInt("camx")),
                        eUtils.scaleInt(t.getInt("coordy")-cVars.getInt("camy")),
                        eUtils.scaleInt(t.getInt("dimw")), eUtils.scaleInt(t.getInt("dimh")));
            }
        }
        else {
            for(gTile t : eManager.currentMap.scene.tiles()) {
                g2.setColor(new Color(0,0,0,255-t.getInt("brightness")));
                g2.fillRect(eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                        eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                        eUtils.scaleInt(t.getInt("dimw")), eUtils.scaleInt(t.getInt("dimh"))
                );
            }
        }
        //flares
        dFlares.drawSceneFlares(g2);
        //bullets
        HashMap bulletsMap = eManager.currentMap.scene.getThingMap("THING_BULLET");
        for(Object id : bulletsMap.keySet()) {
            gBullet t = (gBullet) bulletsMap.get(id);
            g2.drawImage(t.sprite, eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                    eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")), null);
        }
        //animations
        dAnimations.drawAnimations(g2);
        //mapmaker indicators
        if(sSettings.show_mapmaker_ui && !uiInterface.inplay){
            int mousex = MouseInfo.getPointerInfo().getLocation().x;
            int mousey = MouseInfo.getPointerInfo().getLocation().y;
            int window_offsetx = oDisplay.instance().frame.getLocationOnScreen().x;
            int window_offsety = oDisplay.instance().frame.getLocationOnScreen().y;
            // -- selected tile
            if(eManager.currentMap.scene.tiles().size() > cEditorLogic.state.selectedTileId) {
                int st = cEditorLogic.state.selectedTileId;
                int sx = eManager.currentMap.scene.tiles().get(st).getInt("coordx");
                int sy = eManager.currentMap.scene.tiles().get(st).getInt("coordy");
                int sw = eManager.currentMap.scene.tiles().get(st).getInt("dimw");
                int sh = eManager.currentMap.scene.tiles().get(st).getInt("dimh");
                g2.setColor(new Color(255, 0, 200));
                g2.drawRect(eUtils.scaleInt(sx-cVars.getInt("camx")),
                        eUtils.scaleInt(sy-cVars.getInt("camy")),
                        eUtils.scaleInt(sw), eUtils.scaleInt(sh));
            }
            // -- selected prop
            if(eManager.currentMap.scene.props().size() > cEditorLogic.state.selectedPropId) {
                int st = cEditorLogic.state.selectedPropId;
                int sx = eManager.currentMap.scene.props().get(st).getInt("coordx");
                int sy = eManager.currentMap.scene.props().get(st).getInt("coordy");
                int sw = eManager.currentMap.scene.props().get(st).getInt("dimw");
                int sh = eManager.currentMap.scene.props().get(st).getInt("dimh");
                g2.setColor(new Color(255, 150, 0));
                g2.drawRect(eUtils.scaleInt(sx-cVars.getInt("camx")),
                        eUtils.scaleInt(sy-cVars.getInt("camy")),
                        eUtils.scaleInt(sw), eUtils.scaleInt(sh));
            }
            // -- selected flare
            ArrayList<gFlare> flareList = eManager.currentMap.scene.flares();
            if(flareList.size() > cEditorLogic.state.selectedFlareTag) {
                gFlare selectedFlare = flareList.get(cEditorLogic.state.selectedFlareTag);
                int sx = selectedFlare.getInt("coordx");
                int sy = selectedFlare.getInt("coordy");
                int sw = selectedFlare.getInt("dimw");
                int sh = selectedFlare.getInt("dimh");
                g2.setColor(new Color(50, 100, 255));
                g2.drawRect(eUtils.scaleInt(sx-cVars.getInt("camx")),
                        eUtils.scaleInt(sy-cVars.getInt("camy")),
                        eUtils.scaleInt(sw), eUtils.scaleInt(sh));
            }
            // -- preview rect
            int wt = cEditorLogic.state.newTile.getInt("dimw");
            int ht = cEditorLogic.state.newTile.getInt("dimh");
            int wp = cEditorLogic.state.newProp.getInt("dimw");
            int hp = cEditorLogic.state.newProp.getInt("dimh");
            int wf = cEditorLogic.state.newFlare.getInt("dimw");
            int hf = cEditorLogic.state.newFlare.getInt("dimh");
            int w = cEditorLogic.state.createObjCode == gScene.THING_FLARE ? wf
                    : cEditorLogic.state.createObjCode == gScene.THING_PROP ? wp : wt;
            int h = cEditorLogic.state.createObjCode == gScene.THING_FLARE ? hf
                    : cEditorLogic.state.createObjCode == gScene.THING_PROP ? hp : ht;
            int px = eUtils.roundToNearest(eUtils.unscaleInt(mousex - window_offsetx)
                    +cVars.getInt("camx")-w/2, cEditorLogic.state.snapToX) - cVars.getInt("camx");
            int py = eUtils.roundToNearest(eUtils.unscaleInt(mousey - window_offsety)
                    +cVars.getInt("camy")-h/2, cEditorLogic.state.snapToY) - cVars.getInt("camy");
            g2.setColor(new Color(50, 255, 100));
            g2.drawRect(eUtils.scaleInt(px), eUtils.scaleInt(py),
                    eUtils.scaleInt(w), eUtils.scaleInt(h));
        }
        //safezone pointer
        if((cVars.getInt("gamemode") == cGameMode.RACE
            || cVars.getInt("gamemode") == cGameMode.SAFE_ZONES
            || cVars.getInt("gamemode") == cGameMode.CAPTURE_THE_FLAG
            || cVars.getInt("gamemode") == cGameMode.WAYPOINTS
            || cVars.getInt("gamemode") == cGameMode.BOUNCYBALL
            || cVars.getInt("gamemode") == cGameMode.KING_OF_FLAGS
            || cVars.getInt("gamemode") == cGameMode.FLAG_MASTER
            || cVars.getInt("gamemode") == cGameMode.CHOSENONE
            || cVars.getInt("gamemode") == cGameMode.ANTI_CHOSENONE)
            && eManager.currentMap.scene.players().size() > 0){
            //flagmaster nav pointer
            if((cVars.getInt("gamemode") == cGameMode.CAPTURE_THE_FLAG
                    || cVars.getInt("gamemode") == cGameMode.FLAG_MASTER)
                    && !cVars.isVal("flagmasterid", "")
                    && !cVars.get("flagmasterid").equals(uiInterface.uuid)) {
                gPlayer p = cGameLogic.getPlayerById(cVars.get("flagmasterid"));
                dScreenFX.drawNavPointer(g2, p.getInt("coordx") + p.getInt("dimw")/2,
                        p.getInt("coordy") + p.getInt("dimh")/2, "* KILL *");
            }
            //chosen one nav pointer
            if((cVars.getInt("gamemode") == cGameMode.CHOSENONE
                    || cVars.getInt("gamemode") == cGameMode.ANTI_CHOSENONE)
                    && cVars.get("chosenoneid").length() > 0
                    && !(cVars.getInt("gamemode") == cGameMode.CHOSENONE
                    && cVars.get("chosenoneid").equals(uiInterface.uuid))) {
                gPlayer p = cGameLogic.getPlayerById(cVars.get("chosenoneid"));
                if(p != null)
                    dScreenFX.drawNavPointer(g2, p.getInt("coordx") + p.getInt("dimw")/2,
                        p.getInt("coordy") + p.getInt("dimh")/2, "* KILL *");
            }
            for(int i = 0; i < eManager.currentMap.scene.props().size(); i++) {
                gProp p = eManager.currentMap.scene.props().get(i);
                if((cVars.getInt("gamemode") == cGameMode.RACE
                        && p.isInt("code", gProp.SCOREPOINT)
                        && p.getInt("int0") < 1)
                        || (cVars.getInt("gamemode") == cGameMode.SAFE_ZONES && p.isInt("code", gProp.SCOREPOINT)
                            && p.getInt("int0") > 0)
                        || ((cVars.getInt("gamemode") == cGameMode.CAPTURE_THE_FLAG
                            || cVars.getInt("gamemode") == cGameMode.FLAG_MASTER)
                            && p.isInt("code", gProp.FLAGRED) && cVars.isVal("flagmasterid", ""))
                        || ((cVars.getInt("gamemode") == cGameMode.CAPTURE_THE_FLAG
                            || cVars.getInt("gamemode") == cGameMode.FLAG_MASTER)
                            && p.isInt("code", gProp.FLAGBLUE) && cVars.isVal("flagmasterid", uiInterface.uuid))
                        || (cVars.getInt("gamemode") == cGameMode.KING_OF_FLAGS
                            && p.isInt("code", gProp.FLAGRED)
                            && !p.isVal("str0", cGameLogic.userPlayer().get("id")))
                        || (cVars.getInt("gamemode") == cGameMode.WAYPOINTS && p.isInt("code", gProp.SCOREPOINT)
                            && p.getInt("int0") > 0)
                        || (cVars.getInt("gamemode") == cGameMode.BOUNCYBALL && p.isInt("code", gProp.SCOREPOINT)
                            && p.getInt("int0") > 0)
                ) {

                    dScreenFX.drawNavPointer(g2,p.getInt("coordx") + p.getInt("dimw")/2, p.getInt("coordy") + p.getInt("dimh")/2,
                            "* GO HERE *");
//                    if(cVars.getInt("gamemode") == cGameMode.RACE_LINEAR && p.isInt("code", gProp.SCOREPOINT))
//                        break;
                }
            }
        }
        if(cVars.getInt("gamemode") == cGameMode.VIRUS) {
            //waypoints
            if(nServer.clientArgsMap != null && nServer.clientArgsMap.containsKey("server")
                    && nServer.clientArgsMap.get("server").containsKey("state")) {
                String statestr = nServer.clientArgsMap.get("server").get("state");
                for (int i = 0; i < eManager.currentMap.scene.players().size(); i++) {
                    gPlayer p = cGameLogic.getPlayerByIndex(i);
                    if ((sSettings.net_server && i == 0 && statestr.contains("server")) ||
                            (sSettings.net_client && i == 0 && statestr.contains(uiInterface.uuid)) ||
                            (p.get("id").length() > 0 && statestr.contains(p.get("id")))) {
                        dScreenFX.drawNavPointer(g2, p.getInt("coordx") + p.getInt("dimw") / 2,
                                p.getInt("coordy") + p.getInt("dimh") / 2,
                                "* INFECTED *");
                    }
                }
            }
        }
        if(cVars.getInt("gamemode") == cGameMode.VIRUS_SINGLE) {
            //waypoints
            //virus single nav pointer
            if(cVars.get("virussingleid").length() > 0) {
                gPlayer p = cGameLogic.getPlayerById(cVars.get("virussingleid"));
                if(p != null)
                    dScreenFX.drawNavPointer(g2, p.getInt("coordx") + p.getInt("dimw")/2,
                            p.getInt("coordy") + p.getInt("dimh")/2, "* INFECTED *");
                else if(sSettings.net_client && cScripts.isVirus()) {
                    p = cGameLogic.userPlayer();
                    dScreenFX.drawNavPointer(g2, p.getInt("coordx") + p.getInt("dimw")/2,
                            p.getInt("coordy") + p.getInt("dimh")/2, "* INFECTED *");
                }
            }
        }
        //popups
        HashMap popupsMap = eManager.currentMap.scene.getThingMap("THING_POPUP");
        for(Object id : popupsMap.keySet()) {
            gPopup p = (gPopup) popupsMap.get(id);
            g.setColor(p.get("text").charAt(0) == '+' ?
                    new Color(Integer.parseInt(xCon.ex("textcolorbonus").split(",")[0]),
                            Integer.parseInt(xCon.ex("textcolorbonus").split(",")[1]),
                            Integer.parseInt(xCon.ex("textcolorbonus").split(",")[2]),
                            Integer.parseInt(xCon.ex("textcolorbonus").split(",")[3]))
                    : new Color(Integer.parseInt(xCon.ex("textcoloralert").split(",")[0]),
                    Integer.parseInt(xCon.ex("textcoloralert").split(",")[1]),
                    Integer.parseInt(xCon.ex("textcoloralert").split(",")[2]),
                    Integer.parseInt(xCon.ex("textcoloralert").split(",")[3])));
            g.drawString(p.get("text"),
                    eUtils.scaleInt(p.getInt("coordx") - cVars.getInt("camx")),
                    eUtils.scaleInt(p.getInt("coordy") - cVars.getInt("camy")));
        }
        //player highlight
        if(sVars.isOne("playerarrow") && eManager.currentMap.scene.players().size() > 0) {
            g2.setColor(new Color(150,200,200));
            int[][] polygonBase = new int[][]{
                    new int[]{0,2,1},
                    new int[]{0,0,1}
            };
            int polygonSize = sSettings.width/32;
            int[][] polygon = new int[][]{
                    new int[]{eUtils.scaleInt(cGameLogic.userPlayer().getInt("coordx")
                            - cVars.getInt("camx")) + polygonBase[0][0]*polygonSize,
                            eUtils.scaleInt(cGameLogic.userPlayer().getInt("coordx")
                                    - cVars.getInt("camx")) + polygonBase[0][1]*polygonSize,
                            eUtils.scaleInt(cGameLogic.userPlayer().getInt("coordx")
                                    - cVars.getInt("camx")) + polygonBase[0][2]*polygonSize},
                    new int[]{eUtils.scaleInt(cGameLogic.userPlayer().getInt("coordy")
                            - cVars.getInt("camy")-200) + polygonBase[1][0]*polygonSize,
                            eUtils.scaleInt(cGameLogic.userPlayer().getInt("coordy")
                                    - cVars.getInt("camy")-200) + polygonBase[1][1]*polygonSize,
                            eUtils.scaleInt(cGameLogic.userPlayer().getInt("coordy")
                                    - cVars.getInt("camy")-200) + polygonBase[1][2]*polygonSize}
            };
            g2.fillPolygon(new Polygon(polygon[0], polygon[1], polygon[0].length));
        }
        //playernames
        for(gPlayer p : eManager.currentMap.scene.players()) {
            cScripts.setFontNormal(g);
            g.drawString(p.get("name"), eUtils.scaleInt(p.getInt("coordx") - cVars.getInt("camx")),
                    eUtils.scaleInt(p.getInt("coordy") - cVars.getInt("camy")));
            int x = eUtils.scaleInt(p.getInt("coordx")-cVars.getInt("camx")
                    - p.getInt("dimw")/4);
            int y = eUtils.scaleInt(p.getInt("coordy")-cVars.getInt("camy") - p.getInt("dimh")/4);
            int w = eUtils.scaleInt(3*p.getInt("dimw")/2);
            int h = eUtils.scaleInt(3*p.getInt("dimh")/2);
            if(sVars.isOne("vfxenableflares") && p.isOne("flashlight"))
                dFlares.drawFlare(g2,x,y,w,h,1,new int[]{255,255,255,255},new int[4]);
        }
    }
}
