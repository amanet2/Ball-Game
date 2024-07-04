import javax.swing.JPanel;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * JPanel
 * displays image on the screen
 */
public class dPanel extends JPanel {
    public void paintComponent(Graphics g){
        removeAll();
        Graphics2D g2v = (Graphics2D) g.create();
        Graphics2D g2u = (Graphics2D) g.create();
        long gameTime = sSettings.gameTime;
        drawFrame(g2v);
        drawFrameUI(g2u, gameTime);
        sSettings.frames++;
        g2v.dispose();
        g2u.dispose();
        g.dispose();
    }

    private void drawFrame(Graphics2D g2) {
        if(!sSettings.clientMapLoaded) // comment out for no loading screens
            return;
        g2.translate(sSettings.width / 2, sSettings.height / 2);
        g2.scale(sSettings.zoomLevel, sSettings.zoomLevel);
        g2.translate(-sSettings.width / 2, -sSettings.height / 2);
        gScene scene = xMain.shellLogic.clientScene;
        g2.scale(
                ((1.0 / sSettings.gamescale) * (double) sSettings.height),
                ((1.0 / sSettings.gamescale) * (double) sSettings.height)
        );
        g2.translate(-gCamera.coords[0], -gCamera.coords[1]);
//        synchronized (scene.objectMaps) {
            drawBlockFloors(g2, scene);
            drawBlockWallsAndPlayers(g2, scene);
            drawMapmakerOverlay(g2, scene);
            drawBulletsAndAnimations(g2, scene);
            drawWaypoints(g2, scene);
            drawPopups(g2, scene);
            drawUserPlayerArrow(g2);
            drawPlayerNames(g2);
            if (sSettings.show_mapmaker_ui)
                drawSelectionBoxes(g2);
//        }
    }

    private void drawFrameUI(Graphics2D g2, long gameTimeMillis) {
        drawScreenFX(g2);
        dScreenMessages.displayScreenMessages(g2, gameTimeMillis);
        if(!sSettings.inplay && sSettings.show_mapmaker_ui && sSettings.clientMapLoaded) {
            drawBlockFloorsPreview(g2, xMain.shellLogic.clientPreviewScene);
            drawBlockCubesPreview(g2, xMain.shellLogic.clientPreviewScene);
        }
    }

    private void drawScreenFX(Graphics2D g2) {
        if(!sSettings.IS_CLIENT)
            return;
        nState userState = new nStateMap(xMain.shellLogic.clientNetThread.clientStateSnapshot).get(sSettings.uuid);
        if(userState == null)
            return;
        // health overlay
        int userhp = Math.max(Integer.parseInt(userState.get("hp")), 0);
        if (userhp < sSettings.clientMaxHP) {
            int maxAlpha = 255;
            Color color = gColors.getColorFromName("clrp_" + xMain.shellLogic.clientVars.get("playercolor"));
            RadialGradientPaint rgp = new RadialGradientPaint(new Rectangle.Double(0, 0, sSettings.width, sSettings.height),
                    new float[]{0f, 1f}, new Color[]{new Color(0,0,0,0), new Color(color.getRed(), color.getGreen(), color.getBlue(), maxAlpha
                    - maxAlpha * userhp / sSettings.clientMaxHP)}, MultipleGradientPaint.CycleMethod.NO_CYCLE);
            g2.setPaint(rgp);
            g2.fillRect(0,0, sSettings.width, sSettings.height);
        }
    }

    private void drawFlareFromColor(Graphics2D g2, int x, int y, int w, int h, int mode, Color c1, Color c2) {
        RadialGradientPaint df = new RadialGradientPaint(new Point(x + w/2, y + h/2),
                mode == 1 ? Math.max(w/2, h/2) : Math.min(w/2, h/2),
                new float[]{0f, 1f}, new Color[]{c1, c2}
        );
        g2.setPaint(df);
        g2.fillRect(x, y, w, h);
    }

