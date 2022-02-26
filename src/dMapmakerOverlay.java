import java.awt.*;

public class dMapmakerOverlay {
    public static int[] getNewPrefabDims() {
        //TODO: this sucks, find a better way to set size
        if(cVars.get("newprefabname").contains("_large")) {
            return new int[]{2400, 2400};
        }
        else if(cVars.get("newprefabname").contains("cube")) {
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
            if(sVars.isOne("drawhitboxes") && block.isVal("type", "BLOCK_FLOOR")) {
                g2.setColor(new Color(100, 100, 255));
                g2.drawRect(eUtils.scaleInt(block.getInt("coordx")-cVars.getInt("camx")),
                        eUtils.scaleInt(block.getInt("coordy")-cVars.getInt("camy")),
                        eUtils.scaleInt(block.getInt("dimw")), eUtils.scaleInt(block.getInt("dimh")));
            }
            if(block.contains("prefabid") && block.isVal("prefabid", cVars.get("selectedprefabid"))) {
                g2.setColor(new Color(255, 100, 255));
                g2.drawRect(eUtils.scaleInt(block.getInt("coordx")-cVars.getInt("camx")),
                        eUtils.scaleInt(block.getInt("coordy")-cVars.getInt("camy")),
                        eUtils.scaleInt(block.getInt("dimw")), eUtils.scaleInt(block.getInt("dimh")));
            }
        }
        // -- selected item
        for(String id : cClientLogic.scene.getThingMap("THING_ITEM").keySet()) {
            gThing item = cClientLogic.scene.getThingMap("THING_ITEM").get(id);
            if(item.contains("itemid") && item.isVal("itemid", cVars.get("selecteditemid"))) {
                g2.setColor(new Color(255, 100, 255));
                g2.drawRect(eUtils.scaleInt(item.getInt("coordx")-cVars.getInt("camx")),
                        eUtils.scaleInt(item.getInt("coordy")-cVars.getInt("camy")),
                        eUtils.scaleInt(item.getInt("dimw")), eUtils.scaleInt(item.getInt("dimh")));
            }
        }
        //prefab dims
        // -- preview rect
        int w = 300;
        int h = 300;
        if(cVars.get("newprefabname").length() > 0) {
            int[] pfd = getNewPrefabDims();
            w = pfd[0];
            h = pfd[1];
        }
        int px = eUtils.roundToNearest(eUtils.unscaleInt(mousex - window_offsetx)
                +cVars.getInt("camx")-w/2, uiEditorMenus.snapToX) - cVars.getInt("camx");
        int py = eUtils.roundToNearest(eUtils.unscaleInt(mousey - window_offsety)
                +cVars.getInt("camy")-h/2, uiEditorMenus.snapToY) - cVars.getInt("camy");
        g2.setColor(new Color(50, 255, 100));
        g2.drawRect(eUtils.scaleInt(px), eUtils.scaleInt(py),
                eUtils.scaleInt(w), eUtils.scaleInt(h));
    }
}
