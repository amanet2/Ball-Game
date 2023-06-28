import java.awt.*;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

public class dThings {
    public static void drawBlockWallsAndPlayers(Graphics2D g2, gScene scene) {
        Queue<gThing> visualQueue = scene.getWallsAndPlayersSortedByCoordY();
        while(visualQueue.size() > 0) {
            gThing thing = visualQueue.remove();
            if(thing.type.equals("THING_PLAYER"))
                ((gPlayer) thing).draw(g2);
            else if(thing.type.contains("ITEM_"))
                ((gItem) thing).draw(g2);
            else if(thing.type.contains("CUBE") && thing.wallh > 0) {
                drawShadowBlockFlat(g2, thing);
                g2.setPaint(xMain.shellLogic.wallTexture);
                g2.fillRect(thing.coords[0], thing.coords[1] + thing.toph, thing.dims[0], thing.wallh);
                drawBlockWallsShadingFlat(g2, thing);
                drawBlockTopCube(g2, thing);
            }
        }
    }

    public static void drawBlockWallsShadingFlat(Graphics2D g2, gThing block) {
        if (sSettings.vfxenableshading) {
            g2.setStroke(dFonts.thickStroke);
            if (block.wallh > 0) {
                GradientPaint gradient;
                if(block.wallh < 300) {
                    gradient = new GradientPaint(
                            block.coords[0] + block.dims[0] / 2,
                            block.coords[1] + block.toph,
                            gColors.getColorFromName("clrw_walllowshading1"),
                            block.coords[0] + block.dims[0] / 2,
                            block.coords[1] + block.dims[1],
                            gColors.getColorFromName("clrw_walllowshading2")
                    );
                }
                else {
                    gradient = new GradientPaint(
                            block.coords[0] + block.dims[0] / 2,
                            block.coords[1] + block.toph,
                            gColors.getColorFromName("clrw_wallshading1"),
                            block.coords[0] + block.dims[0] / 2,
                            block.coords[1] + block.dims[1],
                            gColors.getColorFromName("clrw_wallshading2")
                    );
                }
                g2.setPaint(gradient);
                g2.fillRect(block.coords[0], block.coords[1] + block.toph, block.dims[0],
                        block.wallh
                );
                if(block.wallh < 300) {
                    gradient = new GradientPaint(
                            block.coords[0] + block.dims[0] / 2,
                            block.coords[1] + block.toph,
                            gColors.getColorFromName("clrw_walllowoutline1"),
                            block.coords[0] + block.dims[0] / 2,
                            block.coords[1] + block.dims[1],
                            gColors.getColorFromName("clrw_walllowoutline2")
                    );
                }
                else {
                    gradient = new GradientPaint(
                            block.coords[0] + block.dims[0] / 2,
                            block.coords[1] + block.toph,
                            gColors.getColorFromName("clrw_walloutline1"),
                            block.coords[0] + block.dims[0] / 2,
                            block.coords[1] + block.dims[1],
                            gColors.getColorFromName("clrw_walloutline2")
                    );
                }
                g2.setPaint(gradient);
                g2.drawRoundRect(block.coords[0], block.coords[1] + block.toph,
                        block.dims[0], block.wallh, 5, 5
                );
            }
        }
    }

    public static void drawBlockFloors(Graphics2D g2, gScene scene) {
        ConcurrentHashMap<String, gThing> floorMap = scene.getThingMap("BLOCK_FLOOR");
        for(String tag : floorMap.keySet()) {
            gThing block = floorMap.get(tag);
            g2.setPaint(xMain.shellLogic.floorTexture);
            g2.fillRect(block.coords[0], block.coords[1], block.dims[0], block.dims[1]);
        }
    }

