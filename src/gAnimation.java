import java.awt.*;
import java.io.File;

public class gAnimation {
    int[] dims;
    Image[] frames;
    File[] fpContents;
    int framerate;

    public gAnimation(String folder, int w, int h, int r) {
        dims = new int[]{w, h};
        framerate = r;
        File fp = new File(eUtils.getPath(folder));
        fpContents = fp.listFiles();
        frames = new Image[fpContents.length];
        for(int i = 0; i < frames.length; i++) {
            frames[i] = gTextures.getGScaledImage(fpContents[i].getPath(), dims[0], dims[1]);
        }
    }
}
