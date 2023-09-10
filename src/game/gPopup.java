package game;

import java.awt.*;

public class gPopup extends gThing {
    String text;

    public gPopup(String text, int x, int y) {
        super();
        this.coords = new int[]{x, y};
        this.text = text;
    }

    public void draw(Graphics2D g2) {
        // look for hashtag color codes here
        StringBuilder ts = new StringBuilder();
        for(String word : text.split(" ")) {
            if(word.contains("#")) {
                if(word.split("#").length != 2)
                    ts.append(word).append(" ");
                else if(gColors.getColorFromName("clrp_" + word.split("#")[1].replace(":","")) != null){
                    g2.setColor(Color.BLACK);
                    g2.drawString(
                            word.split("#")[0]+" ",
                            coords[0] + dFonts.getStringWidth(g2, ts.toString())+3,
                            coords[1] + 3
                    );
                    g2.setColor(gColors.getColorFromName("clrp_" + word.split("#")[1].replace(":","")));
                    g2.drawString(word.split("#")[0]+" ",
                            coords[0] + dFonts.getStringWidth(g2, ts.toString()),
                            coords[1]);
                    dFonts.setFontColor(g2, "clrf_normal");
                    ts.append(word.split("#")[0]).append(word.contains(":") ? ": " : " ");
                    continue;
                }
            }
            g2.setColor(Color.BLACK);
            g2.drawString(
                    word.split("#")[0]+" ",
                    coords[0] + dFonts.getStringWidth(g2, ts.toString())+3,
                    coords[1] + 3
            );
            dFonts.setFontColor(g2, "clrf_normal");
            g2.drawString(
                    word.split("#")[0]+" ",
                    coords[0] + dFonts.getStringWidth(g2, ts.toString()),
                    coords[1]
            );
            ts.append(word).append(" ");
        }
    }
}
