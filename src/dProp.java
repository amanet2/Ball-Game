import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class dProp {
    public static void drawProps(Graphics2D g2)
    {
        for(gProp prop : eManager.currentMap.scene.props()) {
            if (prop.sprite != null) {
                if (prop.isInt("code", gProp.TELEPORTER)) {
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
                    && prop.isInt("code", gProp.FLAGRED)
                        && (!cVars.isVal("flagmasterid", ""))) ||
                        (prop.isInt("code", gProp.POWERUP) && prop.isInt("int0", 0)))) {
                    if(prop.isInt("code", gProp.FLAGRED) && cVars.isInt("gamemode", cGameMode.KING_OF_FLAGS)) {
                        if(prop.getInt("int0") > 0 && !prop.get("sprite").contains("flag_blue"))
                            prop.setSpriteFromPath(eUtils.getPath("misc/flag_blue.png"));
                        else if(prop.getInt("int0") < 1 && !prop.get("sprite").contains("flag_red"))
                            prop.setSpriteFromPath(eUtils.getPath("misc/flag_red.png"));
                    }
                    if(prop.isInt("code", gProp.POWERUP)) {
                        //prop shadow
                        if (cVars.getInt("maptype") == gMap.MAP_TOPVIEW) {
                            if(sVars.isOne("vfxenableshadows")) {
                                Rectangle2D shadowBounds = new Rectangle.Double(
                                    eUtils.scaleInt(prop.getInt("coordx") - cVars.getInt("camx")),
                                    eUtils.scaleInt(prop.getInt("coordy") - cVars.getInt("camy")
                                            + prop.getInt("dimh")/2),
                                    gWeapons.weapons_selection[prop.getInt("int0")].sprite.getWidth(null),
                                gWeapons.weapons_selection[prop.getInt("int0")].sprite.getHeight(null)/4.0);
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
                        g2.drawImage(gWeapons.weapons_selection[prop.getInt("int0")].sprite,
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
                if(sSettings.show_mapmaker_ui) {
                    if (prop.isInt("code", gProp.SAFEPOINT)) {
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
                    }
                    if (prop.isInt("code", gProp.POWERUP)) {
                        g2.setColor(new Color(255, 150, 80, 150));
                        g2.fillRect(eUtils.scaleInt(prop.getInt("coordx") - cVars.getInt("camx")),
                                eUtils.scaleInt(prop.getInt("coordy") - cVars.getInt("camy")),
                                eUtils.scaleInt(prop.getInt("dimw")),
                                eUtils.scaleInt(prop.getInt("dimh")));
                    }
                    if (prop.isInt("code", gProp.SCOREPOINT)) {
                        cScripts.setFontNormal(g2);
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
                    }
                }
            }
        }
    }
}
