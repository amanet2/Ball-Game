import java.awt.*;

public class dTileFloors {
    public static void drawFloors(Graphics2D g2) {
        g2.setColor(Color.YELLOW);
        if(sSettings.show_mapmaker_ui) {
            for(int i = -6000; i <= 6000; i+=300) {
                g2.drawLine(eUtils.scaleInt(-6000 - cVars.getInt("camx")),
                    eUtils.scaleInt(i - cVars.getInt("camy")),
                    eUtils.scaleInt(6000 - cVars.getInt("camx")),
                    eUtils.scaleInt(i - cVars.getInt("camy")));
                g2.drawLine(eUtils.scaleInt(i - cVars.getInt("camx")),
                    eUtils.scaleInt(-6000 - cVars.getInt("camy")),
                    eUtils.scaleInt(i - cVars.getInt("camx")),
                    eUtils.scaleInt(6000 - cVars.getInt("camy")));
            }
        }
        for(gThingTile tile : eManager.currentMap.scene.tiles()) {
            if(tile.sprites[2] != null) {
                g2.drawImage(tile.sprites[2],
                    eUtils.scaleInt(tile.getInt("coordx") - cVars.getInt("camx")),
                    eUtils.scaleInt(tile.getInt("coordy") - cVars.getInt("camy")),
                    null
                );
                if(sVars.isOne("vfxenableshading")) {
                    GradientPaint gradient = new GradientPaint(
                            eUtils.scaleInt(tile.getInt("coordx") + tile.getInt("dimw")/2
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
                    g2.setStroke(new BasicStroke(eUtils.scaleInt(16)));
                    gradient = new GradientPaint(
                            eUtils.scaleInt(tile.getInt("coordx") + tile.getInt("dimw")/2
                                    - cVars.getInt("camx")),
                            eUtils.scaleInt(tile.getInt("coordy") - cVars.getInt("camy")),
                            new Color(0,0,0, cVars.getInt("vfxflooroutlinealpha1")),
                            eUtils.scaleInt(tile.getInt("coordx") + tile.getInt("dimw")/2
                                    - cVars.getInt("camx")),
                            eUtils.scaleInt(tile.getInt("coordy") - cVars.getInt("camy")
                                    + tile.getInt("dimh")),
                            new Color(0,0,0, cVars.getInt("vfxflooroutlinealpha2")));
                    g2.setPaint(gradient);
                    g2.drawRoundRect(eUtils.scaleInt(tile.getInt("coordx") - cVars.getInt("camx")),
                            eUtils.scaleInt(tile.getInt("coordy") - cVars.getInt("camy")),
                            eUtils.scaleInt(tile.getInt("dimw")),
                            eUtils.scaleInt(tile.getInt("dimh")),
                            eUtils.scaleInt(5), eUtils.scaleInt(5)
                    );
                }
//                BufferedImage bi = new BufferedImage
//                        (tile.sprites[2].getWidth(null),
//                                tile.sprites[2].getHeight(null),
//                                BufferedImage.TYPE_INT_RGB);
//                Graphics bg = bi.getGraphics();
//                bg.drawImage(tile.sprites[2], 0, 0, null);
//                bg.dispose();
//                TexturePaint paint = new TexturePaint(bi,
//                        new Rectangle(eUtils.scaleInt(tile.getInt("coordx") - cVars.getInt("camx")),
//                                eUtils.scaleInt(tile.getInt("coordy") - cVars.getInt("camy")),
//                                300,
//                                300));
//                g2.setPaint(paint);
//                g2.fillRect(eUtils.scaleInt(tile.getInt("coordx") - cVars.getInt("camx")),
//                        eUtils.scaleInt(tile.getInt("coordy") - cVars.getInt("camy")),
//                        eUtils.scaleInt(tile.getInt("dim2w")),
//                        eUtils.scaleInt(tile.getInt("dim2h")));
//                //shading floor
//                g2.setColor(new Color(0,0,0,3*cVars.getInt("vfxshadowalpha")/4));
//                g2.fillRect(eUtils.scaleInt(tile.getInt("coordx") - cVars.getInt("camx")),
//                    eUtils.scaleInt(tile.getInt("coordy") - cVars.getInt("camy")),
//                    eUtils.scaleInt(tile.getInt("dim2w")),
//                    eUtils.scaleInt(tile.getInt("dim2h"))
//                );
            }
            else if(!tile.isOne("canspawn")){
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
