import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.util.Queue;

public class dBlockWalls {
    public static void drawBlockWallsAndPlayers(Graphics2D g2, gScene scene) {
        Queue<gThing> visualQueue = scene.getWallsAndPlayersSortedByCoordY();
        while(visualQueue.size() > 0) {
            gThing thing = visualQueue.remove();
            if(thing.isVal("type", "THING_PLAYER"))
                dPlayer.drawPlayer(g2, (gPlayer) thing);
            else if(thing.get("type").contains("ITEM_"))
                dItems.drawItem(g2, (gItem) thing);
            else {
                if(thing.get("type").contains("CUBE")) {
                    if (thing.contains("wallh")) {
                        dBlockShadows.drawShadowBlockFlat(g2, (gBlock) thing);
                        if (thing.contains("wallh")) {
                            g2.setPaint(xMain.shellLogic.blockFactory.wallTexture);
                            g2.fillRect(thing.getX(), thing.getY() + thing.getInt("toph"),
                                    thing.getWidth(), thing.getInt("wallh")
                            );
                            drawBlockWallsShadingFlat(g2, thing);
                        }
                        dBlockTops.drawBlockTopCube(g2, thing);
                    }
                }
            }
        }
    }

    public static void drawBlockWallsShadingFlat(Graphics2D g2, gThing block) {
        if (sSettings.vfxenableshading) {
            g2.setStroke(dFonts.thickStroke);
            if (block.getInt("wallh") > 0) {
                GradientPaint gradient;
                if(block.getInt("wallh") < 300) {
                    gradient = new GradientPaint(
                            block.getX() + block.getWidth() / 2,
                            block.getY() + block.getInt("toph"),
                            gColors.getColorFromName("clrw_walllowshading1"),
                            block.getX() + block.getWidth() / 2,
                            block.getY() + block.getHeight(),
                            gColors.getColorFromName("clrw_walllowshading2")
                    );
                }
                else {
                    gradient = new GradientPaint(
                            block.getX() + block.getWidth() / 2,
                            block.getY() + block.getInt("toph"),
                            gColors.getColorFromName("clrw_wallshading1"),
                            block.getX() + block.getWidth() / 2,
                            block.getY() + block.getHeight(),
                            gColors.getColorFromName("clrw_wallshading2")
                    );
                }
                g2.setPaint(gradient);
                g2.fillRect(block.getX(), block.getY() + block.getInt("toph"), block.getWidth(),
                        block.getInt("wallh")
                );
                if(block.getInt("wallh") < 300) {
                    gradient = new GradientPaint(
                            block.getX() + block.getWidth() / 2,
                            block.getY() + block.getInt("toph"),
                            gColors.getColorFromName("clrw_walllowoutline1"),
                            block.getX() + block.getWidth() / 2,
                            block.getY() + block.getHeight(),
                            gColors.getColorFromName("clrw_walllowoutline2")
                    );
                }
                else {
                    gradient = new GradientPaint(
                            block.getX() + block.getWidth() / 2,
                            block.getY() + block.getInt("toph"),
                            gColors.getColorFromName("clrw_walloutline1"),
                            block.getX() + block.getWidth() / 2,
                            block.getY() + block.getHeight(),
                            gColors.getColorFromName("clrw_walloutline2")
                    );
                }
                g2.setPaint(gradient);
                g2.drawRoundRect(block.getX(), block.getY() + block.getInt("toph"),
                        block.getWidth(), block.getInt("wallh"), 5, 5
                );
            }
        }
    }
}