    public static void drawMapmakerPreviewBlockFloors(Graphics2D g2, gScene scene) {
        ConcurrentHashMap<String, gThing> floorMap = scene.getThingMap("BLOCK_FLOOR");
        for(String tag : floorMap.keySet()) {
            gThing block = floorMap.get(tag);
            dFonts.setFontColor(g2, "clrw_floorcolorpreview");
            g2.fillRect(eUtils.scaleInt(block.coords[0]/4),
                    eUtils.scaleInt(block.coords[1]/4),
                    eUtils.scaleInt(block.dims[0]/4),
                    eUtils.scaleInt(block.dims[1]/4)
            );
        }
    }

    public static void drawBlockTopCube(Graphics2D g2, gThing block) {
        g2.setPaint(xMain.shellLogic.topTexture);
        g2.fillRect(block.coords[0], block.coords[1], block.dims[0], block.toph);
        dFonts.setFontColor(g2, "clrw_topcolor");
        if(block.wallh > 0 && block.wallh < 300)
            dFonts.setFontColor(g2, "clrw_topcolordark");
        g2.fillRect(block.coords[0], block.coords[1], block.dims[0], block.toph);
        drawBlockTopShadingCube(g2, block);
    }

    public static void drawBlockTopCubesPreview(Graphics2D g2) {
        ConcurrentHashMap<String, gThing> squareMap = uiEditorMenus.previewScene.getThingMap("BLOCK_CUBE");
        for(String tag : squareMap.keySet()) {
            gThing block = squareMap.get(tag);
            if(block.wallh > 0) {
                dFonts.setFontColor(g2, "clrw_wallcolorpreview");
                g2.fillRect(eUtils.scaleInt(block.coords[0]/4),
                        eUtils.scaleInt(block.coords[1]/4+ block.toph/4),
                        eUtils.scaleInt(block.dims[0]/4),
                        eUtils.scaleInt(block.wallh/4)
                );
            }
        }
        for(String tag : squareMap.keySet()) {
            gThing block = squareMap.get(tag);
            if(block.toph > 0) {
                dFonts.setFontColor(g2, "clrw_topcolorpreview");
                g2.fillRect(
                        eUtils.scaleInt(block.coords[0]/4),
                        eUtils.scaleInt(block.coords[1]/4),
                        eUtils.scaleInt(block.dims[0]/4),
                        eUtils.scaleInt(block.toph/4)
                );
            }
        }
    }

    public static void drawBlockTopShadingCube(Graphics2D g2, gThing block) {
        if (sSettings.vfxenableshading) {
            g2.setStroke(dFonts.thickStroke);
            GradientPaint gradient = new GradientPaint(
                    block.coords[0] + block.dims[0] / 2
                    ,
                    block.coords[1] ,
                    gColors.getColorFromName("clrw_roofoutline1"),
                    block.coords[0] + block.dims[0] / 2
                    ,
                    block.coords[1]
                            + block.toph,
                    gColors.getColorFromName("clrw_roofoutline2")
            );
            g2.setPaint(gradient);
            if(block.wallh > 0 && block.wallh < 300)
                g2.fillRect(
                        block.coords[0] ,
                        block.coords[1] ,
                        block.dims[0],
                        block.toph
                );
            g2.drawRoundRect(
                    block.coords[0] ,
                    block.coords[1] ,
                    block.dims[0],
                    block.toph,
                    5,
                    5
            );
        }
    }

    public static void drawShadowBlockFlat(Graphics2D g2, gThing block) {
        if(sSettings.vfxenableshadows) {
            g2.setStroke(dFonts.thickStroke);
            GradientPaint gradient = new GradientPaint(
                    block.coords[0] + block.dims[0]/2,block.coords[1] + block.dims[1],
                    gColors.getColorFromName("clrw_shadow1"),
                    block.coords[0] + block.dims[0]/2,
                    block.coords[1] + block.dims[1] + (int)((block.wallh)*sSettings.vfxshadowfactor),
                    gColors.getColorFromName("clrw_shadow2")
            );
            g2.setPaint(gradient);
            g2.fillRect(
                    block.coords[0],
                    block.coords[1] + block.dims[1],
                    block.dims[0],
                    (int)(block.wallh*sSettings.vfxshadowfactor)
            );
        }
    }
}
