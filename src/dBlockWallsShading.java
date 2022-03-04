import java.awt.*;

public class dBlockWallsShading {
    public static void drawBlockWallsShadingFlat(Graphics2D g2, gBlock block) {
        if (sSettings.vfxenableshading) {
            g2.setStroke(dFonts.thickStroke);
            if (block.getInt("wallh") > 0) {
                GradientPaint gradient;
                if(block.getInt("wallh") < 300) {
                    gradient = new GradientPaint(
                            block.getInt("coordx") + block.getInt("dimw") / 2,
                            block.getInt("coordy") + block.getInt("toph"),
                            gColors.getWorldColorFromName("walllowshading1"),
                            block.getInt("coordx") + block.getInt("dimw") / 2,
                            block.getInt("coordy") + block.getInt("dimh"),
                            gColors.getWorldColorFromName("walllowshading2")
                    );
                }
                else {
                    gradient = new GradientPaint(
                            block.getInt("coordx") + block.getInt("dimw") / 2
                                    ,
                            block.getInt("coordy") 
                                    + block.getInt("toph"),
                            gColors.getWorldColorFromName("wallshading1"),
                            block.getInt("coordx") + block.getInt("dimw") / 2
                                    ,
                            block.getInt("coordy") 
                                    + block.getInt("dimh"),
                            gColors.getWorldColorFromName("wallshading2")
                    );
                }
                g2.setPaint(gradient);
                g2.fillRect(block.getInt("coordx") ,
                        block.getInt("coordy") 
                                + block.getInt("toph"),
                        block.getInt("dimw"),
                        block.getInt("wallh")
                );
                if(block.getInt("wallh") < 300) {
                    gradient = new GradientPaint(
                            block.getInt("coordx") + block.getInt("dimw") / 2
                                    ,
                            block.getInt("coordy") 
                                    + block.getInt("toph"),
                            gColors.getWorldColorFromName("walllowoutline1"),
                            block.getInt("coordx") + block.getInt("dimw") / 2
                                    ,
                            block.getInt("coordy") 
                                    + block.getInt("dimh"),
                            gColors.getWorldColorFromName("walllowoutline2")
                    );
                }
                else {
                    gradient = new GradientPaint(
                            block.getInt("coordx") + block.getInt("dimw") / 2
                                    ,
                            block.getInt("coordy") 
                                    + block.getInt("toph"),
                            gColors.getWorldColorFromName("walloutline1"),
                            block.getInt("coordx") + block.getInt("dimw") / 2
                                    ,
                            block.getInt("coordy") 
                                    + block.getInt("dimh"),
                            gColors.getWorldColorFromName("walloutline2")
                    );
                }
                g2.setPaint(gradient);
                g2.drawRoundRect(block.getInt("coordx"), block.getInt("coordy") + block.getInt("toph"),
                                 block.getInt("dimw"), block.getInt("wallh"), 5, 5
                );
            }
        }
    }
}
