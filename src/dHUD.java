import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.concurrent.ConcurrentHashMap;


public class dHUD {
    private static final String dividerString = "_______________________";
    static int spriteRad = sSettings.height/30;

    public static void drawHUD(Graphics g) {
        if(!sSettings.IS_CLIENT || !sSettings.clientMapLoaded || sSettings.showscore)
            return;
        nStateMap clStateMap = new nStateMap(xMain.shellLogic.clientNetThread.clientStateSnapshot);
        nState userState = clStateMap.get(sSettings.uuid);
        if(userState == null)
            return;
        int ctr = 0;
        int hpbarwidth = sSettings.width/8;
        int marginX = sSettings.width/2 - clStateMap.keys().size()*(hpbarwidth/2 + sSettings.width/128);
        for(String id : clStateMap.keys()) {
            nState clState = clStateMap.get(id);
            //healthbar
            g.setColor(Color.black);
            g.fillRect(marginX + ctr*(hpbarwidth + sSettings.width/64)+3,28 * sSettings.height/32+3,hpbarwidth,
                    sSettings.height/24);
            g.setColor(gColors.getColorFromName("clrp_" + clState.get("color")));
            if(Integer.parseInt(clState.get("hp")) > 0 && xMain.shellLogic.getPlayerById(id) != null)
                g.fillRect(marginX + ctr*(hpbarwidth + sSettings.width/64),28 * sSettings.height/32,
                        hpbarwidth*Integer.parseInt(clState.get("hp"))/ sSettings.clientMaxHP,
                        sSettings.height/24);
            dFonts.setFontLarge(g);
            //score
            if(clState.contains("score")) {
                g.setColor(Color.BLACK);
                g.drawString(clState.get("score").split(":")[1],
                        marginX + ctr*(hpbarwidth + sSettings.width/64) + 3, 63*sSettings.height/64 + 3);
                g.setColor(gColors.getColorFromName("clrp_" + clState.get("color")));
                g.drawString(clState.get("score").split(":")[1],
                        marginX + ctr*(hpbarwidth + sSettings.width/64), 63*sSettings.height/64);
            }
            ctr++;
        }
    }

    public static int[] getNewPrefabDims() {
        //TODO: this sucks, find a better way to set size
        if(sSettings.clientNewPrefabName.contains("_large"))
            return new int[]{2400, 2400};
        else if(sSettings.clientNewPrefabName.contains("cube"))
            return new int[]{300, 300};
        return new int[]{1200, 1200};
    }

