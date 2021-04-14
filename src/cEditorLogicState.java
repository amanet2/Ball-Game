public class cEditorLogicState {
    int snapToX;
    int snapToY;
    gScene mapScene;

    public cEditorLogicState(int stx, int sty, gScene ms) {
        snapToX = stx;
        snapToY = sty;
        mapScene = ms;
    }
}
