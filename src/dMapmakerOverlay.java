import java.awt.*;
import java.util.ArrayList;

public class dMapmakerOverlay {
    public static void drawSelectionBoxes(Graphics2D g2) {
        if(sSettings.show_mapmaker_ui && !uiInterface.inplay){
            int mousex = MouseInfo.getPointerInfo().getLocation().x;
            int mousey = MouseInfo.getPointerInfo().getLocation().y;
            int window_offsetx = oDisplay.instance().frame.getLocationOnScreen().x;
            int window_offsety = oDisplay.instance().frame.getLocationOnScreen().y;
            // -- selected tile
            if(eManager.currentMap.scene.tiles().size() > cEditorLogic.state.selectedTileId) {
                int st = cEditorLogic.state.selectedTileId;
                int sx = eManager.currentMap.scene.tiles().get(st).getInt("coordx");
                int sy = eManager.currentMap.scene.tiles().get(st).getInt("coordy");
                int sw = eManager.currentMap.scene.tiles().get(st).getInt("dimw");
                int sh = eManager.currentMap.scene.tiles().get(st).getInt("dimh");
                g2.setColor(new Color(255, 0, 200));
                g2.drawRect(eUtils.scaleInt(sx-cVars.getInt("camx")),
                        eUtils.scaleInt(sy-cVars.getInt("camy")),
                        eUtils.scaleInt(sw), eUtils.scaleInt(sh));
            }
            // -- selected prop
            if(eManager.currentMap.scene.props().size() > cEditorLogic.state.selectedPropId) {
                int st = cEditorLogic.state.selectedPropId;
                int sx = eManager.currentMap.scene.props().get(st).getInt("coordx");
                int sy = eManager.currentMap.scene.props().get(st).getInt("coordy");
                int sw = eManager.currentMap.scene.props().get(st).getInt("dimw");
                int sh = eManager.currentMap.scene.props().get(st).getInt("dimh");
                g2.setColor(new Color(255, 150, 0));
                g2.drawRect(eUtils.scaleInt(sx-cVars.getInt("camx")),
                        eUtils.scaleInt(sy-cVars.getInt("camy")),
                        eUtils.scaleInt(sw), eUtils.scaleInt(sh));
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
            int wt = cEditorLogic.state.newTile.getInt("dimw");
            int ht = cEditorLogic.state.newTile.getInt("dimh");
            int wp = cEditorLogic.state.newProp.getInt("dimw");
            int hp = cEditorLogic.state.newProp.getInt("dimh");
            int wf = cEditorLogic.state.newFlare.getInt("dimw");
            int hf = cEditorLogic.state.newFlare.getInt("dimh");
            int w = cEditorLogic.state.createObjCode == gScene.THING_FLARE ? wf
                    : cEditorLogic.state.createObjCode == gScene.THING_PROP ? wp : wt;
            int h = cEditorLogic.state.createObjCode == gScene.THING_FLARE ? hf
                    : cEditorLogic.state.createObjCode == gScene.THING_PROP ? hp : ht;
            int px = eUtils.roundToNearest(eUtils.unscaleInt(mousex - window_offsetx)
                    +cVars.getInt("camx")-w/2, cEditorLogic.state.snapToX) - cVars.getInt("camx");
            int py = eUtils.roundToNearest(eUtils.unscaleInt(mousey - window_offsety)
                    +cVars.getInt("camy")-h/2, cEditorLogic.state.snapToY) - cVars.getInt("camy");
            g2.setColor(new Color(50, 255, 100));
            g2.drawRect(eUtils.scaleInt(px), eUtils.scaleInt(py),
                    eUtils.scaleInt(w), eUtils.scaleInt(h));
        }
    }
}
