import javax.swing.JMenuItem;

public class cEditorLogicState {
    int snapToX;
    int snapToY;
    int selectedPropId;
    int selectedFlareTag;
    int createObjCode;
    gScene mapScene;
    gProp newProp;
    gFlare newFlare;

    public cEditorLogicState(int stx, int sty, int spi, int sfi, int coc, gProp np, gFlare nf, gScene ms) {
        newProp = np;
        newFlare = nf;
        snapToX = stx;
        snapToY = sty;
        createObjCode = coc;
        selectedPropId = spi;
        selectedFlareTag = sfi;
        mapScene = ms;
    }
}