    private void drawMapmakerOverlay(Graphics2D g2, gScene scene) {
        //draw the grid OVER everything
        if(sSettings.drawmapmakergrid) {
            dFonts.setFontColor(g2, "clrf_mapmakergrid");
            g2.setStroke(dFonts.defaultStroke);
            for(int i = -12000; i <= 12000; i+=300) {
                g2.drawLine(-12000, i, 12000, i);
                g2.drawLine(i, -12000, i, 12000);
            }
        }
        //draw hitboxes
        if(sSettings.drawhitboxes) {
            for(String id : scene.getThingMap("BLOCK_COLLISION").keySet()) {
                gBlockCollision collision = (gBlockCollision) scene.getThingMap("BLOCK_COLLISION").get(id);
                if(collision.isOnScreen())
                    drawCollision(g2, collision);
            }
            for(String id : scene.getThingMap("THING_PLAYER").keySet()) {
                gThing player = scene.getThingMap("THING_PLAYER").get(id);
                if(player.isOnScreen()) {
                    g2.setColor(Color.WHITE);
                    g2.drawRect(player.coords[0], player.coords[1], player.dims[0], player.dims[1]);
                }
            }
        }
    }

    private void drawBulletsAndAnimations(Graphics2D g2, gScene scene) {
        ConcurrentHashMap<String, gThing> bulletsMap = scene.getThingMap("THING_BULLET");
        for (String id : bulletsMap.keySet()) {
            gThing t = bulletsMap.get(id);
            if(t.isOnScreen())
                g2.drawImage(t.sprite, t.coords[0], t.coords[1], null);
        }
        if(!sSettings.vfxenableanimations)
            return;
        ConcurrentHashMap<String, gThing> animationsMap = scene.getThingMap("THING_ANIMATION");
        long gameTimeMillis = sSettings.gameTime;
        for(String id : animationsMap.keySet()) {
            gAnimation emit = (gAnimation) animationsMap.get(id);
            if(emit.frame < gAnimations.animation_selection[emit.code].frames.length) {
                if (gAnimations.animation_selection[emit.code].frames[emit.frame] != null) {
                    if(emit.isOnScreen())
                        g2.drawImage(gAnimations.animation_selection[emit.code].frames[emit.frame],
                                emit.coords[0], emit.coords[1], null);
                    if(emit.frametime < gameTimeMillis) {
                        emit.frame = emit.frame + 1;
                        emit.frametime = gameTimeMillis + 30;
                    }
                }
            }
            else {
                xMain.shellLogic.scheduledEvents.put(
                        Long.toString(sSettings.gameTime + 500), new gDoable() {
                            public void doCommand() {
                                xMain.shellLogic.clientScene.getThingMap("THING_ANIMATION").remove(id);
                            }
                        }
                );
            }
        }
    }

    private void drawWaypoints(Graphics2D g2, gScene scene) {
        if(sSettings.inplay) {
            // players
            for(String id : scene.getThingMapIds("THING_PLAYER")) {
                if(id.equals(sSettings.uuid))
                    continue;
                gPlayer wpPlayer = scene.getPlayerById(id);
                if(wpPlayer == null)
                    continue;
                if(!(wpPlayer.waypoint.equals("null") || wpPlayer.waypoint.equals("0")))
                    drawThingWaypoint(g2, wpPlayer);
            }
            // items
            String[] itemIds = scene.getThingMapIds("THING_ITEM");
            for(String id : itemIds) {
                gThing item = scene.getThingMap("THING_ITEM").get(id);
                if(item == null)
                    continue;
                if(!(item.waypoint.equals("null") || item.waypoint.equals("0")))
                    drawThingWaypoint(g2, item);
            }
        }
    }

    private void drawThingWaypoint(Graphics2D g2, gThing thing) {
        if(thing.isOnScreen())
            drawNavPointerMapMarker(
                    g2,
                    thing.coords[0] + thing.dims[0]/2,
                    thing.coords[1] + thing.dims[1]/2,
                    thing.waypoint
            );
        drawNavPointerHUDArrow(
                g2,
                thing.coords[0] + thing.dims[0]/2,
                thing.coords[1] + thing.dims[1]/2,
                thing.waypoint
        );
    }

    private void drawNavPointerMapMarker(Graphics2D g2, int dx, int dy, String message) {
        int[][] polygondims = new int[][]{
                new int[]{dx - eUtils.unscaleInt(sSettings.height / 16), dx,
                        dx + eUtils.unscaleInt(sSettings.height / 16), dx
                },
                new int[]{dy, dy - eUtils.unscaleInt(sSettings.height / 16),
                        dy, dy + eUtils.unscaleInt(sSettings.height / 16)
                }
        };
        g2.translate(3,3);
        g2.setColor(Color.BLACK);
        g2.fillPolygon(polygondims[0], polygondims[1], 4);
        g2.translate(-3,-3);
        g2.setColor(gColors.getColorFromName("clrp_" + xMain.shellLogic.clientVars.get("playercolor")));
        g2.fillPolygon(polygondims[0], polygondims[1], 4);
        dFonts.setFontGNormal(g2);
        dFonts.drawCenteredString(g2, message, dx, dy);
    }

