import java.awt.*;

public class dTileFloors {
    public static void drawFloors(Graphics2D g2) {
        if(cVars.isOne("maploaded")) {
            dBlockFloors.drawBlockFloors(g2);
            for (gTile tile : eManager.currentMap.scene.tiles()) {
                if (tile.sprites[2] != null) {
                    g2.drawImage(tile.sprites[2],
                            eUtils.scaleInt(tile.getInt("coordx") - cVars.getInt("camx")),
                            eUtils.scaleInt(tile.getInt("coordy") - cVars.getInt("camy")),
                            null
                    );
                    if (sVars.isOne("vfxenableshading")) {
                        GradientPaint gradient = new GradientPaint(
                                eUtils.scaleInt(tile.getInt("coordx") + tile.getInt("dimw") / 2
                                        - cVars.getInt("camx")),
                                eUtils.scaleInt(tile.getInt("coordy") - cVars.getInt("camy")),
                                new Color(0, 0, 0, cVars.getInt("vfxfloorshadingalpha1")),
                                eUtils.scaleInt(tile.getInt("coordx") + tile.getInt("dimw") / 2 - cVars.getInt("camx")),
                                eUtils.scaleInt(tile.getInt("coordy") - cVars.getInt("camy") + tile.getInt("dimh")),
                                new Color(0, 0, 0, cVars.getInt("vfxfloorshadingalpha2")));
                        g2.setPaint(gradient);
                        g2.fillRect(eUtils.scaleInt(tile.getInt("coordx") - cVars.getInt("camx")),
                                eUtils.scaleInt(tile.getInt("coordy") - cVars.getInt("camy")),
                                eUtils.scaleInt(tile.getInt("dimw")),
                                eUtils.scaleInt(tile.getInt("dimh"))
                        );
                        g2.setStroke(dFonts.thickStroke);
                        gradient = new GradientPaint(
                                eUtils.scaleInt(tile.getInt("coordx") + tile.getInt("dimw") / 2
                                        - cVars.getInt("camx")),
                                eUtils.scaleInt(tile.getInt("coordy") - cVars.getInt("camy")),
                                new Color(0, 0, 0, cVars.getInt("vfxflooroutlinealpha1")),
                                eUtils.scaleInt(tile.getInt("coordx") + tile.getInt("dimw") / 2
                                        - cVars.getInt("camx")),
                                eUtils.scaleInt(tile.getInt("coordy") - cVars.getInt("camy")
                                        + tile.getInt("dimh")),
                                new Color(0, 0, 0, cVars.getInt("vfxflooroutlinealpha2")));
                        g2.setPaint(gradient);
                        g2.drawRoundRect(eUtils.scaleInt(tile.getInt("coordx") - cVars.getInt("camx")),
                                eUtils.scaleInt(tile.getInt("coordy") - cVars.getInt("camy")),
                                eUtils.scaleInt(tile.getInt("dimw")),
                                eUtils.scaleInt(tile.getInt("dimh")),
                                eUtils.scaleInt(5), eUtils.scaleInt(5)
                        );
                    }
                } else if (tile.getInt("dim2w") > 0 && tile.getInt("dim2h") > 0) {
                    g2.setColor(Color.LIGHT_GRAY);
                    g2.fillRect(eUtils.scaleInt(tile.getInt("coordx") - cVars.getInt("camx")),
                            eUtils.scaleInt(tile.getInt("coordy") - cVars.getInt("camy")),
                            eUtils.scaleInt(tile.getInt("dimw")),
                            eUtils.scaleInt(tile.getInt("dimh"))
                    );
                }
            }
        }
    }
}
