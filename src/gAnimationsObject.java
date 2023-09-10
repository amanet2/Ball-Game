import java.awt.Image;
import java.io.File;

public class gAnimationsObject {
    int[] dims;
    Image[] frames;
    File[] fpContents;
    int framerate;

    public gAnimationsObject(String folder, int w, int h, int r) {
        dims = new int[]{w, h};
        framerate = r;
        File fp = new File(eManager.getPath(folder));
        fpContents = fp.listFiles();
        frames = new Image[fpContents.length];
        for(int i = 0; i < frames.length; i++) {
            frames[i] = gTextures.getGScaledImage(fpContents[i].getPath(), dims[0], dims[1]);
        }
    }
}