    private void drawNavPointerHUDArrow(Graphics2D g2, int dx, int dy, String message) {
        if(sSettings.inplay && xMain.shellLogic.getUserPlayer() != null) {
            double[] deltas = new double[]{
                    dx - xMain.shellLogic.getUserPlayer().coords[0]
                            + xMain.shellLogic.getUserPlayer().dims[0]/2,
                    dy - xMain.shellLogic.getUserPlayer().coords[1]
                            + xMain.shellLogic.getUserPlayer().dims[1]/2
            };
            //map marker
            drawNavPointerMapMarker(g2, dx, dy, message);
            //hud arrow
            AffineTransform backup = g2.getTransform();
            g2.translate(gCamera.coords[0], gCamera.coords[1]);
            double angle = Math.atan2(deltas[1], deltas[0]);
            if (angle < 0)
                angle += 2 * Math.PI;
            angle += Math.PI / 2;
            AffineTransform a = g2.getTransform();
            a.rotate(angle,
                    eUtils.unscaleInt((int)((double)sSettings.width / 2)),
                    eUtils.unscaleInt((int)((double)sSettings.height / 2))
            );
            g2.setTransform(a);
            int[][] arrowpolygon = new int[][]{
                    new int[]{
                            eUtils.unscaleInt(sSettings.width / 2 - sSettings.width / 54),
                            eUtils.unscaleInt(sSettings.width / 2 + sSettings.width / 54),
                            eUtils.unscaleInt(sSettings.width / 2)
                    },
                    new int[]{
                            eUtils.unscaleInt(sSettings.height / 12),
                            eUtils.unscaleInt(sSettings.height / 12),
                            eUtils.unscaleInt(0)
                    }
            };
            g2.translate(3,3);
            g2.setColor(Color.BLACK);
            g2.fillPolygon(arrowpolygon[0], arrowpolygon[1], 3);
            g2.translate(-3,-3);
            g2.setColor(gColors.getColorFromName("clrp_" + xMain.shellLogic.clientVars.get("playercolor")));
            g2.fillPolygon(arrowpolygon[0], arrowpolygon[1], 3);
            g2.translate(-gCamera.coords[0], -gCamera.coords[1]);
            g2.setTransform(backup);
        }
    }

    private void drawCollision(Graphics2D g2, gBlockCollision collision) {
        g2.setColor(Color.WHITE);
        g2.drawRect(collision.coords[0], collision.coords[1], collision.dims[0], collision.dims[1]);
    }

    private void drawBlockWallsAndPlayers(Graphics2D g2, gScene scene) {
        Queue<gThing> visualQueue = scene.getWallsAndPlayersSortedByCoordY();
        while(visualQueue.size() > 0) {
            gThing thing = visualQueue.remove();
            if (thing.type.equals("THING_PLAYER"))
                drawPlayer(g2, (gPlayer) thing);
            else if (thing.type.startsWith("ITEM_"))
                drawItem(g2, (gItem) thing);
            else if (thing.type.contains("CUBE"))
                drawCube(g2, (gBlockCube) thing);
        }
        for(String tag : scene.getThingMap("BLOCK_FLOOR").keySet()) {
            gThing floor = scene.getThingMap("BLOCK_FLOOR").get(tag);
            //flashlight
            if(xMain.shellLogic.getUserPlayer() != null) {
                int aimerx = eUtils.unscaleInt(xMain.shellLogic.getMouseCoordinates()[0]);
                int aimery = eUtils.unscaleInt(xMain.shellLogic.getMouseCoordinates()[1]);
                int snapX = aimerx + (int) gCamera.coords[0];
                int snapY = aimery + (int) gCamera.coords[1];
                RadialGradientPaint df = new RadialGradientPaint(new Point(snapX, snapY), 600,
                        new float[]{0f, 1f}, new Color[]{new Color(0,0,0,0), new Color(0, 0, 0,255 - (int) (255*(xMain.shellLogic.clientScene.brightnessLevel*0.01)))}
                );
                g2.setPaint(df);
                g2.fillRect(floor.coords[0], floor.coords[1], floor.dims[0], floor.dims[1]);
            }
            else {
                g2.setColor(new Color(0, 0, 0,255 - (int) (255*(xMain.shellLogic.clientScene.brightnessLevel*0.01))));
                g2.fillRect(floor.coords[0], floor.coords[1], floor.dims[0], floor.dims[1]);
            }
        }
    }

