import javax.swing.JPanel;
import java.awt.*;
import java.awt.geom.AffineTransform;
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

    private void drawFrameUI(Graphics2D g2, long gameTimeMillis) {
        drawScreenFX(g2);
        dScreenMessages.displayScreenMessages(g2, gameTimeMillis);
        if(!sSettings.inplay && sSettings.show_mapmaker_ui && sSettings.clientMapLoaded) {
            drawBlockFloorsPreview(g2, xMain.shellLogic.clientPreviewScene);
            drawBlockCubesPreview(g2);
        }
    }

    private void drawScreenFX(Graphics g) {
        if(!sSettings.IS_CLIENT)
            return;
        nState userState = new nStateMap(xMain.shellLogic.clientNetThread.clientStateSnapshot).get(sSettings.uuid);
        if(userState == null)
            return;
        // health overlay
        int userhp = Math.max(Integer.parseInt(userState.get("hp")), 0);
        if (userhp < sSettings.clientMaxHP) {
            int factors = sSettings.vfxfactor;
            int maxl = gColors.hpAlpha;
            Color color = gColors.getColorFromName("clrp_" + xMain.shellLogic.clientVars.get("playercolor"));
            for (int i = 0; i < sSettings.width; i += sSettings.width / factors) {
                for (int j = 0; j < sSettings.height; j += sSettings.height / factors) {
                    int w = sSettings.width / factors;
                    int h = sSettings.height / factors;
                    if (Math.random() > 0.95 && Math.random() > 0.95) {
                        g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), maxl
                                - maxl * userhp / sSettings.clientMaxHP
                                + (int) (Math.random() * (-25) + 25)));
                        g.fillRect(i, j, w, h);
                    }
                }
            }
            int factorsdiv = sSettings.vfxfactordiv;
            int factorsw = sSettings.width / factorsdiv;
            int factorsh = sSettings.height / factorsdiv;
            for (int i = 0; i < factorsw; i++) {
                Color hpvfxColor = new Color(color.getRed(), color.getGreen(), color.getBlue(),
                        Math.abs(Math.abs((maxl / (factorsw / 2)) * (Math.abs(((factorsw / 2) - i))
                                - (factorsw / 2))) - maxl)
                                * (sSettings.clientMaxHP - userhp) / sSettings.clientMaxHP / 2);
                g.setColor(hpvfxColor);
                g.fillRect(sSettings.width / factorsw * i, 0, sSettings.width / factorsw, sSettings.height);
            }
            for (int i = 0; i < factorsh; i++) {
                Color hpvfxColor = new Color(color.getRed(), color.getGreen(), color.getBlue(),
                        Math.abs(Math.abs((maxl / (factorsh / 2)) * (Math.abs(((factorsh / 2) - i)) - (factorsh / 2))) - maxl)
                                * (sSettings.clientMaxHP - userhp) / sSettings.clientMaxHP / 2);
                g.setColor(hpvfxColor);
                g.fillRect(0, sSettings.height / factorsh * i, sSettings.width, sSettings.height / factorsh);
            }
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
        synchronized (scene.objectMaps) {
            drawBlockFloors(g2, scene);
            drawBlockWallsAndPlayers(g2, scene);
            drawMapmakerOverlay(g2, scene);
            dHUD.drawBulletsAndAnimations(g2, scene);
            dHUD.drawWaypoints(g2, scene);
            dHUD.drawPopups(g2, scene);
            dHUD.drawUserPlayerArrow(g2);
            dHUD.drawPlayerNames(g2);
            if (sSettings.show_mapmaker_ui)
                dHUD.drawSelectionBoxes(g2);
        }
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
                drawCollision(g2, (gBlockCollision) scene.getThingMap("BLOCK_COLLISION").get(id));
            }
            for(String id : scene.getThingMap("THING_PLAYER").keySet()) {
                gThing player = scene.getThingMap("THING_PLAYER").get(id);
                g2.setColor(Color.WHITE);
                g2.drawRect(player.coords[0], player.coords[1], player.dims[0], player.dims[1]);
            }
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
            if(thing.type.equals("THING_PLAYER"))
                drawPlayer(g2, (gPlayer) thing);
            else if(thing.type.startsWith("ITEM_"))
                drawItem(g2, (gItem) thing);
            else if(thing.type.contains("CUBE"))
                drawCube(g2, (gBlockCube) thing);
        }
        ConcurrentHashMap<String, gThing> floorMap = scene.getThingMap("BLOCK_FLOOR");
        for(String tag : floorMap.keySet()) {
            gThing floor = floorMap.get(tag);
            //flashlight
            if(xMain.shellLogic.getUserPlayer() != null) {
                int aimerx = eUtils.unscaleInt(xMain.shellLogic.getMouseCoordinates()[0]);
                int aimery = eUtils.unscaleInt(xMain.shellLogic.getMouseCoordinates()[1]);
                int snapX = aimerx + (int) gCamera.coords[0];
                int snapY = aimery + (int) gCamera.coords[1];
                int setw = sSettings.height;
                RadialGradientPaint df = new RadialGradientPaint(new Point(snapX, snapY), setw/2,
                        new float[]{0f, 1f}, new Color[]{new Color(0,0,0,0), gColors.getColorFromName("clrw_ambientshading")}
                );
                g2.setPaint(df);
                g2.fillRect(floor.coords[0], floor.coords[1], floor.dims[0], floor.dims[1]);
            }
            else {
                g2.setColor(gColors.getColorFromName("clrw_ambientshading"));
                g2.fillRect(floor.coords[0], floor.coords[1], floor.dims[0], floor.dims[1]);
            }
        }
    }

    private void drawCube(Graphics2D g2, gBlockCube cube) {
        //floor shadow
        if(sSettings.vfxenableshadows) {
            g2.setStroke(dFonts.thickStroke);
            GradientPaint gradient = new GradientPaint(
                    cube.coords[0] + cube.dims[0]/2,cube.coords[1] + cube.dims[1], gColors.getColorFromName("clrw_shadow1"),
                    cube.coords[0] + cube.dims[0]/2, cube.coords[1] + cube.dims[1] + (int)((cube.wallh)*sSettings.vfxshadowfactor),
                    gColors.getColorFromName("clrw_shadow2")
            );
            g2.setPaint(gradient);
            g2.fillRect(
                    cube.coords[0], cube.coords[1] + cube.dims[1], cube.dims[0], (int)(cube.wallh*sSettings.vfxshadowfactor)
            );
        }
        g2.setPaint(xMain.shellLogic.wallTextures[sSettings.mapTheme]);
        g2.fillRect(cube.coords[0], cube.coords[1] + cube.toph, cube.dims[0], cube.wallh);
        if (sSettings.vfxenableshading) {
            g2.setStroke(dFonts.thickStroke);
            if (cube.wallh > 0) {
                GradientPaint gradient;
                if(cube.wallh < 300) {
                    gradient = new GradientPaint(
                            cube.coords[0] + cube.dims[0] / 2, cube.coords[1] + cube.toph,
                            gColors.getColorFromName("clrw_walllowshading1"),
                            cube.coords[0] + cube.dims[0] / 2, cube.coords[1] + cube.dims[1],
                            gColors.getColorFromName("clrw_walllowshading2")
                    );
                }
                else {
                    gradient = new GradientPaint(
                            cube.coords[0] + cube.dims[0] / 2, cube.coords[1] + cube.toph,
                            gColors.getColorFromName("clrw_wallshading1"),
                            cube.coords[0] + cube.dims[0] / 2, cube.coords[1] + cube.dims[1],
                            gColors.getColorFromName("clrw_wallshading2")
                    );
                }
                g2.setPaint(gradient);
                g2.fillRect(cube.coords[0], cube.coords[1] + cube.toph, cube.dims[0], cube.wallh);
                if(cube.wallh < 300) {
                    gradient = new GradientPaint(
                            cube.coords[0] + cube.dims[0] / 2, cube.coords[1] + cube.toph,
                            gColors.getColorFromName("clrw_walllowoutline1"),
                            cube.coords[0] + cube.dims[0] / 2, cube.coords[1] + cube.dims[1],
                            gColors.getColorFromName("clrw_walllowoutline2")
                    );
                }
                else {
                    gradient = new GradientPaint(
                            cube.coords[0] + cube.dims[0] / 2, cube.coords[1] + cube.toph,
                            gColors.getColorFromName("clrw_walloutline1"),
                            cube.coords[0] + cube.dims[0] / 2, cube.coords[1] + cube.dims[1],
                            gColors.getColorFromName("clrw_walloutline2")
                    );
                }
                g2.setPaint(gradient);
                g2.drawRoundRect(cube.coords[0], cube.coords[1] + cube.toph, cube.dims[0], cube.wallh, 5, 5);
            }
        }
        g2.setPaint(xMain.shellLogic.topTextures[sSettings.mapTheme]);
        g2.fillRect(cube.coords[0], cube.coords[1], cube.dims[0], cube.toph);
        if(sSettings.vfxenableshading) {
            dFonts.setFontColor(g2, "clrw_topcolor");
            if(cube.wallh > 0 && cube.wallh < 300)
                dFonts.setFontColor(g2, "clrw_topcolordark");
            g2.fillRect(cube.coords[0], cube.coords[1], cube.dims[0], cube.toph);
            g2.setStroke(dFonts.thickStroke);
            GradientPaint gradient = new GradientPaint(
                    cube.coords[0] + cube.dims[0] / 2, cube.coords[1], gColors.getColorFromName("clrw_roofoutline1"),
                    cube.coords[0] + cube.dims[0] / 2, cube.coords[1] + cube.toph, gColors.getColorFromName("clrw_roofoutline2")
            );
            g2.setPaint(gradient);
            if(cube.wallh > 0 && cube.wallh < 300)
                g2.fillRect(cube.coords[0], cube.coords[1], cube.dims[0], cube.toph);
            g2.drawRoundRect(cube.coords[0], cube.coords[1], cube.dims[0], cube.toph, 5, 5);
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
        ConcurrentHashMap<String, gThing> floorMap = scene.getThingMap("BLOCK_FLOOR");
        for(String tag : floorMap.keySet()) {
            drawBlockFloor(g2, (gBlockFloor) floorMap.get(tag));
        }
    }

    private void drawBlockFloor(Graphics2D g2, gBlockFloor floor) {
        g2.setPaint(xMain.shellLogic.floorTextures[sSettings.mapTheme]);
        g2.fillRect(floor.coords[0], floor.coords[1], floor.dims[0], floor.dims[1]);
        if(!sSettings.vfxenableshading)
            return;
        g2.setColor(gColors.getColorFromName("clrw_floorshading"));
        g2.fillRect(floor.coords[0], floor.coords[1], floor.dims[0], floor.dims[1]);
    }

    private void drawBlockFloorPreview(Graphics2D g2, gBlockFloor floorPreview) {
        dFonts.setFontColor(g2, "clrw_floorcolorpreview");
        g2.fillRect(
                eUtils.scaleInt(floorPreview.coords[0]/4), eUtils.scaleInt(floorPreview.coords[1]/4),
                eUtils.scaleInt(floorPreview.dims[0]/4), eUtils.scaleInt(floorPreview.dims[1]/4)
        );
    }

    private void drawBlockFloorsPreview(Graphics2D g2, gScene scene) {
        ConcurrentHashMap<String, gThing> floorMap = scene.getThingMap("BLOCK_FLOOR");
        for(String tag : floorMap.keySet()) {
            drawBlockFloorPreview(g2, (gBlockFloor) floorMap.get(tag));
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

    private void drawBlockCubesPreview(Graphics2D g2) {
        ConcurrentHashMap<String, gThing> squareMap = xMain.shellLogic.clientPreviewScene.getThingMap("BLOCK_CUBE");
        for(String tag : squareMap.keySet()) {
            drawBlockCubePreview(g2, (gBlockCube) squareMap.get(tag));
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
