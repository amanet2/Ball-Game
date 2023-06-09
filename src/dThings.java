import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

public class dThings {
    public static void drawBlockWallsAndPlayers(Graphics2D g2, gScene scene) {
        Queue<gThing> visualQueue = scene.getWallsAndPlayersSortedByCoordY();
        while(visualQueue.size() > 0) {
            gThing thing = visualQueue.remove();
            if(thing.isVal("type", "THING_PLAYER"))
                drawPlayer(g2, (gPlayer) thing);
            else if(thing.get("type").contains("ITEM_"))
                drawItem(g2, (gItem) thing);
            else {
                if(thing.get("type").contains("CUBE")) {
                    if (thing.contains("wallh")) {
                        drawShadowBlockFlat(g2, (gBlock) thing);
                        if (thing.contains("wallh")) {
                            g2.setPaint(xMain.shellLogic.wallTexture);
                            g2.fillRect(thing.getX(), thing.getY() + thing.getInt("toph"),
                                    thing.getWidth(), thing.getInt("wallh")
                            );
                            drawBlockWallsShadingFlat(g2, thing);
                        }
                        drawBlockTopCube(g2, thing);
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

    public static void drawBlockFloors(Graphics2D g2, gScene scene) {
        ConcurrentHashMap<String, gThing> floorMap = scene.getThingMap("BLOCK_FLOOR");
        for(String tag : floorMap.keySet()) { //TODO: concurrent exception occured on this line
            gThing block = floorMap.get(tag);
            g2.setPaint(xMain.shellLogic.floorTexture);
            g2.fillRect(block.getInt("coordx"), block.getY(), block.getWidth(), block.getHeight());
        }
    }

    public static void drawMapmakerPreviewBlockFloors(Graphics2D g2, gScene scene) {
        ConcurrentHashMap<String, gThing> floorMap = scene.getThingMap("BLOCK_FLOOR");
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

    public static void drawBlockTopCube(Graphics2D g2, gThing block) {
        g2.setPaint(xMain.shellLogic.topTexture);
        g2.fillRect(block.getX(), block.getY(), block.getWidth(), block.getInt("toph"));
        dFonts.setFontColor(g2, "clrw_topcolor");
        if(block.contains("wallh") && block.getInt("wallh") < 300)
            dFonts.setFontColor(g2, "clrw_topcolordark");
        g2.fillRect(block.getX(), block.getY(), block.getWidth(), block.getInt("toph"));
        drawBlockTopShadingCube(g2, block);
    }

    public static void drawBlockTopCubesPreview(Graphics2D g2) {
        ConcurrentHashMap<String, gThing> squareMap = uiEditorMenus.previewScene.getThingMap("BLOCK_CUBE");
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

    public static void drawBlockTopShadingCube(Graphics2D g2, gThing block) {
        if (sSettings.vfxenableshading) {
            g2.setStroke(dFonts.thickStroke);
            GradientPaint gradient = new GradientPaint(
                    block.getInt("coordx") + block.getInt("dimw") / 2
                    ,
                    block.getInt("coordy") ,
                    gColors.getColorFromName("clrw_roofoutline1"),
                    block.getInt("coordx") + block.getInt("dimw") / 2
                    ,
                    block.getInt("coordy")
                            + block.getInt("toph"),
                    gColors.getColorFromName("clrw_roofoutline2")
            );
            g2.setPaint(gradient);
            if(block.contains("wallh") && block.getInt("wallh") < 300)
                g2.fillRect(
                        block.getInt("coordx") ,
                        block.getInt("coordy") ,
                        block.getInt("dimw"),
                        block.getInt("toph")
                );
            g2.drawRoundRect(
                    block.getInt("coordx") ,
                    block.getInt("coordy") ,
                    block.getInt("dimw"),
                    block.getInt("toph"),
                    5,
                    5
            );
        }
    }

    public static void drawShadowBlockFlat(Graphics2D g2, gBlock block) {
        if(sSettings.vfxenableshadows) {
            g2.setStroke(dFonts.thickStroke);
            if (block.getInt("wallh") + block.getInt("toph") == block.getHeight()) {
                GradientPaint gradient = new GradientPaint(
                        block.getX() + block.getWidth()/2,block.getY() + block.getHeight(),
                        gColors.getColorFromName("clrw_shadow1"),
                        block.getX() + block.getWidth()/2,
                        block.getY() + block.getHeight()
                                + (int)((block.getInt("wallh"))*sSettings.vfxshadowfactor),
                        gColors.getColorFromName("clrw_shadow2")
                );
                g2.setPaint(gradient);
                g2.fillRect(
                        block.getX(),
                        block.getY() + block.getHeight(),
                        block.getWidth(),
                        (int)(block.getInt("wallh")*sSettings.vfxshadowfactor)
                );
            }
            else if (block.getInt("toph") > 0) {
                GradientPaint gradient = new GradientPaint(
                        block.getX() + block.getWidth()/2,
                        block.getY() + block.getHeight() - block.getInt("toph"),
                        gColors.getColorFromName("clrw_shadow1"),
                        block.getX() + block.getWidth()/2,
                        block.getY() + block.getHeight()
                                + (int)((block.getHeight() - block.getInt("toph") - block.getInt("toph")
                        )*sSettings.vfxshadowfactor),
                        gColors.getColorFromName("clrw_shadow2")
                );
                g2.setPaint(gradient);
                g2.fillRect(block.getX(), block.getY() + block.getHeight() - block.getInt("toph"),
                        block.getWidth(), (int)((block.getHeight() - block.getInt("toph"))*sSettings.vfxshadowfactor)
                );
            }
        }
    }

    public static void drawThingShadow(Graphics2D g2, gThing thing) {
        if(sSettings.vfxenableshadows) {
            //check null fields
            if(!thing.containsFields(new String[]{"coordx", "coordy", "dimw", "dimh"}))
                return;
            int yadj = 5*thing.getInt("dimh")/6;
            Rectangle2D shadowBounds = new Rectangle.Double(
                    thing.getInt("coordx"),
                    thing.getInt("coordy") + yadj,
                    thing.getInt("dimw"),
                    (double)thing.getInt("dimh")/3);
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
        drawThingShadow(g2, player);
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
            g2.fillOval(player.getInt("coordx"), player.getInt("coordy"), player.getInt("dimw"), player.getInt("dimh"));
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
            drawThingShadow(g2, item);
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
            g2.fillRect(item.getInt("coordx"), item.getInt("coordy"), item.getInt("dimw"), item.getInt("dimh"));
        }
    }
}
