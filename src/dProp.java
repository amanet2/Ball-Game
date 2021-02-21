import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class dProp {
    public static void drawProps(Graphics2D g2) {
        if(eManager.currentMap.wasLoaded > 0) {
            for(gProp prop : eManager.currentMap.scene.props()) {
                if (prop.sprite != null) {
                    if (prop.isInt("code", gProps.TELEPORTER)) {
                        if (prop.getDouble("fv") < 2 * Math.PI) {
                            prop.putDouble("fv", prop.getDouble("fv")+0.1);
                        } else {
                            prop.putDouble("fv", 0.0);
                        }
                    }
                    AffineTransform backup = g2.getTransform();
                    AffineTransform a = g2.getTransform();
                    a.rotate(prop.getDouble("fv"),
                            eUtils.scaleInt(prop.getInt("coordx") - cVars.getInt("camx")
                                    + prop.getInt("dimw") / 2),
                            eUtils.scaleInt(prop.getInt("coordy") - cVars.getInt("camy")
                                    + prop.getInt("dimh") / 2)
                    );
                    g2.setTransform(a);
                    if(!(((cVars.getInt("gamemode") == cGameMode.CAPTURE_THE_FLAG
                            || cVars.getInt("gamemode") == cGameMode.FLAG_MASTER)
                            && prop.isInt("code", gProps.FLAGRED)
                            && (!cVars.isVal("flagmasterid", ""))) ||
                            (prop.isInt("code", gProps.POWERUP) && prop.isInt("int0", 0)))) {
                        if(prop.isInt("code", gProps.FLAGRED) && cVars.isInt("gamemode", cGameMode.KING_OF_FLAGS)) {
                            if(cGameLogic.userPlayer() != null
                                    && prop.isVal("str0", cGameLogic.userPlayer().get("id"))
                                    && !prop.get("sprite").contains("flag_blue"))
                                prop.setSpriteFromPath(eUtils.getPath("misc/flag_blue.png"));
                            else if(cGameLogic.userPlayer() != null
                                    && !prop.isVal("str0", cGameLogic.userPlayer().get("id"))
                                    && !prop.get("sprite").contains("flag_red"))
                                prop.setSpriteFromPath(eUtils.getPath("misc/flag_red.png"));
                        }
                        if(prop != null && prop.isInt("code", gProps.POWERUP) && prop.getInt("int0") > 0) {
                            //propshadow
                            if (cVars.getInt("mapview") == gMap.MAP_TOPVIEW) {
                                if(sVars.isOne("vfxenableshadows")) {
                                    Rectangle2D shadowBounds = new Rectangle.Double(
                                            eUtils.scaleInt(prop.getInt("coordx") - cVars.getInt("camx")),
                                            eUtils.scaleInt(prop.getInt("coordy") - cVars.getInt("camy")
                                                    + 3*gWeapons.fromCode(prop.getInt("int0")).dims[1]/4),
                                            eUtils.scaleInt(gWeapons.fromCode(prop.getInt("int0")).dims[0]),
                                            eUtils.scaleInt(gWeapons.fromCode(prop.getInt("int0")).dims[1])/2);
                                    if(shadowBounds.getWidth() > 0 && shadowBounds.getHeight() > 0) {
                                        RadialGradientPaint df = new RadialGradientPaint(
                                                shadowBounds, new float[]{0f, 1f},
                                                new Color[]{
                                                        new Color(0, 0, 0, cVars.getInt("vfxshadowalpha1")),
                                                        new Color(0, 0, 0, 0)
                                                }, MultipleGradientPaint.CycleMethod.NO_CYCLE);
                                        g2.setPaint(df);
                                        g2.fillRect((int) shadowBounds.getX(), (int) shadowBounds.getY(), (int) shadowBounds.getWidth(),
                                                (int) shadowBounds.getHeight());
                                    }
                                }
                            }
                            g2.drawImage(gWeapons.fromCode(prop.getInt("int0")).sprite,
                                    eUtils.scaleInt(prop.getInt("coordx") - cVars.getInt("camx")),
                                    eUtils.scaleInt(prop.getInt("coordy") - cVars.getInt("camy")),
                                    null);
                        }
                        else
                            g2.drawImage(prop.sprite,
                                    eUtils.scaleInt(prop.getInt("coordx") - cVars.getInt("camx")),
                                    eUtils.scaleInt(prop.getInt("coordy") - cVars.getInt("camy")),
                                    null
                            );
                    }
                    g2.setTransform(backup);
                }
                else {
                    switch (prop.getInt("code")) {
                        case gProps.BOOST:
                            drawBoostProp(g2, (gPropBoost) prop);
                            break;
                        default:
                            break;
                    }
                }
                if(sSettings.show_mapmaker_ui) {
                    switch(prop.getInt("code")) {
                        case gProps.POWERUP:
                            g2.setColor(new Color(255, 150, 80, 150));
                            g2.fillRect(eUtils.scaleInt(prop.getInt("coordx") - cVars.getInt("camx")),
                                    eUtils.scaleInt(prop.getInt("coordy") - cVars.getInt("camy")),
                                    eUtils.scaleInt(prop.getInt("dimw")),
                                    eUtils.scaleInt(prop.getInt("dimh")));
                            break;
                        case gProps.SCOREPOINT:
                            dFonts.setFontNormal(g2);
                            g2.drawString(Integer.toString(prop.getInt("tag")),
                                    eUtils.scaleInt(prop.getInt("coordx") + prop.getInt("dimw") / 2
                                            - cVars.getInt("camx")),
                                    eUtils.scaleInt(prop.getInt("coordy") + prop.getInt("dimh") / 2
                                            - cVars.getInt("camy")));
                            if (prop.getInt("int0") > 0) {
                                g2.setColor(new Color(80, 255, 180, 150));
                                g2.fillRect(eUtils.scaleInt(prop.getInt("coordx") - cVars.getInt("camx")),
                                        eUtils.scaleInt(prop.getInt("coordy") - cVars.getInt("camy")),
                                        eUtils.scaleInt(prop.getInt("dimw")),
                                        eUtils.scaleInt(prop.getInt("dimh")));
                            } else {
                                g2.setColor(new Color(255, 80, 80, 150));
                                g2.fillRect(eUtils.scaleInt(prop.getInt("coordx") - cVars.getInt("camx")),
                                        eUtils.scaleInt(prop.getInt("coordy") - cVars.getInt("camy")),
                                        eUtils.scaleInt(prop.getInt("dimw")),
                                        eUtils.scaleInt(prop.getInt("dimh")));
                            }
                            break;
                        case gProps.SPAWNPOINT:
                            g2.setColor(new Color(255, 100, 255, 100));
                            g2.fillRect(eUtils.scaleInt(prop.getInt("coordx")-cVars.getInt("camx")),
                                    eUtils.scaleInt(prop.getInt("coordy")-cVars.getInt("camy")),
                                    eUtils.scaleInt(prop.getInt("dimw")), eUtils.scaleInt(prop.getInt("dimh")));
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

    public static void drawBoostProp(Graphics2D g2, gPropBoost prop) {
        int[][][] polygonsBases = new int[][][]{
                new int[][]{ //up
                        new int[]{0,1,2},
                        new int[]{2,0,2}
                },
                new int[][]{ //down
                        new int[]{0,2,1},
                        new int[]{0,0,1}
                },
                new int[][]{ //left
                        new int[]{2,0,2},
                        new int[]{0,1,2}
                },
                new int[][]{ //right
                        new int[]{0,2,0},
                        new int[]{0,1,2}
                }

        };
        int polygonw = eUtils.scaleInt(prop.getInt("dimw"))/2;
        int polygonh = eUtils.scaleInt(prop.getInt("dimh"))/2;
        int velDir = prop.getInt("int0");
        int[][] usePolygon = velDir > -1 && velDir < polygonsBases.length ? polygonsBases[velDir] : null;
        if(usePolygon != null) {
            int[][] polygon = new int[][]{
                    new int[]{eUtils.scaleInt(prop.getInt("coordx")
                            - cVars.getInt("camx")) + usePolygon[0][0] * polygonw,
                            eUtils.scaleInt(prop.getInt("coordx")
                                    - cVars.getInt("camx")) + usePolygon[0][1] * polygonw,
                            eUtils.scaleInt(prop.getInt("coordx")
                                    - cVars.getInt("camx")) + usePolygon[0][2] * polygonw},
                    new int[]{eUtils.scaleInt(prop.getInt("coordy")
                            - cVars.getInt("camy")) + usePolygon[1][0] * polygonh,
                            eUtils.scaleInt(prop.getInt("coordy")
                                    - cVars.getInt("camy")) + usePolygon[1][1] * polygonh,
                            eUtils.scaleInt(prop.getInt("coordy")
                                    - cVars.getInt("camy")) + usePolygon[1][2] * polygonh}
            };
            Polygon pg = new Polygon(polygon[0], polygon[1], polygon[0].length);
            g2.setColor(new Color(20, 200, 220, 100));
            g2.fillPolygon(pg);
        }
    }
}
