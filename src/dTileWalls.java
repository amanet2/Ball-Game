import java.awt.*;
import java.util.HashMap;

public class dTileWalls {
    public static void drawWalls(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        //two passes, first pass corners, second reg walls
        if(cVars.isOne("maploaded")) {
            HashMap<String, gThing> squareMap = eManager.currentMap.scene.getThingMap("BLOCK_CUBE");
            for(String tag : squareMap.keySet()) {
                gBlockCube block = (gBlockCube) squareMap.get(tag);
                if(block.contains("wallh")) {
                    String[] colorvals = block.get("colorwall").split(",");
                    g2.setColor(new Color(
                            Integer.parseInt(colorvals[0]),
                            Integer.parseInt(colorvals[1]),
                            Integer.parseInt(colorvals[2]),
                            Integer.parseInt(colorvals[3])
                    ));
                    g2.fillRect(eUtils.scaleInt(block.getInt("coordx") - cVars.getInt("camx")),
                            eUtils.scaleInt(block.getInt("coordy") - cVars.getInt("camy")
                                + block.getInt("toph")),
                            eUtils.scaleInt(block.getInt("dimw")),
                            eUtils.scaleInt(block.getInt("wallh"))
                    );
                }
            }
            for (gTile t : eManager.currentMap.scene.tiles()) {
                dShadows.drawTileShadows(g2, t);
                int d6w = t.getInt("dim6w");
                if (d6w == -2 || d6w == -3) { //BR and BL
                    g2.drawImage(t.spriteTW,
                            eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                            eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dimh")),
                            eUtils.scaleInt(t.getInt("dimw")), eUtils.scaleInt(150), null
                    );
                    if (sVars.isOne("vfxenableshading")) {
                        GradientPaint gradient = new GradientPaint(
                                eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw") / 2 - cVars.getInt("camx")),
                                eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dimh")),
                                new Color(0, 0, 0, cVars.getInt("vfxwallshadingalpha1")),
                                eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw") / 2 - cVars.getInt("camx")),
                                eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dimh") + 150),
                                new Color(0, 0, 0, cVars.getInt("vfxwallshadingalpha2")));
                        g2.setPaint(gradient);
                        g2.fillRect(eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                                eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dimh")),
                                eUtils.scaleInt(t.getInt("dimw")), eUtils.scaleInt(150)
                        );
                        g2.drawRoundRect(eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                                eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dimh")),
                                eUtils.scaleInt(t.getInt("dimw")), eUtils.scaleInt(150),
                                eUtils.scaleInt(5), eUtils.scaleInt(5)
                        );
                    }
                }
            }
            for (gTile t : eManager.currentMap.scene.tiles()) {
                int d6w = t.getInt("dim6w");
                if (t.sprites[1] != null) {
                    g2.drawImage(t.sprites[1],
                            eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                            eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")
                                    + t.getInt("dim0h")),
                            null
                    );
                } else {
                    g2.setColor(Color.LIGHT_GRAY);
                    g2.fillRect(eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                            eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")
                                    + t.getInt("dim0h")),
                            eUtils.scaleInt(t.getInt("dim1w")),
                            eUtils.scaleInt(t.getInt("dim1h"))
                    );
                }
                if (t.sprites[4] != null) {
                    g2.drawImage(t.sprites[4],
                            eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                            eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dimh")
                                    - t.getInt("dim4h")),
                            null
                    );
                } else {
                    g2.setColor(Color.LIGHT_GRAY);
                    g2.fillRect(eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                            eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dimh")
                                    - t.getInt("dim4h")),
                            eUtils.scaleInt(t.getInt("dim4w")),
                            eUtils.scaleInt(t.getInt("dim4h"))
                    );
                }
                if (d6w == -1) { //UR
                    g2.setStroke(new BasicStroke(eUtils.scaleInt(16)));
                    Polygon pw = new Polygon(
                            new int[]{
                                    eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                                    eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx") + t.getInt("dimw")),
                                    eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx") + t.getInt("dimw")),
                                    eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx"))
                            },
                            new int[]{
                                    eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                                    eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dimh")),
                                    eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dimh") + 150),
                                    eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + 150)
                            },
                            4);
                    g2.setColor(Color.LIGHT_GRAY);
                    g2.fillPolygon(pw);
