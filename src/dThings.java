import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;

public class dThings {
    public static void drawBlockFloors(Graphics2D g2) {
        g2.setPaint(xMain.shellLogic.floorTexture);
        while(xMain.shellLogic.drawFloorsQueue.size() > 0) {
            int[] dvars = xMain.shellLogic.drawFloorsQueue.remove();
            g2.fillRect(dvars[0], dvars[1], dvars[2], dvars[3]);
        }
    }

    public static void drawBlockWallsAndPlayersNew(Graphics2D g2) {
        while(xMain.shellLogic.drawWallsAndPlayersQueue.size() > 0) {
            dDrawPayload drawPayload = xMain.shellLogic.drawWallsAndPlayersQueue.remove();
            if(drawPayload.sprites.length > 0) {
                if(drawPayload.sprites[0] != null) { //player or object
                    //shadows
                    if(sSettings.vfxenableshadows && drawPayload.shadow) {
                            Rectangle2D shadowBounds = new Rectangle.Double(
                                    drawPayload.spriteDims[0],
                                    drawPayload.spriteDims[1] + (double) 5*drawPayload.spriteDims[3]/6,
                                    drawPayload.spriteDims[2],
                                    (double) drawPayload.spriteDims[3]/3
                                );
                            RadialGradientPaint df = new RadialGradientPaint(
                                    shadowBounds, new float[]{0f, 1f},
                                    new Color[]{
                                            gColors.getColorFromName("clrw_shadow1"),
                                            gColors.getColorFromName("clrw_clear")
                                    }, MultipleGradientPaint.CycleMethod.NO_CYCLE);
                            g2.setPaint(df);
                            g2.fillRect((int)shadowBounds.getX(), (int)shadowBounds.getY(), (int)shadowBounds.getWidth(),
                                    (int)shadowBounds.getHeight());
                    }
                    g2.drawImage(drawPayload.sprites[0], drawPayload.spriteDims[0], drawPayload.spriteDims[1], null);
                }
                else {
                    if(sSettings.vfxenableshading && drawPayload.isPlayerShading) {
                        GradientPaint df = new GradientPaint(
                                drawPayload.spriteDims[0],
                                drawPayload.spriteDims[1] + 2*drawPayload.spriteDims[3]/3,
                                gColors.getColorFromName("clrw_clear"),
                                drawPayload.spriteDims[0],
                                drawPayload.spriteDims[1] + drawPayload.spriteDims[3],
                                gColors.getColorFromName("clrw_shadow1half")
                        );
                        g2.setPaint(df);
                        g2.fillOval(
                                drawPayload.spriteDims[0],
                                drawPayload.spriteDims[1],
                                drawPayload.spriteDims[2],
                                drawPayload.spriteDims[3]
                        );
                    }
                    else if(drawPayload.sprites.length > 2 && drawPayload.sprites[2] == null) {
                        g2.setPaint(xMain.shellLogic.topTexture);
                        g2.fillRect(drawPayload.spriteDims[0], drawPayload.spriteDims[1], drawPayload.spriteDims[2], drawPayload.spriteDims[3]);
                        //top shading
                        if (sSettings.vfxenableshading) {
                            g2.setStroke(dFonts.thickStroke);
                            GradientPaint gradient = new GradientPaint(
                                    drawPayload.spriteDims[0] + drawPayload.spriteDims[2] / 2,
                                    drawPayload.spriteDims[1] ,
                                    gColors.getColorFromName("clrw_roofoutline1"),
                                    drawPayload.spriteDims[0] + drawPayload.spriteDims[2] / 2,
                                    drawPayload.spriteDims[1] + drawPayload.spriteDims[3],
                                    gColors.getColorFromName("clrw_roofoutline2")
                            );
                            g2.setPaint(gradient);
                            g2.drawRoundRect(
                                    drawPayload.spriteDims[0] ,
                                    drawPayload.spriteDims[1] ,
                                    drawPayload.spriteDims[2],
                                    drawPayload.spriteDims[3],
                                    5,
                                    5
                            );
                        }
                    }
                    else if(drawPayload.sprites.length > 1 && drawPayload.sprites[1] == null){
                        //block shadow
                        if(sSettings.vfxenableshadows) {
                            g2.setStroke(dFonts.thickStroke);
                            GradientPaint gradient = new GradientPaint(
                                    drawPayload.spriteDims[0] + drawPayload.spriteDims[2]/2,drawPayload.spriteDims[1] + drawPayload.spriteDims[3],
                                    gColors.getColorFromName("clrw_shadow1"),
                                    drawPayload.spriteDims[0] + drawPayload.spriteDims[2]/2,
                                    drawPayload.spriteDims[1] + drawPayload.spriteDims[3]
                                            + (int)((drawPayload.spriteDims[3])*sSettings.vfxshadowfactor),
                                    gColors.getColorFromName("clrw_shadow2")
                            );
                            g2.setPaint(gradient);
                            g2.fillRect(
                                    drawPayload.spriteDims[0],
                                    drawPayload.spriteDims[1] + drawPayload.spriteDims[3],
                                    drawPayload.spriteDims[2],
                                    (int)(drawPayload.spriteDims[3]*sSettings.vfxshadowfactor)
                            );
                        }
                        g2.setPaint(xMain.shellLogic.wallTexture);
                        g2.fillRect(drawPayload.spriteDims[0], drawPayload.spriteDims[1],
                                drawPayload.spriteDims[2], drawPayload.spriteDims[3]
                        );
                        //wall shading
                        if (sSettings.vfxenableshading) {
                            g2.setStroke(dFonts.thickStroke);
                            GradientPaint gradient;
                            if(drawPayload.spriteDims[3] < 300) {
                                gradient = new GradientPaint(
                                        drawPayload.spriteDims[0] + drawPayload.spriteDims[2] / 2,
                                        drawPayload.spriteDims[1],
                                        gColors.getColorFromName("clrw_walllowshading1"),
                                        drawPayload.spriteDims[0] + drawPayload.spriteDims[2] / 2,
                                        drawPayload.spriteDims[1] + drawPayload.spriteDims[3],
                                        gColors.getColorFromName("clrw_walllowshading2")
                                );
                            }
                            else {
                                gradient = new GradientPaint(
                                        drawPayload.spriteDims[0] + drawPayload.spriteDims[2] / 2,
                                        drawPayload.spriteDims[1],
                                        gColors.getColorFromName("clrw_wallshading1"),
                                        drawPayload.spriteDims[0] + drawPayload.spriteDims[2] / 2,
                                        drawPayload.spriteDims[1] + drawPayload.spriteDims[3],
                                        gColors.getColorFromName("clrw_wallshading2")
                                );
                            }
                            g2.setPaint(gradient);
                            g2.fillRect(drawPayload.spriteDims[0], drawPayload.spriteDims[1], drawPayload.spriteDims[2],
                                    drawPayload.spriteDims[3]
                            );
                            if(drawPayload.spriteDims[3] < 300) {
                                gradient = new GradientPaint(
                                        drawPayload.spriteDims[0] + drawPayload.spriteDims[2] / 2,
                                        drawPayload.spriteDims[1],
                                        gColors.getColorFromName("clrw_walllowoutline1"),
                                        drawPayload.spriteDims[0] + drawPayload.spriteDims[2] / 2,
                                        drawPayload.spriteDims[1] + drawPayload.spriteDims[3],
                                        gColors.getColorFromName("clrw_walllowoutline2")
                                );
                            }
                            else {
                                gradient = new GradientPaint(
                                        drawPayload.spriteDims[0] + drawPayload.spriteDims[2] / 2,
                                        drawPayload.spriteDims[1],
                                        gColors.getColorFromName("clrw_walloutline1"),
                                        drawPayload.spriteDims[0] + drawPayload.spriteDims[2] / 2,
                                        drawPayload.spriteDims[1] + drawPayload.spriteDims[3],
                                        gColors.getColorFromName("clrw_walloutline2")
                                );
                            }
                            g2.setPaint(gradient);
                            g2.drawRoundRect(drawPayload.spriteDims[0], drawPayload.spriteDims[1],
                                    drawPayload.spriteDims[2], drawPayload.spriteDims[3], 5, 5
                            );
                        }
                    }
                    else {
                        if(sSettings.show_mapmaker_ui){
                            dFonts.setFontColor(g2, "clrf_spawnpoint");
                            g2.fillRect(
                                    drawPayload.spriteDims[0],
                                    drawPayload.spriteDims[1],
                                    drawPayload.spriteDims[2],
                                    drawPayload.spriteDims[3]
                            );
                        }
                    }
                }
            }
        }
    }

