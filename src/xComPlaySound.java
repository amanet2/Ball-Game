import javafx.scene.media.AudioClip;

public class xComPlaySound extends xCom {
    double sfxrange = 1800.0;
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 1 && sSettings.audioenabled) {
            AudioClip soundClip = new AudioClip(getClass().getResource(eUtils.getPath(toks[1])).toString());
            if(toks.length > 2) {
                int cycs = Integer.parseInt(toks[2]);
                soundClip.setCycleCount(cycs);
                if(cycs < 1)
                    soundClip.setCycleCount(AudioClip.INDEFINITE);
            }
            if(toks.length > 4) {
                int diffx = gCamera.getX()+ eUtils.unscaleInt(sSettings.width)/2-Integer.parseInt(toks[3]);
                int diffy = gCamera.getY()+ eUtils.unscaleInt(sSettings.height)/2-Integer.parseInt(toks[4]);
                double balance = 0.0;
                double ratio = Math.abs(diffx/(sfxrange-300));
                if(diffx < 0)
                    balance = ratio;
                else if(diffx > 0)
                    balance = -ratio;
                soundClip.setBalance(balance);
                soundClip.play((sfxrange/Math.sqrt(Math.pow((diffx),2)+Math.pow((diffy),2)))
                        *((double)sVars.getInt("volume")/100.0));
                oAudio.instance().clips.add(soundClip);
            }
            else {
                soundClip.play((double) sVars.getInt("volume") / 100.0);
                oAudio.instance().clips.add(soundClip);
            }
        }
        return fullCommand;
    }
}