//                    Shape b = g2.getClip();
//                    g2.setClip(pw);
//                    g2.drawImage(t.spriteTW,
//                            eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
//                            eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
//                            null
//                    );
                    //corner wall shading
                    if (sVars.isOne("vfxenableshading")) {
                        GradientPaint gradient = new GradientPaint(
                                eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw") / 2 - cVars.getInt("camx")),
                                eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                                new Color(0, 0, 0, cVars.getInt("vfxwallshadingalpha1")),
                                eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw") / 2 - cVars.getInt("camx")),
                                eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dimh") + 150),
                                new Color(0, 0, 0, cVars.getInt("vfxwallshadingalpha2")));
                        g2.setPaint(gradient);
                        g2.drawPolygon(pw);
                        g2.fillPolygon(pw);
//                        g2.fillRect(eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
//                                eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
//                                eUtils.scaleInt(t.getInt("dimw")),
//                                eUtils.scaleInt(t.getInt("dimh") + 150)
//                        );
                    }
//                    g2.setClip(b);
                    //corner wall shadows
                    if (sVars.isOne("vfxenableshadows")) {
                        Polygon ps = new Polygon(
                                new int[]{
                                        eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                                        eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx") + t.getInt("dimw")),
                                        eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx") + t.getInt("dimw")),
                                        eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx"))
                                },
                                new int[]{
                                        eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + 150),
                                        eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")
                                                + t.getInt("dimh") + 150),
                                        eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")
                                                + t.getInt("dimh") + 150 + (int) (150 * cVars.getDouble("vfxshadowfactor"))),
                                        eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + 150
                                                + (int) (150 * cVars.getDouble("vfxshadowfactor")))
                                },
                                4);
                        GradientPaint gradient = new GradientPaint(
                                eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx") + t.getInt("dimw")),
                                eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + 150),
                                new Color(0, 0, 0, cVars.getInt("vfxcornershadowalpha1")),
                                eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                                eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dimh")
                                        + 150),
                                new Color(0, 0, 0, cVars.getInt("vfxcornershadowalpha2"))
                        );
                        g2.setPaint(gradient);
                        g2.fillPolygon(ps);
                    }
                } else if (d6w == -4) { //UL
                    g2.setStroke(new BasicStroke(eUtils.scaleInt(16)));
                    Polygon pw = new Polygon(
                            new int[]{
                                    eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                                    eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx") + t.getInt("dimw")),
                                    eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx") + t.getInt("dimw")),
                                    eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx"))
                            },
                            new int[]{
                                    eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dimh")),
                                    eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                                    eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + 150),
                                    eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dimh") + 150),
                            },
                            4);
                    g2.setColor(Color.LIGHT_GRAY);
                    g2.fillPolygon(pw);
//                    Shape b = g2.getClip();
//                    g2.setClip(pw);
//                    g2.drawImage(t.spriteTW,
//                            eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
//                            eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
//                            null
//                    );
                    //corner wall shading
                    if (sVars.isOne("vfxenableshading")) {
                        GradientPaint gradient = new GradientPaint(
                                eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw") / 2 - cVars.getInt("camx")),
                                eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
                                new Color(0, 0, 0, cVars.getInt("vfxwallshadingalpha1")),
                                eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw") / 2 - cVars.getInt("camx")),
                                eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dimh") + 150),
                                new Color(0, 0, 0, cVars.getInt("vfxwallshadingalpha2")));
                        g2.setPaint(gradient);
                        g2.drawPolygon(pw);
                        g2.fillPolygon(pw);
//                        g2.fillRect(eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
//                                eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")),
//                                eUtils.scaleInt(t.getInt("dimw")),
//                                eUtils.scaleInt(t.getInt("dimh") + 150)
//                        );
                    }