    public static void drawMapmakerPreviewBlockFloors(Graphics2D g2, gScene scene) {
        HashMap<String, gThing> floorMap = scene.getThingMap("BLOCK_FLOOR");
        for(String tag : floorMap.keySet()) {
            gThing block = floorMap.get(tag);
            dFonts.setFontColor(g2, "clrw_floorcolorpreview");
            g2.fillRect(eUtils.scaleInt(block.getX()/4),
                    eUtils.scaleInt(block.getY()/4),
                    eUtils.scaleInt(block.getWidth()/4),
                    eUtils.scaleInt(block.getHeight()/4)
            );
        }
    }

    public static void drawBlockTopCubesPreview(Graphics2D g2) {
        HashMap<String, gThing> squareMap = uiEditorMenus.previewScene.getThingMap("BLOCK_CUBE");
        for(String tag : squareMap.keySet()) {
            gThing block = squareMap.get(tag);
            if(block.contains("wallh")) {
                dFonts.setFontColor(g2, "clrw_wallcolorpreview");
                g2.fillRect(eUtils.scaleInt(block.getX()/4),
                        eUtils.scaleInt(block.getY()/4+ block.getInt("toph")/4),
                        eUtils.scaleInt(block.getWidth()/4),
                        eUtils.scaleInt(block.getInt("wallh")/4)
                );
            }
        }
        for(String tag : squareMap.keySet()) {
            gThing block = squareMap.get(tag);
            if(block.contains("toph")) {
                dFonts.setFontColor(g2, "clrw_topcolorpreview");
                g2.fillRect(
                        eUtils.scaleInt(block.getX()/4),
                        eUtils.scaleInt(block.getY()/4),
                        eUtils.scaleInt(block.getWidth()/4),
                        eUtils.scaleInt(block.getInt("toph")/4)
                );
            }
        }
    }

