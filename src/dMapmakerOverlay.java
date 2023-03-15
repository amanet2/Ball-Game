import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Polygon;

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
                dFonts.setFontColor(g2, "clrf_flooroutline");
                g2.drawRect(block.getInt("coordx"),
                        block.getInt("coordy"),
                        block.getInt("dimw"), block.getInt("dimh"));
            }
            if(cClientLogic.getUserPlayer() == null &&
                    block.contains("prefabid") && block.isVal("prefabid", cClientLogic.selectedPrefabId)) {
                g2.setColor(gColors.getColorFromName("clrp_" + cClientLogic.playerColor));
                g2.drawRect(block.getInt("coordx"),
                        block.getInt("coordy"),
                        block.getInt("dimw"), block.getInt("dimh"));
            }
        }
        // -- selected item
        for(String id : cClientLogic.scene.getThingMap("THING_ITEM").keySet()) {
            gThing item = cClientLogic.scene.getThingMap("THING_ITEM").get(id);
            if(item.contains("id") && item.isVal("id", cClientLogic.selecteditemid)) {
                g2.setColor(gColors.getColorFromName("clrp_" + cClientLogic.playerColor));
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
        cClientLogic.prevX = px;
        cClientLogic.prevY = py;
        cClientLogic.prevW = w;
        cClientLogic.prevH = h;
        nStateMap clStateMap = nClient.instance().clientStateMap;
        for(String id : clStateMap.keys()) {
            if(cClientLogic.scene.getPlayerById(id) != null)
                continue;
            nState clState = clStateMap.get(id);
            String pxs = clState.get("px");
            String pys = clState.get("py");
            String pws = clState.get("pw");
            String phs = clState.get("ph");
            String cs = clState.get("color");
            String nm = clState.get("name");
            g2.setColor(gColors.getColorFromName("clrp_" + cs));
            g2.drawRect(Integer.parseInt(pxs), Integer.parseInt(pys), Integer.parseInt(pws), Integer.parseInt(phs));
            dFonts.setFontGNormal(g2);
            g2.setColor(gColors.getColorFromName("clrp_" + cs));
            g2.drawString(nm, Integer.parseInt(pxs), Integer.parseInt(pys));
            if(id.equals(uiInterface.uuid)) { //draw arrow over our own preview box
                Polygon pg = dTileTops.getPolygon(Integer.parseInt(pxs), Integer.parseInt(pys) - 200);
                Color color = gColors.getColorFromName("clrp_" + cs);
                g2.setStroke(dFonts.thickStroke);
                dFonts.setFontColor(g2, "clrf_normaltransparent");
                g2.drawPolygon(pg);
                g2.setColor(color);
                g2.fillPolygon(pg);
            }
        }
    }
}