    public static void drawSelectionBoxes(Graphics2D g2) {
        int mousex = MouseInfo.getPointerInfo().getLocation().x;
        int mousey = MouseInfo.getPointerInfo().getLocation().y;
        int window_offsetx = xMain.shellLogic.displayPane.frame.getLocationOnScreen().x;
        int window_offsety = xMain.shellLogic.displayPane.frame.getLocationOnScreen().y;
//        // -- selected prefab (blocks)
//        g2.setStroke(dFonts.thickStroke);
//        for(String id : xMain.shellLogic.clientScene.getThingMap("THING_BLOCK").keySet()) { //TODO: concurrent excpetion occurred on this line
//            gThing block = xMain.shellLogic.clientScene.getThingMap("THING_BLOCK").get(id);
//            if(sSettings.drawhitboxes && block.type.equals("BLOCK_FLOOR")) {
//                dFonts.setFontColor(g2, "clrf_flooroutline");
//                g2.drawRect(block.coords[0],
//                        block.coords[1],
//                        block.dims[0], block.dims[1]);
//            }
//            if(xMain.shellLogic.getUserPlayer() == null && block.prefabId.equals(sSettings.clientSelectedPrefabId)) {
//                g2.setColor(gColors.getColorFromName("clrp_" + sSettings.clientPlayerColor));
//                g2.drawRect(block.coords[0],
//                        block.coords[1],
//                        block.dims[0], block.dims[1]);
//            }
//        }
//        // -- selected item
//        for(String id : xMain.shellLogic.clientScene.getThingMap("THING_ITEM").keySet()) {
//            gThing item = xMain.shellLogic.clientScene.getThingMap("THING_ITEM").get(id);
//            if(item.id.equals(sSettings.clientSelectedItemId)) {
//                g2.setColor(gColors.getColorFromName("clrp_" + sSettings.clientPlayerColor));
//                g2.drawRect(item.coords[0],
//                        item.coords[1],
//                        item.dims[0], item.dims[1]);
//            }
//        }
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
                + gCamera.coords[0] - w/2, uiEditorMenus.snapToX);
        int py = eUtils.roundToNearest(eUtils.unscaleInt(mousey - window_offsety)
                + gCamera.coords[1] - h/2, uiEditorMenus.snapToY);
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
                int x = collision.coords[0];
                int y = collision.coords[1];
                int w = collision.dims[0];
                int h = collision.dims[1];
                g2.drawRect(x,y,w,h);
            }
            for(String id : scene.getThingMap("THING_PLAYER").keySet()) {
                gThing player = scene.getThingMap("THING_PLAYER").get(id);
                g2.setColor(Color.RED);
                int x1 = player.coords[0];
                int y1 = player.coords[1];
                g2.drawRect(x1, y1, player.dims[0], player.dims[1]);
            }
        }
    }

    public static void drawBulletsAndAnimations(Graphics2D g2, gScene scene) {
        ConcurrentHashMap<String, gThing> bulletsMap = scene.getThingMap("THING_BULLET");
        for (String id : bulletsMap.keySet()) {
            gThing t = bulletsMap.get(id);
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

    public static void drawPopups(Graphics g, gScene scene) {
        dFonts.setFontGNormal(g);
        for(String id : scene.getThingMap("THING_POPUP").keySet()) {
            ((gPopup)scene.getThingMap("THING_POPUP").get(id)).draw((Graphics2D) g);
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

    public static void drawPlayerNames(Graphics g) {
        nStateMap clStateMap = new nStateMap(xMain.shellLogic.clientNetThread.clientStateSnapshot);
        for(String id : clStateMap.keys()) {
            gPlayer p = xMain.shellLogic.getPlayerById(id);
            if(p == null)
                continue;
            nState clState = clStateMap.get(id);
            dFonts.setFontGNormal(g);
            String name = clState.get("name");
            int coordx = p.coords[0];
            int coordy = p.coords[1];
            String ck = clState.get("color");
            Color color = gColors.getColorFromName("clrp_" + ck);
            g.setColor(Color.BLACK);
            g.drawString(name,coordx + p.dims[0]/2 - (int)g.getFont().getStringBounds(name, dFonts.fontrendercontext).getWidth()/2 + 3, coordy + 3);
            g.setColor(color);
            g.drawString(name,coordx + p.dims[0]/2 - (int)g.getFont().getStringBounds(name, dFonts.fontrendercontext).getWidth()/2, coordy);
            int[] bounds = {
                    coordx + p.dims[0]/2-(int)g.getFont().getStringBounds(name, dFonts.fontrendercontext).getWidth()/2
                            - eUtils.unscaleInt(5*sSettings.height/128),
                    coordy - eUtils.unscaleInt(sSettings.height/32),
                    eUtils.unscaleInt(sSettings.height/32),
                    eUtils.unscaleInt(sSettings.height/32)
            };
            g.setColor(Color.BLACK);
            g.fillOval(bounds[0]+3, bounds[1]+3, bounds[2], bounds[3]);
            g.setColor(color);
            g.fillOval(bounds[0], bounds[1], bounds[2], bounds[3]);
        }
    }

    public static void showScoreBoard(Graphics g) {
        if(!sSettings.IS_CLIENT)
            return;
        nStateMap clStateMap = new nStateMap(xMain.shellLogic.clientNetThread.clientStateSnapshot);
        dFonts.setFontColor(g, "clrf_scoreboardbg");
        g.fillRect(0,0,sSettings.width,sSettings.height);
        dFonts.setFontColor(g, "clrf_normal");
        dFonts.drawCenteredString(g, sSettings.clientGameModeTitle + " - " + sSettings.clientGameModeText, sSettings.width/2, 2*spriteRad);
        g.drawString(clStateMap.keys().size() + " players", sSettings.width/3,5*spriteRad);
        g.drawString("                           Wins",sSettings.width/3,5*spriteRad);
        g.drawString("                                       Score",sSettings.width/3,5*spriteRad);
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
            if(id.equals(sSettings.uuid))
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
            g.setColor(color);
            if(isMe) {
                Polygon myArrow = new Polygon(
                        new int[] {coordx - height, coordx, coordx - height},
                        new int[]{coordy - height, coordy - height/2, coordy},
                        3
                );
                g.setColor(color);
                g.fillPolygon(myArrow);
            }
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
        if(sSettings.inplay && xMain.shellLogic.getUserPlayer() != null) {
            double[] deltas = new double[]{
                    dx - xMain.shellLogic.getUserPlayer().coords[0]
                            + xMain.shellLogic.getUserPlayer().dims[0]/2,
                    dy - xMain.shellLogic.getUserPlayer().coords[1]
                            + xMain.shellLogic.getUserPlayer().dims[1]/2
            };
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
            //big font
            dFonts.setFontGNormal(g2);
            dFonts.drawCenteredString(g2, message, dx, dy);
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

    public static void drawWaypoints(Graphics2D g2, gScene scene) {
        if(sSettings.inplay) {
            // players
            for(String id : scene.getThingMapIds("THING_PLAYER")) {
                if(id.equals(sSettings.uuid))
                    continue;
                gPlayer wpPlayer = scene.getPlayerById(id);
                if(wpPlayer == null)
                    continue;
                if(!(wpPlayer.waypoint.equals("null") || wpPlayer.waypoint.equals("0")))
                    drawNavPointer(g2, wpPlayer.coords[0] + wpPlayer.dims[0] / 2,
                            wpPlayer.coords[1] + wpPlayer.dims[1] / 2,
                            wpPlayer.waypoint);
            }
            // items
            String[] itemIds = scene.getThingMapIds("THING_ITEM");
            for(String id : itemIds) {
                gThing item = scene.getThingMap("THING_ITEM").get(id);
                if(!(item.waypoint.equals("null") || item.waypoint.equals("0")))
                    drawNavPointer(g2,item.coords[0] + item.dims[0]/2,
                            item.coords[1] + item.dims[1]/2, item.waypoint);
            }
        }
    }
}
