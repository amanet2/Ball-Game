import javafx.scene.media.AudioClip;

import java.util.ArrayList;

public class oAudio{
    private static oAudio instance = null;
    ArrayList<AudioClip> clips;
    public static oAudio instance() {
        if (instance == null)
            instance = new oAudio();
        return instance;
    }

    private oAudio() {
        clips = new ArrayList<>();
    }

    public void checkAudio() {
        if(clips.size() > 0){
            ArrayList<AudioClip> tr = new ArrayList<>();
            for (AudioClip c : clips) {
                if (!c.isPlaying()) {
                    tr.add(c);
                }
            }
            for (AudioClip c : tr) {
                clips.remove(c);
            }
        }
    }
}
