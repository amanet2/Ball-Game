import java.awt.*;

public class dItems {
    public static void drawItem(Graphics2D g2, gItem item) {
        if(item.sprite != null) {
            //item shadow
            dBlockShadows.drawThingShadow(g2, item);
            g2.drawImage(item.sprite,
                    item.getInt("coordx"),
                    item.getInt("coordy"),
                    null
            );
            if(sSettings.vfxenableflares && !item.get("flare").equals("null")) {
                String[] flareToks = item.get("flare").split(":");
                int[] flareArgs = new int[] {
                        Integer.parseInt(flareToks[0]),
                        Integer.parseInt(flareToks[1]),
                        Integer.parseInt(flareToks[2]),
                        Integer.parseInt(flareToks[3])
                };
                dFlares.drawFlare(g2,
                        item.getInt("coordx") - item.getInt("dimw")/2,
                        item.getInt("coordy") - item.getInt("dimh")/2,
                        item.getInt("dimw")*2,
                        item.getInt("dimh")*2,
                        1, new int[]{flareArgs[0], flareArgs[1], flareArgs[2], flareArgs[3]}, new int[4]
                );
            }
        }
        else if(sSettings.show_mapmaker_ui){
            dFonts.setFontColor(g2, "clrf_spawnpoint");
            g2.fillRect(
                    item.getInt("coordx"),
                    item.getInt("coordy"),
                    item.getInt("dimw"),
                    item.getInt("dimh"));
        }
    }
}