    private void drawCube(Graphics2D g2, gBlockCube cube) {
        //floor shadow
        if(sSettings.vfxenableshadows) {
            g2.setPaint(cube.shadowOverlay);
            g2.fillRect(
                    cube.coords[0], cube.coords[1] + cube.dims[1], cube.dims[0], (int)(cube.wallh*sSettings.vfxshadowfactor)
            );
        }
        g2.setPaint(xMain.shellLogic.wallTextures[sSettings.mapTheme]);
        g2.fillRect(cube.coords[0], cube.coords[1] + cube.toph, cube.dims[0], cube.wallh);
        if (sSettings.vfxenableshading) {
            if (cube.wallh > 0) {
                g2.setPaint(cube.shadingOverlay);
                g2.fillRect(cube.coords[0], cube.coords[1] + cube.toph, cube.dims[0], cube.wallh);
            }
        }
        g2.setPaint(xMain.shellLogic.topTextures[sSettings.mapTheme]);
        g2.fillRect(cube.coords[0], cube.coords[1], cube.dims[0], cube.toph);
        if(!sSettings.vfxenableshading)
            return;
        if(cube.wallh > 0 && cube.wallh < 300) {
            g2.setColor(gColors.getColorFromName("clrw_wallshading1"));
            g2.fillRect(cube.coords[0], cube.coords[1], cube.dims[0], cube.toph);
        }
    }

    private void drawItem(Graphics2D g2, gItem item) {
        if(item.sprite != null) {
            //item shadow
            item.drawRoundShadow(g2);
            g2.drawImage(item.sprite, item.coords[0], item.coords[1], null);
            if(sSettings.vfxenableflares && !item.flare.equals("null")) {
                String[] flareToks = item.flare.split(":");
                int[] flareArgs = new int[] {
                        Integer.parseInt(flareToks[0]), Integer.parseInt(flareToks[1]),
                        Integer.parseInt(flareToks[2]), Integer.parseInt(flareToks[3])
                };
                drawFlareFromColor(g2,
                        item.coords[0] - item.dims[0]/2, item.coords[1] - item.dims[1]/2, item.dims[0]*2, item.dims[1]*2, 1,
                        new Color(flareArgs[0], flareArgs[1], flareArgs[2], flareArgs[3]),
                        new Color(0,0,0,0)
                );
            }
        }
        else if(sSettings.show_mapmaker_ui){
            dFonts.setFontColor(g2, "clrf_spawnpoint");
            g2.fillRect(item.coords[0], item.coords[1], item.dims[0], item.dims[1]);
            dFonts.setFontSmall(g2);
            g2.setColor(Color.BLACK);
            g2.drawString(item.type, item.coords[0], item.coords[1] + item.dims[1]/2);
        }
    }

