import java.awt.*;
import java.util.HashMap;

public class dMapmakerOverlay {
    public static int[] getNewPrefabDims() {
        //TODO: this sucks, find a better way to set size
        if(cClientLogic.newprefabname.contains("_large")) {
            return new int[]{2400, 2400};
        }
        else if(cClientLogic.newprefabname.contains("cube")) {
            return new int[]{300, 300};
        }
        return new int[]{1200, 1200};
    }

    public static void drawSelectionBoxes(Graphics2D g2) {
        int mousex = MouseInfo.getPointerInfo().getLocation().x;
        int mousey = MouseInfo.getPointerInfo().getLocation().y;
        int window_offsetx = oDisplay.instance().frame.getLocationOnScreen().x;
        int window_offsety = oDisplay.instance().frame.getLocationOnScreen().y;
        // -- selected prefab (blocks)
        g2.setStroke(dFonts.thickStroke);
        for(String id : cClientLogic.scene.getThingMap("THING_BLOCK").keySet()) {
            gThing block = cClientLogic.scene.getThingMap("THING_BLOCK").get(id);
            if(sSettings.drawhitboxes && block.isVal("type", "BLOCK_FLOOR")) {
                g2.setColor(gColors.getFontColorFromName("flooroutline"));
                g2.drawRect(block.getInt("coordx"),
                        block.getInt("coordy"),
                        block.getInt("dimw"), block.getInt("dimh"));
            }
            if(block.contains("prefabid") && block.isVal("prefabid", cClientLogic.selectedPrefabId)) {
                g2.setColor(gColors.getFontColorFromName("selected"));
                g2.drawRect(block.getInt("coordx"),
                        block.getInt("coordy"),
                        block.getInt("dimw"), block.getInt("dimh"));
            }
        }
        // -- selected item
        for(String id : cClientLogic.scene.getThingMap("THING_ITEM").keySet()) {
            gThing item = cClientLogic.scene.getThingMap("THING_ITEM").get(id);
            if(item.contains("id") && item.isVal("id", cClientLogic.selecteditemid)) {
                g2.setColor(gColors.getFontColorFromName("selected"));
                g2.drawRect(item.getInt("coordx"),
                        item.getInt("coordy"),
                        item.getInt("dimw"), item.getInt("dimh"));
            }
        }
        //prefab dims
        // -- preview rect
        int w = 300;
        int h = 300;
        if(cClientLogic.newprefabname.length() > 0) {
            int[] pfd = getNewPrefabDims();
            w = pfd[0];
            h = pfd[1];
        }
        int px = eUtils.roundToNearest(eUtils.unscaleInt(mousex - window_offsetx)
                + gCamera.getX() - w/2, uiEditorMenus.snapToX);
        int py = eUtils.roundToNearest(eUtils.unscaleInt(mousey - window_offsety)
                + gCamera.getY() - h/2, uiEditorMenus.snapToY);
//        g2.setColor(gColors.getFontColorFromName("preview"));
//        g2.drawRect(px, py, w, h);
        cClientLogic.prevX = px;
        cClientLogic.prevY = py;
        cClientLogic.prevW = w;
        cClientLogic.prevH = h;
        for(String id : nClient.instance().serverArgsMap.keySet()) {
            if(cClientLogic.scene.getPlayerById(id) != null)
                continue;
            HashMap<String, String> cArgs = nClient.instance().serverArgsMap.get(id);
            String pxs = cArgs.get("px");
            String pys = cArgs.get("py");
            String pws = cArgs.get("pw");
            String phs = cArgs.get("ph");
            String cs = cArgs.get("color");
            String nm = cArgs.get("name");
            if(pxs == null || pys == null || pws == null || phs == null || cs == null)
                continue;
            g2.setColor(gColors.getPlayerHudColorFromName(cs));
            g2.drawRect(Integer.parseInt(pxs), Integer.parseInt(pys), Integer.parseInt(pws), Integer.parseInt(phs));
            if(nm == null)
                continue;
            dFonts.setFontGNormal(g2);
            g2.setColor(gColors.getPlayerHudColorFromName(cs));
            g2.drawString(nm, Integer.parseInt(pxs), Integer.parseInt(pys));
            if(id.equals(uiInterface.uuid)) {
                Polygon pg = dTileTops.getPolygon(Integer.parseInt(pxs), Integer.parseInt(pys) - 200);
                Color color = gColors.getPlayerHudColorFromName(cs);
                g2.setStroke(dFonts.thickStroke);
                g2.setColor(gColors.getFontColorFromName("normaltransparent"));
                g2.drawPolygon(pg);
                g2.setColor(color);
                g2.fillPolygon(pg);
            }
        }
    }
}
