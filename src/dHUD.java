import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;


public class dHUD {
    private static final String dividerString = "_______________________";

    public static void drawHUD(Graphics g) {
        if(!sSettings.IS_CLIENT)
            return;
        nState userState = new nStateMap(xMain.shellLogic.clientNetThread.clientStateSnapshot).get(uiInterface.uuid);
        if(userState == null)
            return;
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(dFonts.hudStroke);
//        health
        g.setColor(Color.black);
        g.fillRect(sSettings.width/64,59 * sSettings.height/64,sSettings.width/8,
                sSettings.height/64);
        g.setColor(gColors.getColorFromName("clrp_" + sSettings.clientPlayerColor));
        g.fillRect(sSettings.width/64,59 * sSettings.height/64,
                sSettings.width/8*Integer.parseInt(userState.get("hp"))/ sSettings.clientMaxHP,
                sSettings.height/64);
        g.drawString(userState.get("hp"), 37*sSettings.width / 256, 15*sSettings.height/16);
        dFonts.setFontNormal(g);
        //score
        nStateMap clStateMap = new nStateMap(xMain.shellLogic.clientNetThread.clientStateSnapshot);
        if(clStateMap.contains(uiInterface.uuid) && clStateMap.get(uiInterface.uuid).contains("score")) {
            g.setColor(gColors.getColorFromName("clrp_" + sSettings.clientPlayerColor));
            g.drawString("$ "+ clStateMap.get(uiInterface.uuid).get("score").split(":")[1],
                    sSettings.width / 64, 58*sSettings.height/64);
        }
        dFonts.setFontColor(g, "clrf_normaldark");
        g.drawString(sSettings.clientPlayerName, sSettings.width / 64, 62*sSettings.height/64);
        g.setColor(gColors.getColorFromName("clrp_" + sSettings.clientPlayerColor));
        g.fillRect(sSettings.width/128, 28*sSettings.height/32, sSettings.width/256, 3*sSettings.height/32);
        // other players on server
        dFonts.setFontSmall(g);
        int ctr = 1;
        for (String id : clStateMap.keys()) {
            if(id.equals(uiInterface.uuid))
                continue;
            dFonts.setFontColor(g, "clrf_normaldark");
            String color = "blue";
            if(clStateMap.get(id).contains("color"))
                color = clStateMap.get(id).get("color");
            g.setColor(gColors.getColorFromName("clrp_" + color));
            String score = "0:0";
            if(clStateMap.get(id).contains("score"))
                score = clStateMap.get(id).get("score");
            g.drawString("$ " + score.split(":")[1],
                    sSettings.width / 64, 55 * sSettings.height / 64 - (ctr * (sSettings.height / 32)));
            dFonts.setFontColor(g, "clrf_normaldark");
            g.drawString(clStateMap.get(id).get("name"), sSettings.width / 64,
                    56 * sSettings.height / 64 - (ctr * (sSettings.height / 32)));
            g.setColor(gColors.getColorFromName("clrp_" + color));
            g.fillRect(sSettings.width / 128, 54 * sSettings.height / 64 - (ctr * (sSettings.height / 32)),
                    sSettings.width / 256, sSettings.height / 32);
            ctr++;
        }
    }

    public static int[] getNewPrefabDims() {
        //TODO: this sucks, find a better way to set size
        if(sSettings.clientNewPrefabName.contains("_large")) {
            return new int[]{2400, 2400};
        }
        else if(sSettings.clientNewPrefabName.contains("cube")) {
            return new int[]{300, 300};
        }
        return new int[]{1200, 1200};
    }