    private void drawPlayer(Graphics2D g2, gPlayer player) {
        Color pc = gColors.getColorFromName("clrp_" + player.color);
        if (pc != null) {
            int x = player.coords[0] - player.dims[0] / 4;
            int y = player.coords[1] - player.dims[1] / 4;
            int w = 3 * player.dims[0] / 2;
            int h = 3 * player.dims[1] / 2;
            if (sSettings.vfxenableflares)
                drawFlareFromColor(g2, x, y, w, h, 1, pc, new Color(0, 0, 0, 0));
        }
        //player shadow
        player.drawRoundShadow(g2);
        //player itself
        g2.drawImage(
                player.sprite,
                player.coords[0],
                player.coords[1],
                null
        );
        if(!player.decorationSprite.equalsIgnoreCase("null")) {
            g2.drawImage(
                    gTextures.getGScaledImage(eManager.getPath(player.decorationSprite), 300, 300),
                    player.coords[0], player.coords[1] - 2*player.dims[1]/3,
                    null
            );
        }
        //shading
        if(sSettings.vfxenableshading) {
            GradientPaint df = new GradientPaint(
                    player.coords[0], player.coords[1] + 2*player.dims[1]/3, gColors.getColorFromName("clrw_clear"),
                    player.coords[0], player.coords[1] + player.dims[1], gColors.getColorFromName("clrw_shadow1half")
            );
            g2.setPaint(df);
            g2.fillOval(player.coords[0], player.coords[1], player.dims[0], player.dims[1]);
        }
        //player weapon
        AffineTransform backup = g2.getTransform();
        AffineTransform a = g2.getTransform();
        a.rotate(player.fv - Math.PI/2, player.coords[0] + (float) player.dims[0] / 2, player.coords[1] + (float) player.dims[1] / 2);
        g2.setTransform(a);
        int diff = gWeapons.fromCode(player.weapon).dims[1] / 2;
        g2.drawImage(
                gWeapons.fromCode(player.weapon).sprite,
                player.coords[0] + player.dims[0]/2,
                player.coords[1] + player.dims[1]/2 - diff,
                null
        );
        g2.setTransform(backup);
    }

    private void drawBlockFloors(Graphics2D g2, gScene scene) {
        for(String tag : scene.getThingMap("BLOCK_FLOOR").keySet()) {
            gThing floor = scene.getThingMap("BLOCK_FLOOR").get(tag);
            //on-screen check
            if(floor.isOnScreen()) {
                g2.setPaint(xMain.shellLogic.floorTextures[sSettings.mapTheme]);
                g2.fillRect(floor.coords[0], floor.coords[1], floor.dims[0], floor.dims[1]);
                if (!sSettings.vfxenableshading)
                    continue;
                g2.setColor(gColors.getColorFromName("clrw_floorshading"));
                g2.fillRect(floor.coords[0], floor.coords[1], floor.dims[0], floor.dims[1]);
            }
        }
    }

    private void drawBlockFloorsPreview(Graphics2D g2, gScene scene) {
        for(String tag : scene.getThingMap("BLOCK_FLOOR").keySet()) {
            gThing floor = scene.getThingMap("BLOCK_FLOOR").get(tag);
            dFonts.setFontColor(g2, "clrw_floorcolorpreview");
            g2.fillRect(
                    eUtils.scaleInt(floor.coords[0]/4), eUtils.scaleInt(floor.coords[1]/4),
                    eUtils.scaleInt(floor.dims[0]/4), eUtils.scaleInt(floor.dims[1]/4)
            );
        }
    }
    
    private void drawBlockCubePreview(Graphics2D g2, gBlockCube cube) {
        dFonts.setFontColor(g2, "clrw_wallcolorpreview");
        g2.fillRect(
                eUtils.scaleInt(cube.coords[0]/4), eUtils.scaleInt(cube.coords[1]/4+ cube.toph/4),
                eUtils.scaleInt(cube.dims[0]/4), eUtils.scaleInt(cube.wallh/4)
        );
        dFonts.setFontColor(g2, "clrw_topcolorpreview");
        g2.fillRect(
                eUtils.scaleInt(cube.coords[0]/4), eUtils.scaleInt(cube.coords[1]/4),
                eUtils.scaleInt(cube.dims[0]/4), eUtils.scaleInt(cube.toph/4)
        );
    }

    private void drawBlockCubesPreview(Graphics2D g2, gScene scene) {
        for(String tag : scene.getThingMap("BLOCK_CUBE").keySet()) {
            drawBlockCubePreview(g2, (gBlockCube) scene.getThingMap("BLOCK_CUBE").get(tag));
        }
    }