//                    g2.setClip(b);
                    //corner wall shadows
                    if (sVars.isOne("vfxenableshadows")) {
                        Polygon ps = new Polygon(
                                new int[]{
                                        eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                                        eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx") + t.getInt("dimw")),
                                        eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx") + t.getInt("dimw")),
                                        eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx"))
                                },
                                new int[]{
                                        eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dimh") + 150),
                                        eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + 150),
                                        eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + 150
                                                + (int) (150 * cVars.getDouble("vfxshadowfactor"))),
                                        eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")
                                                + t.getInt("dimh") + 150 + +(int) (150 * cVars.getDouble("vfxshadowfactor"))),
                                },
                                4);
                        GradientPaint gradient = new GradientPaint(
                                eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                                eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + 150),
                                new Color(0, 0, 0, cVars.getInt("vfxcornershadowalpha1")),
                                eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx") + t.getInt("dimw")),
                                eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dimh")
                                        + 150),
                                new Color(0, 0, 0, cVars.getInt("vfxcornershadowalpha2"))
                        );
                        g2.setPaint(gradient);
                        g2.fillPolygon(ps);
                    }
                }
                g2.setStroke(new BasicStroke(eUtils.scaleInt(16)));
                g2.setColor(new Color(0, 0, 0, 255));
                if (sVars.isOne("vfxenableshading")) {
                    if (t.getInt("dim1h") > 0) {
                        GradientPaint gradient = new GradientPaint(
                                eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw") / 2 - cVars.getInt("camx")),
                                eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dim0h")),
                                new Color(0, 0, 0, cVars.getInt("vfxwallshadingalpha1")),
                                eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw") / 2 - cVars.getInt("camx")),
                                eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")
                                        + t.getInt("dim0h") + t.getInt("dim1h")),
                                new Color(0, 0, 0, cVars.getInt("vfxwallshadingalpha2")));
                        g2.setPaint(gradient);
                        g2.fillRect(eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                                eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dim0h")),
                                eUtils.scaleInt(t.getInt("dim1w")),
                                eUtils.scaleInt(t.getInt("dim1h"))
                        );
                        gradient = new GradientPaint(
                                eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw") / 2 - cVars.getInt("camx")),
                                eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dim0h")),
                                new Color(0, 0, 0, cVars.getInt("vfxwalloutlinealpha1")),
                                eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw") / 2 - cVars.getInt("camx")),
                                eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")
                                        + t.getInt("dim0h") + t.getInt("dim1h")),
                                new Color(0, 0, 0, cVars.getInt("vfxwalloutlinealpha2")));
                        g2.setPaint(gradient);
                        g2.drawRoundRect(eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                                eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy")
                                        + t.getInt("dim0h")),
                                eUtils.scaleInt(t.getInt("dim1w")),
                                eUtils.scaleInt(t.getInt("dim1h")),
                                eUtils.scaleInt(5), eUtils.scaleInt(5)
                        );
                    }
                    if (t.getInt("dim4h") > 0) {
                        GradientPaint gradient = new GradientPaint(
                                eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw") / 2 - cVars.getInt("camx")),
                                eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dimh")
                                        - t.getInt("dim4h")),
                                new Color(0, 0, 0, cVars.getInt("vfxwallshadingalpha1")),
                                eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw") / 2 - cVars.getInt("camx")),
                                eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dimh")),
                                new Color(0, 0, 0, cVars.getInt("vfxwallshadingalpha2")));
                        g2.setPaint(gradient);
                        g2.fillRect(eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                                eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dimh")
                                        - t.getInt("dim4h")),
                                eUtils.scaleInt(t.getInt("dim4w")),
                                eUtils.scaleInt(t.getInt("dim4h"))
                        );
                        gradient = new GradientPaint(
                                eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw") / 2 - cVars.getInt("camx")),
                                eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dimh")
                                        - t.getInt("dim4h")),
                                new Color(0, 0, 0, cVars.getInt("vfxwalloutlinealpha1")),
                                eUtils.scaleInt(t.getInt("coordx") + t.getInt("dimw") / 2 - cVars.getInt("camx")),
                                eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dimh")),
                                new Color(0, 0, 0, cVars.getInt("vfxwalloutlinealpha2")));
                        g2.setPaint(gradient);
                        g2.drawRoundRect(eUtils.scaleInt(t.getInt("coordx") - cVars.getInt("camx")),
                                eUtils.scaleInt(t.getInt("coordy") - cVars.getInt("camy") + t.getInt("dimh")
                                        - t.getInt("dim4h")),
                                eUtils.scaleInt(t.getInt("dim4w")),
                                eUtils.scaleInt(t.getInt("dim4h")),
                                eUtils.scaleInt(5), eUtils.scaleInt(5)
                        );
                    }
                }
            }
        }
    }
}
