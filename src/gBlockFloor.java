import java.awt.*;

public class gBlockFloor extends gBlock {

    public gBlockFloor(String id, String prefabId, int x, int y) {
        super(id, prefabId, x, y, 1200, 1200);
        this.type = "BLOCK_FLOOR";
    }

    public void draw(Graphics2D g2) {
        g2.setPaint(xMain.shellLogic.floorTextures[sSettings.mapTheme]);
        g2.fillRect(coords[0], coords[1], dims[0], dims[1]);
        g2.setColor(gColors.getColorFromName("clrw_floorshading"));
        g2.fillRect(coords[0], coords[1], dims[0], dims[1]);
    }

    public void drawPreview(Graphics2D g2) {
        dFonts.setFontColor(g2, "clrw_floorcolorpreview");
        g2.fillRect(
                eUtils.scaleInt(coords[0]/4), eUtils.scaleInt(coords[1]/4),
                eUtils.scaleInt(dims[0]/4), eUtils.scaleInt(dims[1]/4)
        );
    }
}