    private void drawPopup(Graphics2D g2, gPopup popup) {
        if(!popup.isOnScreen())
            return;
        // look for hashtag color codes here
        StringBuilder ts = new StringBuilder();
        for(String word : popup.text.split(" ")) {
            if(word.contains("#")) {
                if(word.split("#").length != 2)
                    ts.append(word).append(" ");
                else if(gColors.getColorFromName("clrp_" + word.split("#")[1].replace(":","")) != null){
                    g2.setColor(Color.BLACK);
                    g2.drawString(
                            word.split("#")[0]+" ",
                            popup.coords[0] + dFonts.getStringWidth(g2, ts.toString())+3,
                            popup.coords[1] + 3
                    );
                    g2.setColor(gColors.getColorFromName("clrp_" + word.split("#")[1].replace(":","")));
                    g2.drawString(word.split("#")[0]+" ",
                            popup.coords[0] + dFonts.getStringWidth(g2, ts.toString()),
                            popup.coords[1]);
                    dFonts.setFontColor(g2, "clrf_normal");
                    ts.append(word.split("#")[0]).append(word.contains(":") ? ": " : " ");
                    continue;
                }
            }
            g2.setColor(Color.BLACK);
            g2.drawString(
                    word.split("#")[0]+" ",
                    popup.coords[0] + dFonts.getStringWidth(g2, ts.toString())+3,
                    popup.coords[1] + 3
            );
            dFonts.setFontColor(g2, "clrf_normal");
            g2.drawString(
                    word.split("#")[0]+" ",
                    popup.coords[0] + dFonts.getStringWidth(g2, ts.toString()),
                    popup.coords[1]
            );
            ts.append(word).append(" ");
        }
    }

    private void drawPopups(Graphics2D g2, gScene scene) {
        dFonts.setFontGNormal(g2);
        for(String id : scene.getThingMap("THING_POPUP").keySet()) {
            drawPopup(g2, (gPopup)scene.getThingMap("THING_POPUP").get(id));
        }
    }
    
    private void drawPlayerNames(Graphics2D g2) {
        nStateMap clStateMap = new nStateMap(xMain.shellLogic.clientNetThread.clientStateSnapshot);
        for(String id : clStateMap.keys()) {
            gPlayer p = xMain.shellLogic.getPlayerById(id);
            if(p == null || !p.isOnScreen())
                continue;
            nState clState = clStateMap.get(id);
            dFonts.setFontGNormal(g2);
            String name = clState.get("name");
            int coordx = p.coords[0];
            int coordy = p.coords[1];
            String ck = clState.get("color");
            Color color = gColors.getColorFromName("clrp_" + ck);
            g2.setColor(Color.BLACK);
            g2.drawString(name,coordx + p.dims[0]/2 - (int)g2.getFont().getStringBounds(name, dFonts.fontrendercontext).getWidth()/2 + 3, coordy + 3);
            g2.setColor(color);
            g2.drawString(name,coordx + p.dims[0]/2 - (int)g2.getFont().getStringBounds(name, dFonts.fontrendercontext).getWidth()/2, coordy);
            int[] bounds = {
                    coordx + p.dims[0]/2-(int)g2.getFont().getStringBounds(name, dFonts.fontrendercontext).getWidth()/2
                            - eUtils.unscaleInt(5*sSettings.height/128),
                    coordy - eUtils.unscaleInt(sSettings.height/32),
                    eUtils.unscaleInt(sSettings.height/32),
                    eUtils.unscaleInt(sSettings.height/32)
            };
            g2.setColor(Color.BLACK);
            g2.fillOval(bounds[0]+3, bounds[1]+3, bounds[2], bounds[3]);
            g2.setColor(color);
            g2.fillOval(bounds[0], bounds[1], bounds[2], bounds[3]);
        }
    }

    private Polygon getPolygon(int midx, int coordy) {
        int[][] polygonBase = new int[][]{new int[]{1,1,1}, new int[]{0,0,1}};
        int polygonSize = eUtils.unscaleInt(sSettings.width/32);
        int[][] polygon = new int[][]{
                new int[]{midx - polygonBase[0][0]*polygonSize, midx + polygonBase[0][1]*polygonSize, midx},
                new int[]{coordy + polygonBase[1][0]*polygonSize, coordy + polygonBase[1][1]*polygonSize,
                        coordy + polygonBase[1][2]*polygonSize}
        };
        return new Polygon(polygon[0], polygon[1], polygon[0].length);
    }

    private void drawUserPlayerArrow(Graphics2D g2) {
        gPlayer userPlayer = xMain.shellLogic.getUserPlayer();
        if(userPlayer == null || (sSettings.show_mapmaker_ui && !sSettings.inplay))
            return;
        int midx = userPlayer.coords[0] + userPlayer.dims[0]/2;
        int coordy = userPlayer.coords[1] - 200;
        Polygon pg = getPolygon(midx, coordy);
        Color color = gColors.getColorFromName("clrp_" + userPlayer.color);
        g2.setStroke(dFonts.thickStroke);
        g2.setColor(Color.BLACK);
        pg.translate(3, 3);
        g2.fillPolygon(pg);
        pg.translate(-3, -3);
        g2.setColor(color);
        g2.fillPolygon(pg);
    }