    public static void drawSelectionBoxes(Graphics2D g2) {
        int mousex = MouseInfo.getPointerInfo().getLocation().x;
        int mousey = MouseInfo.getPointerInfo().getLocation().y;
        int window_offsetx = xMain.shellLogic.displayPane.frame.getLocationOnScreen().x;
        int window_offsety = xMain.shellLogic.displayPane.frame.getLocationOnScreen().y;
        // -- selected prefab (blocks)
        g2.setStroke(dFonts.thickStroke);
        for(String id : xMain.shellLogic.clientScene.getThingMap("THING_BLOCK").keySet()) {
            gThing block = xMain.shellLogic.clientScene.getThingMap("THING_BLOCK").get(id);
            if(sSettings.drawhitboxes && block.isVal("type", "BLOCK_FLOOR")) {
                dFonts.setFontColor(g2, "clrf_flooroutline");
                g2.drawRect(block.getInt("coordx"),
                        block.getInt("coordy"),
                        block.getInt("dimw"), block.getInt("dimh"));
            }
            if(xMain.shellLogic.getUserPlayer() == null &&
                    block.contains("prefabid") && block.isVal("prefabid", sSettings.clientSelectedPrefabId)) {
                g2.setColor(gColors.getColorFromName("clrp_" + sSettings.clientPlayerColor));
                g2.drawRect(block.getInt("coordx"),
                        block.getInt("coordy"),
                        block.getInt("dimw"), block.getInt("dimh"));
            }
        }
        // -- selected item
        for(String id : xMain.shellLogic.clientScene.getThingMap("THING_ITEM").keySet()) {
            gThing item = xMain.shellLogic.clientScene.getThingMap("THING_ITEM").get(id);
            if(item.contains("id") && item.isVal("id", sSettings.clientSelectedItemId)) {
                g2.setColor(gColors.getColorFromName("clrp_" + sSettings.clientPlayerColor));
                g2.drawRect(item.getInt("coordx"),
                        item.getInt("coordy"),
                        item.getInt("dimw"), item.getInt("dimh"));
            }
        }
        //prefab dims
        // -- preview rect
        int w = 300;
        int h = 300;
        if(sSettings.clientNewPrefabName.length() > 0) {
            int[] pfd = getNewPrefabDims();
            w = pfd[0];
            h = pfd[1];
        }
        int px = eUtils.roundToNearest(eUtils.unscaleInt(mousex - window_offsetx)
                + gCamera.getX() - w/2, uiEditorMenus.snapToX);
        int py = eUtils.roundToNearest(eUtils.unscaleInt(mousey - window_offsety)
                + gCamera.getY() - h/2, uiEditorMenus.snapToY);
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
            if(id.equals(uiInterface.uuid)) { //draw arrow over our own preview box
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

    public static void drawMapmakerOverlay(Graphics2D g2, gScene scene) {
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
            g2.setColor(Color.RED);
            for(String id : scene.getThingMap("BLOCK_COLLISION").keySet()) {
                gThing collision = scene.getThingMap("BLOCK_COLLISION").get(id);
                int x = collision.getX();
                int y = collision.getY();
                int w = collision.getWidth();
                int h = collision.getHeight();
                g2.drawRect(x,y,w,h);
            }
            for(String id : scene.getThingMap("THING_PLAYER").keySet()) {
                gThing player = scene.getThingMap("THING_PLAYER").get(id);
                g2.setColor(Color.RED);
                if(!player.containsFields(new String[]{"coordx", "coordy", "dimw", "dimh"}))
                    continue;
                int x1 = player.getInt("coordx");
                int y1 = player.getInt("coordy");
                g2.drawRect(x1, y1, player.getInt("dimw"), player.getInt("dimh"));
            }
        }
    }

    public static void drawBulletsAndAnimations(Graphics2D g2, gScene scene) {
        HashMap<String, gThing> bulletsMap = scene.getThingMap("THING_BULLET");
        Queue<gThing> drawThings = new LinkedList<>();
        for (String id : bulletsMap.keySet()) {
            drawThings.add(bulletsMap.get(id));
        }
        while (drawThings.size() > 0) {
            gBullet t = (gBullet) drawThings.remove();
            g2.drawImage(t.sprite, t.getInt("coordx"), t.getInt("coordy"), null);
        }
        if(!sSettings.vfxenableanimations)
            return;
        HashMap<String, gThing> animationsMap = scene.getThingMap("THING_ANIMATION");
        long gameTimeMillis = gTime.gameTime;
        for(String id : animationsMap.keySet()) {
            gThing emit = animationsMap.get(id);
            if(emit.getInt("frame") < gAnimations.animation_selection[emit.getInt("animation")].frames.length) {
                if (gAnimations.animation_selection[emit.getInt("animation")].frames[emit.getInt("frame")] != null) {
                    g2.drawImage(gAnimations.animation_selection[emit.getInt("animation")].frames[emit.getInt("frame")],
                            emit.getInt("coordx"), emit.getInt("coordy"), null);
                    if (emit.getLong("frametime") + 1000/gAnimations.animation_selection[emit.getInt("animation")].framerate
                            < gameTimeMillis) {
                        emit.putInt("frame", emit.getInt("frame")+1);
                        emit.putLong("frametime", gameTimeMillis);
                    }
                }
            }
        }
    }

    public static void drawPopups(Graphics g, gScene scene) {
        Collection<String> keys = scene.getThingMap("THING_POPUP").keySet();
        int size = keys.size();
        String[] popupsIds = keys.toArray(new String[size]);
        if(size > 0)
            dFonts.setFontGNormal(g);
        for(String id : popupsIds) {
            gPopup p = (gPopup) scene.getThingMap("THING_POPUP").get(id);
            if(p == null)
                continue;
            // look for hashtag color codes here
            String s = p.get("text");
            StringBuilder ts = new StringBuilder();
            for(String word : s.split(" ")) {
                if(word.contains("#")) {
                    if(word.split("#").length != 2)
                        ts.append(word).append(" ");
                    else if(gColors.getColorFromName("clrp_" + word.split("#")[1].replace(":","")) != null){
                        g.setColor(Color.BLACK);
                        g.drawString(word.split("#")[0]+" ",
                                p.getInt("coordx") + dFonts.getStringWidth(g, ts.toString())+3,
                                p.getInt("coordy") + 3);
                        g.setColor(gColors.getColorFromName("clrp_" + word.split("#")[1].replace(":","")));
                        g.drawString(word.split("#")[0]+" ",
                                p.getInt("coordx") + dFonts.getStringWidth(g, ts.toString()),
                                p.getInt("coordy"));
                        dFonts.setFontColor(g, "clrf_normal");
                        ts.append(word.split("#")[0]).append(word.contains(":") ? ": " : " ");
                        continue;
                    }
                }
                g.setColor(Color.BLACK);
                g.setColor(Color.BLACK);
                g.drawString(word.split("#")[0]+" ",
                        p.getInt("coordx") + dFonts.getStringWidth(g, ts.toString())+3,
                        p.getInt("coordy") + 3);
                dFonts.setFontColor(g, "clrf_normal");
                g.drawString(word.split("#")[0]+" ",
                        p.getInt("coordx") + dFonts.getStringWidth(g, ts.toString()), p.getInt("coordy"));
                ts.append(word).append(" ");
            }
        }
    }

    public static Polygon getPolygon(int midx, int coordy) {
        int[][] polygonBase = new int[][]{new int[]{1,1,1}, new int[]{0,0,1}};
        int polygonSize = eUtils.unscaleInt(sSettings.width/32);
        int[][] polygon = new int[][]{
                new int[]{midx - polygonBase[0][0]*polygonSize, midx + polygonBase[0][1]*polygonSize, midx},
                new int[]{coordy + polygonBase[1][0]*polygonSize, coordy + polygonBase[1][1]*polygonSize,
                        coordy + polygonBase[1][2]*polygonSize}
        };
        return new Polygon(polygon[0], polygon[1], polygon[0].length);
    }

    public static void drawUserPlayerArrow(Graphics2D g2) {
        if(sSettings.drawplayerarrow) {
            gPlayer userPlayer = xMain.shellLogic.getUserPlayer();
            if(userPlayer == null || (sSettings.show_mapmaker_ui && !uiInterface.inplay))
                return;
            int midx = userPlayer.getInt("coordx") + userPlayer.getInt("dimw")/2;
            int coordy = userPlayer.getInt("coordy") - 200;
            Polygon pg = getPolygon(midx, coordy);
            Color color = gColors.getColorFromName("clrp_" + xMain.shellLogic.clientVars.get("playercolor"));
            g2.setStroke(dFonts.thickStroke);
            dFonts.setFontColor(g2, "clrf_normaltransparent");
            g2.drawPolygon(pg);
            g2.setColor(color);
            g2.fillPolygon(pg);
        }
    }

    public static void drawPlayerNames(Graphics g) {
        nStateMap clStateMap = new nStateMap(xMain.shellLogic.clientNetThread.clientStateSnapshot);
        for(String id : clStateMap.keys()) {
            gPlayer p = xMain.shellLogic.getPlayerById(id);
            if(p == null)
                continue;
            nState clState = clStateMap.get(id);
            dFonts.setFontGNormal(g);
            String name = clState.get("name");
            int coordx = p.getInt("coordx");
            int coordy = p.getInt("coordy");
            String ck = clState.get("color");
            Color color = gColors.getColorFromName("clrp_" + ck);
            dFonts.drawPlayerNameHud(g, name, coordx + p.getInt("dimw")/2, coordy, color);
            int[] bounds = {
                    coordx + p.getInt("dimw")/2-(int)g.getFont().getStringBounds(name, dFonts.fontrendercontext).getWidth()/2
                            - eUtils.unscaleInt(5*sSettings.height/128),
                    coordy - eUtils.unscaleInt(sSettings.height/32),
                    eUtils.unscaleInt(sSettings.height/32),
                    eUtils.unscaleInt(sSettings.height/32)
            };
            g.fillOval(bounds[0], bounds[1], bounds[2], bounds[3]);
            dFonts.setFontColor(g, "clrf_normaltransparent");
            g.drawOval(bounds[0], bounds[1], bounds[2], bounds[3]);
        }
    }

    public static void showScoreBoard(Graphics g) {
        if(!sSettings.IS_CLIENT)
            return;
        nStateMap clStateMap = new nStateMap(xMain.shellLogic.clientNetThread.clientStateSnapshot);
        dFonts.setFontColor(g, "clrf_scoreboardbg");
        g.fillRect(0,0,sSettings.width,sSettings.height);
        dFonts.setFontColor(g, "clrf_highlight");
        dFonts.drawCenteredString(g, sSettings.clientGameModeTitle.toUpperCase() + ": " + sSettings.clientGameModeText,
                sSettings.width/2, 2*sSettings.height/30);
        dFonts.setFontColor(g, "clrf_normal");
        g.drawString(clStateMap.keys().size() + " players",
                sSettings.width/3,5*sSettings.height/30);
        g.drawString("                           Wins",sSettings.width/3,5*sSettings.height/30);
        g.drawString("                                       Score",sSettings.width/3,5*sSettings.height/30);
        g.drawString(dividerString,
                sSettings.width/3, 11*sSettings.height/60);

        StringBuilder sortedScoreIds = new StringBuilder();
        boolean sorted = false;
        while(!sorted) {
            sorted = true;
            int topscore = -1;
            String topid = "";
            for (String id : clStateMap.keys()) {
                if(!sortedScoreIds.toString().contains(id) && clStateMap.get(id).contains("score")) {
                    if(Integer.parseInt(clStateMap.get(id).get("score").split(":")[1]) > topscore) {
                        topscore = Integer.parseInt(clStateMap.get(id).get("score").split(":")[1]);
                        topid = id;
                        sorted = false;
                    }
                }
            }
            sortedScoreIds.append(topid).append(",");
        }
        int ctr = 0;
        int place = 1;
        int prevscore = -1;
        boolean isMe = false;
        for(String id : sortedScoreIds.toString().split(",")) {
            if(id.equals(uiInterface.uuid))
                isMe = true;
            if(Integer.parseInt(clStateMap.get(id).get("score").split(":")[1]) < prevscore)
                place++;
            String hudName = place + "." + clStateMap.get(id).get("name");
            int coordx = sSettings.width/3;
            int coordy = 7 * sSettings.height / 30 + ctr * sSettings.height / 30;
            int height = sSettings.height / 30;
            String ck = clStateMap.get(id).get("color");
            Color color = gColors.getColorFromName("clrp_" + ck);
            dFonts.drawPlayerNameScoreboard(g, hudName, coordx, coordy, color);
            if(xMain.shellLogic.getPlayerById(id) != null) {
                Image sprite = gTextures.getGScaledImage(eManager.getPath(String.format("animations/player_%s/a03.png", ck)), sSettings.height / 30, sSettings.height / 30);
                g.drawImage(sprite, coordx - sSettings.height / 30, coordy - height, null);
            }
            g.setColor(color);
            if(isMe)
                g.drawRect(coordx, coordy - height,
                        dFonts.getStringWidth(g, dividerString), dFonts.getStringHeight(g, hudName));
            g.drawString("                           "
                            + clStateMap.get(id).get("score").split(":")[0], sSettings.width/3, coordy);
            g.drawString("                                       "
                            + clStateMap.get(id).get("score").split(":")[1], sSettings.width/3, coordy);
            dFonts.setFontColor(g, "clrf_normal");
            if(isMe)
                isMe = false;
            ctr++;
            prevscore = Integer.parseInt(clStateMap.get(id).get("score").split(":")[1]);
        }
    }

    public static void drawNavPointer(Graphics2D g2, int dx, int dy, String message) {
        if(uiInterface.inplay && xMain.shellLogic.getUserPlayer() != null) {
            double[] deltas = new double[]{
                    dx - xMain.shellLogic.getUserPlayer().getInt("coordx")
                            + xMain.shellLogic.getUserPlayer().getDouble("dimw")/2,
                    dy - xMain.shellLogic.getUserPlayer().getInt("coordy")
                            + xMain.shellLogic.getUserPlayer().getDouble("dimh")/2
            };
            g2.setColor(gColors.getColorFromName("clrp_" + xMain.shellLogic.clientVars.get("playercolor")));
            int[][] polygondims = new int[][]{
                    new int[]{dx - eUtils.unscaleInt(sSettings.height / 16), dx,
                            dx + eUtils.unscaleInt(sSettings.height / 16), dx
                    },
                    new int[]{dy, dy - eUtils.unscaleInt(sSettings.height / 16),
                            dy, dy + eUtils.unscaleInt(sSettings.height / 16)
                    }
            };
            g2.fillPolygon(polygondims[0], polygondims[1], 4);
            g2.setStroke(dFonts.thickStroke);
            dFonts.setFontColor(g2, "clrf_normaltransparent");
            g2.drawPolygon(polygondims[0], polygondims[1], 4);
            //big font
            dFonts.setFontGNormal(g2);
            dFonts.drawCenteredString(g2, message, dx, dy);
            AffineTransform backup = g2.getTransform();
            g2.translate(gCamera.getX(), gCamera.getY());
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
            g2.setColor(gColors.getColorFromName("clrp_" + xMain.shellLogic.clientVars.get("playercolor")));
            g2.fillPolygon(arrowpolygon[0], arrowpolygon[1], 3);
            dFonts.setFontColor(g2, "clrf_normaltransparent");
            g2.setStroke(dFonts.waypointStroke);
            g2.drawPolygon(arrowpolygon[0], arrowpolygon[1], 3);
            g2.translate(-gCamera.getX(), -gCamera.getY());
            g2.setTransform(backup);
        }
    }

    public static void drawWaypoints(Graphics2D g2, gScene scene) {
        if(uiInterface.inplay) {
            // players
            for(String id : scene.getThingMapIds("THING_PLAYER")) {
                if(id.equals(uiInterface.uuid))
                    continue;
                gPlayer wpPlayer = scene.getPlayerById(id);
                if(wpPlayer == null)
                    continue;
                if(!(wpPlayer.get("waypoint").equals("null") || wpPlayer.get("waypoint").equals("0")))
                    drawNavPointer(g2, wpPlayer.getInt("coordx") + wpPlayer.getInt("dimw") / 2,
                            wpPlayer.getInt("coordy") + wpPlayer.getInt("dimh") / 2,
                            wpPlayer.get("waypoint"));
            }
            // items
            String[] itemIds = scene.getThingMapIds("THING_ITEM");
            for(String id : itemIds) {
                gThing item = scene.getThingMap("THING_ITEM").get(id);
                if(!(item.get("waypoint").equals("null") || item.get("waypoint").equals("0")))
                    drawNavPointer(g2,item.getInt("coordx") + item.getInt("dimw")/2,
                            item.getInt("coordy") + item.getInt("dimh")/2, item.get("waypoint"));
            }
        }
    }
}
