import java.awt.*;

public class gItem extends gThing {
    int occupied = 0;
    String flare = "null";
    String script = "null";

    public void activateItem(gThing p) {
        if(!script.equals("null"))
            xMain.shellLogic.console.ex(String.format("%s %s %s", script, id, p.id));
    }

    public gItem(String id, String type, int x, int y, int w, int h, String sp) {
        super();
        this.id = id;
        this.type = type;
        if(type.trim().length() < 1)
            this.type = "null";
        coords[0] = x;
        coords[1] = y;
        dims[0] = w;
        dims[1] = h;
        sprite = sp.equalsIgnoreCase("null") ? null : gTextures.getGScaledImage(eManager.getPath(sp), dims[0], dims[1]);
    }

    public void draw(Graphics2D g2) {
        if(sprite != null) {
            //item shadow
            drawRoundShadow(g2);
            g2.drawImage(
                    sprite,
                    coords[0],
                    coords[1],
                    null
            );
            if(sSettings.vfxenableflares && !flare.equals("null")) {
                String[] flareToks = flare.split(":");
                int[] flareArgs = new int[] {
                        Integer.parseInt(flareToks[0]),
                        Integer.parseInt(flareToks[1]),
                        Integer.parseInt(flareToks[2]),
                        Integer.parseInt(flareToks[3])
                };
                dScreenFX.drawFlareFromColor(g2,
                        coords[0] - dims[0]/2,
                        coords[1] - dims[1]/2,
                        dims[0]*2,
                        dims[1]*2,
                        1,
                        new Color(flareArgs[0], flareArgs[1], flareArgs[2], flareArgs[3]),
                        new Color(0,0,0,0)
                );
            }
        }
        else if(sSettings.show_mapmaker_ui){
            dFonts.setFontColor(g2, "clrf_spawnpoint");
            g2.fillRect(coords[0], coords[1], dims[0], dims[1]);
        }
    }
}