    private void drawSelectionBoxes(Graphics2D g2) {
        int mousex = MouseInfo.getPointerInfo().getLocation().x;
        int mousey = MouseInfo.getPointerInfo().getLocation().y;
        int window_offsetx = xMain.shellLogic.frame.getLocationOnScreen().x;
        int window_offsety = xMain.shellLogic.frame.getLocationOnScreen().y;
        // -- selected prefab (blocks)
        g2.setStroke(dFonts.thickStroke);
        for(String id : xMain.shellLogic.clientScene.getThingMap("THING_BLOCK").keySet()) {
            gBlock block = (gBlock) xMain.shellLogic.clientScene.getThingMap("THING_BLOCK").get(id);
            if(xMain.shellLogic.getUserPlayer() == null && block.prefabId.equals(sSettings.clientSelectedPrefabId)) {
                g2.setColor(gColors.getColorFromName("clrp_" + sSettings.clientPlayerColor));
                g2.drawRect(block.coords[0], block.coords[1], block.dims[0], block.dims[1]);
            }
        }
        // -- selected item
        for(String id : xMain.shellLogic.clientScene.getThingMap("THING_ITEM").keySet()) {
            gThing item = xMain.shellLogic.clientScene.getThingMap("THING_ITEM").get(id);
            if(item.id.equals(sSettings.clientSelectedItemId)) {
                g2.setColor(gColors.getColorFromName("clrp_" + sSettings.clientPlayerColor));
                g2.drawRect(item.coords[0], item.coords[1], item.dims[0], item.dims[1]);
            }
        }
        //prefab dims
        // -- preview rect
        int w = 300;
        int h = 300;
        if(sSettings.clientNewPrefabName.length() > 0) {
            int[] pfd = uiEditorMenus.getNewPrefabDims();
            w = pfd[0];
            h = pfd[1];
        }
        int px = eUtils.roundToNearest(eUtils.unscaleInt(mousex - window_offsetx)
                + (int) gCamera.coords[0] - w/2, uiEditorMenus.snapToX);
        int py = eUtils.roundToNearest(eUtils.unscaleInt(mousey - window_offsety)
                + (int) gCamera.coords[1] - h/2, uiEditorMenus.snapToY);
        sSettings.clientPrevX = px;
        sSettings.clientPrevY = py;
        sSettings.clientPrevW = w;
        sSettings.clientPrevH = h;
        nStateMap clStateMap = new nStateMap(xMain.shellLogic.clientNetThread.clientStateSnapshot);
        for(String id : clStateMap.keys()) {
            if(xMain.shellLogic.clientScene.getPlayerById(id) != null)
                continue;
            nState clState = clStateMap.get(id);
            String pxs = clState.get("px");
            String pys = clState.get("py");
            String pws = clState.get("pw");
            String phs = clState.get("ph");
            String cs = clState.get("color");
            String nm = clState.get("name");
            g2.setColor(gColors.getColorFromName("clrp_" + cs));
            //check for null fields
            for(String s : new String[]{pxs,pys,pws,phs,cs,nm}) {
                if(s.equalsIgnoreCase("null"))
                    return;
            }
            g2.drawRect(Integer.parseInt(pxs), Integer.parseInt(pys), Integer.parseInt(pws), Integer.parseInt(phs));
            dFonts.setFontGNormal(g2);
            g2.setColor(gColors.getColorFromName("clrp_" + cs));
            g2.drawString(nm, Integer.parseInt(pxs), Integer.parseInt(pys));
            if(id.equals(sSettings.uuid)) { //draw arrow over our own preview box
                Polygon pg = getPolygon(Integer.parseInt(pxs), Integer.parseInt(pys) - 200);
                Color color = gColors.getColorFromName("clrp_" + cs);
                g2.setStroke(dFonts.thickStroke);
                dFonts.setFontColor(g2, "clrf_normaltransparent");
                g2.drawPolygon(pg);
                g2.setColor(color);
                g2.fillPolygon(pg);
            }
        }
    }


    public dPanel() {
        super();
        super.setDoubleBuffered(false);
        setLayout(new GridBagLayout());
        setOpaque(false);
        setDoubleBuffered(false);
    }
}
