import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.*;

public class dTileTops {
    public static void drawMapmakerOverlay(Graphics2D g2, gScene scene) {
        //draw the grid OVER everything
        if(sSettings.drawmapmakergrid) {
            dFonts.setFontColor(g2, "clrf_mapmakergrid");
            g2.setStroke(dFonts.defaultStroke);
            for(int i = -12000; i <= 12000; i+=300) {
                g2.drawLine(-12000,
                        i,
                        12000,
                        i);
                g2.drawLine(i,
                        -12000,
                        i,
                        12000);
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
                g2.drawRect(
                        x1,
                        y1,
                        player.getInt("dimw"),
                        player.getInt("dimh")
                );
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
            gPlayer userPlayer = cClientLogic.getUserPlayer();
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
        nStateMap clStateMap = new nStateMap(cClientLogic.netClientThread.clientStateSnapshot);
        for(String id : clStateMap.keys()) {
            gPlayer p = cClientLogic.getPlayerById(id);
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
}
