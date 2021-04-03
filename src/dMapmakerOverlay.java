import java.awt.*;
import java.util.ArrayList;

public class dMapmakerOverlay {
    public static void drawSelectionBoxes(Graphics2D g2) {
        int mousex = MouseInfo.getPointerInfo().getLocation().x;
        int mousey = MouseInfo.getPointerInfo().getLocation().y;
        int window_offsetx = oDisplay.instance().frame.getLocationOnScreen().x;
        int window_offsety = oDisplay.instance().frame.getLocationOnScreen().y;
        // -- selected prefab (blocks)
        for(String id : eManager.currentMap.scene.getThingMap("THING_BLOCK").keySet()) {
            gThing block = eManager.currentMap.scene.getThingMap("THING_BLOCK").get(id);
            if(cEditorLogic.state.createObjCode == gScene.THING_PREFAB
            && block.contains("prefabid") && block.isInt("prefabid", cVars.getInt("prefabid"))) {
                g2.setColor(new Color(255, 100, 255));
                g2.drawRect(eUtils.scaleInt(block.getInt("coordx")-cVars.getInt("camx")),
                        eUtils.scaleInt(block.getInt("coordy")-cVars.getInt("camy")),
                        eUtils.scaleInt(block.getInt("dimw")), eUtils.scaleInt(block.getInt("dimh")));
            }
        }
        // -- selected item
        for(String id : eManager.currentMap.scene.getThingMap("THING_ITEM").keySet()) {
            gThing item = eManager.currentMap.scene.getThingMap("THING_ITEM").get(id);
            if(cEditorLogic.state.createObjCode == gScene.THING_ITEM
                    && item.contains("itemid") && item.isInt("itemid", cVars.getInt("itemid"))) {
                g2.setColor(new Color(255, 150, 0));
                g2.drawRect(eUtils.scaleInt(item.getInt("coordx")-cVars.getInt("camx")),
                        eUtils.scaleInt(item.getInt("coordy")-cVars.getInt("camy")),
                        eUtils.scaleInt(item.getInt("dimw")), eUtils.scaleInt(item.getInt("dimh")));
            }
        }
        // -- selected flare
        ArrayList<gFlare> flareList = eManager.currentMap.scene.flares();
        if(flareList.size() > cEditorLogic.state.selectedFlareTag) {
            gFlare selectedFlare = flareList.get(cEditorLogic.state.selectedFlareTag);
            int sx = selectedFlare.getInt("coordx");
            int sy = selectedFlare.getInt("coordy");
            int sw = selectedFlare.getInt("dimw");
            int sh = selectedFlare.getInt("dimh");
            g2.setColor(new Color(50, 100, 255));
            g2.drawRect(eUtils.scaleInt(sx-cVars.getInt("camx")),
                    eUtils.scaleInt(sy-cVars.getInt("camy")),
                    eUtils.scaleInt(sw), eUtils.scaleInt(sh));
        }
        // -- preview rect
        int wf = cEditorLogic.state.newFlare.getInt("dimw");
        int hf = cEditorLogic.state.newFlare.getInt("dimh");
        int[] pfd = cScripts.getNewPrefabDims();
        int w = cEditorLogic.state.createObjCode == gScene.THING_FLARE ? wf
                : cEditorLogic.state.createObjCode == gScene.THING_PREFAB ? pfd[0] : 300;
        int h = cEditorLogic.state.createObjCode == gScene.THING_FLARE ? hf
                : cEditorLogic.state.createObjCode == gScene.THING_PREFAB ? pfd[1] : 300;
        int px = eUtils.roundToNearest(eUtils.unscaleInt(mousex - window_offsetx)
                +cVars.getInt("camx")-w/2, cEditorLogic.state.snapToX) - cVars.getInt("camx");
        int py = eUtils.roundToNearest(eUtils.unscaleInt(mousey - window_offsety)
                +cVars.getInt("camy")-h/2, cEditorLogic.state.snapToY) - cVars.getInt("camy");
        g2.setColor(new Color(50, 255, 100));
        g2.drawRect(eUtils.scaleInt(px), eUtils.scaleInt(py),
                eUtils.scaleInt(w), eUtils.scaleInt(h));
    }
}
