import javax.swing.JMenuItem;

public class cEditorLogicState {
    int snapToX;
    int snapToY;
    String selectedTitle;
    JMenuItem selectedTileMenuItem;
    JMenuItem[] selectedTextureMenuItems;
    int selectedTileId;
    int selectedPropId;
    String selectedFlareId;
    int createObjCode;
    gScene mapScene;
    gTile newTile;
    gProp newProp;
    gFlare newFlare;

    public cEditorLogicState(int stx, int sty, String st, JMenuItem stmi, int sti, int spi, String sfi, int coc, gTile nt,
                             gProp np, gFlare nf, gScene ms) {
        newTile = nt;
        newProp = np;
        newFlare = nf;

        snapToX = stx;
        snapToY = sty;
        selectedTitle = st;
        selectedTileMenuItem = stmi;
        selectedTextureMenuItems = new JMenuItem[]{new JMenuItem(newTile.get("sprite0")),
                new JMenuItem(newTile.get("sprite1")), new JMenuItem(newTile.get("sprite2"))};
        createObjCode = coc;
        selectedTileId = sti;
        selectedPropId = spi;
        selectedFlareId = sfi;
        mapScene = ms;
    }
}