    public static void drawPlayer(Graphics2D g2, gPlayer player) {
        //player glow
        if(player == null)
            return;
        if(!player.contains("id"))
            return;
        nStateMap clStateMap = new nStateMap(xMain.shellLogic.clientNetThread.clientStateSnapshot);
        nState cState = clStateMap.get(player.get("id"));
        if(cState == null)
            return;
        if(cState.contains("color")) {
            Color pc = gColors.getColorFromName("clrp_" + cState.get("color"));
            if (pc != null) {
                int x = player.getInt("coordx") - player.getInt("dimw") / 4;
                int y = player.getInt("coordy") - player.getInt("dimh") / 4;
                int w = 3 * player.getInt("dimw") / 2;
                int h = 3 * player.getInt("dimh") / 2;
                if (sSettings.vfxenableflares)
                    dScreenFX.drawFlareFromColor(g2, x, y, w, h, 1, pc, new Color(0, 0, 0, 0));
            }
        }
        //player shadow
//        drawThingShadow(g2, player);
        //player itself
        g2.drawImage(
                player.sprite,
                player.getInt("coordx"),
                player.getInt("coordy"),
                null
        );
        String decor = player.get("decorationsprite");
        if(!decor.equalsIgnoreCase("null")) {
            g2.drawImage(
                    gTextures.getGScaledImage(eManager.getPath(decor), 300, 300),
                    player.getInt("coordx"), player.getInt("coordy") - 2*player.getInt("dimh")/3,
                    null
            );
        }
        //shading
        if(sSettings.vfxenableshading) {
            GradientPaint df = new GradientPaint(
                    player.getInt("coordx"),
                    player.getInt("coordy") + 2*player.getInt("dimh")/3,
                    gColors.getColorFromName("clrw_clear"),
                    player.getInt("coordx"),
                    player.getInt("coordy") + player.getInt("dimh"),
                    gColors.getColorFromName("clrw_shadow1half")
            );
            g2.setPaint(df);
            g2.fillOval(
                    player.getInt("coordx"),
                    player.getInt("coordy"),
                    player.getInt("dimw"),
                    player.getInt("dimh")
            );
        }
        //player weapon
        AffineTransform backup = g2.getTransform();
        AffineTransform a = g2.getTransform();
        a.rotate(player.getDouble("fv")-Math.PI/2,
                player.getInt("coordx") + (float) player.getInt("dimw") / 2,
                player.getInt("coordy") + (float) player.getInt("dimh") / 2
        );
        g2.setTransform(a);
        int diff = gWeapons.fromCode(player.getInt("weapon")).dims[1] / 2;
        g2.drawImage(gWeapons.fromCode(player.getInt("weapon")).sprite,
                player.getInt("coordx") + player.getInt("dimw")/2,
                player.getInt("coordy") + player.getInt("dimh")/2 - diff,
                null);
        g2.setTransform(backup);
    }

    public static void drawItem(Graphics2D g2, gItem item) {
        if(item.sprite != null) {
            //item shadow
//            drawThingShadow(g2, item);
            g2.drawImage(item.sprite,
                    item.getInt("coordx"),
                    item.getInt("coordy"),
                    null
            );
            if(sSettings.vfxenableflares && !item.get("flare").equals("null")) {
                String[] flareToks = item.get("flare").split(":");
                int[] flareArgs = new int[] {
                        Integer.parseInt(flareToks[0]),
                        Integer.parseInt(flareToks[1]),
                        Integer.parseInt(flareToks[2]),
                        Integer.parseInt(flareToks[3])
                };
                dScreenFX.drawFlareFromColor(g2,
                        item.getInt("coordx") - item.getInt("dimw")/2,
                        item.getInt("coordy") - item.getInt("dimh")/2,
                        item.getInt("dimw")*2,
                        item.getInt("dimh")*2,
                        1, new Color(flareArgs[0], flareArgs[1], flareArgs[2], flareArgs[3]), new Color(0,0,0,0)
                );
            }
        }
        else if(sSettings.show_mapmaker_ui){
            dFonts.setFontColor(g2, "clrf_spawnpoint");
            g2.fillRect(
                    item.getInt("coordx"),
                    item.getInt("coordy"),
                    item.getInt("dimw"),
                    item.getInt("dimh"));
        }
    }
}
