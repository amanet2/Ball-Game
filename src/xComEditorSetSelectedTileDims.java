import javax.swing.*;
import java.awt.*;

public class xComEditorSetSelectedTileDims extends xCom {
    public String doCommand(String fullCommand) {
        gTile p = eManager.currentMap.scene.tiles().get(cEditorLogic.state.selectedTileId);
        if(p != null) {
            JTextField[] jfields = new JTextField[p.vars().keySet().size()];
            int i = 0;
            for(String s : p.vars().keySet()) {
                JTextField nf = new JTextField(p.get(s));
                nf.setToolTipText(s);
                jfields[i] = nf;
                i++;
            }
            if(JOptionPane.showInputDialog(oDisplay.instance(), jfields, "Selected Tile Settings",
                    JOptionPane.OK_CANCEL_OPTION) != null) {
                i = 0;
                for (String s : p.vars().keySet()) {
                    p.put(s, jfields[i].getText());
                    i++;
                }
                for(int j = 0; j < 5; j++) {
                    if(p.getInt(String.format("dim%dh", j)) != 0)
                        p.put(String.format("dim%dw", j), p.get("dimw"));
                    else
                        p.put(String.format("dim%dw", j), "0");
                }
                for(int j = 5; j < 7; j++) {
                    if(p.getInt(String.format("dim%dw", j)) != 0)
                        p.putInt(String.format("dim%dh", j),
                                p.getInt("dimh")-p.getInt("dim0h")-p.getInt("dim3h")-p.getInt("dim4h"));
                    else
                        p.putInt(String.format("dim%dh", j), 0);
                }
                p.sprites = new Image[] {
                        gTextures.getScaledImage(p.get("sprite0"), p.getInt("dim0w"), p.getInt("dim0h")),
                        gTextures.getScaledImage(p.get("sprite1"), p.getInt("dim1w"), p.getInt("dim1h")),
                        gTextures.getScaledImage(p.get("sprite2"), p.getInt("dim2w"), p.getInt("dim2h")),
                        gTextures.getScaledImage(p.get("sprite0"), p.getInt("dim3w"), p.getInt("dim3h")),
                        gTextures.getScaledImage(p.get("sprite1"), p.getInt("dim4w"), p.getInt("dim4h")),
                        gTextures.getScaledImage(p.get("sprite0"), p.getInt("dim5w"), p.getInt("dim5h")),
                        gTextures.getScaledImage(p.get("sprite0"), p.getInt("dim6w"), p.getInt("dim6h"))
                };
            }
        }
        return "";
    }
}
