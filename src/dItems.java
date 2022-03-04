import java.awt.*;

public class dItems {
    public static void drawItem(Graphics2D g2, gItem item) {
        if(item.sprite != null) {
            g2.drawImage(item.sprite,
                    item.getInt("coordx"),
                    item.getInt("coordy"),
                    null
            );
            if(item.get("type").equals("ITEM_TELEPORTER_RED")) {
                if(sSettings.vfxenableflares)
                    dFlares.drawFlare(g2,
                            item.getInt("coordx")
                                    - item.getInt("dimw")/2,
                            item.getInt("coordy")
                                    - item.getInt("dimh")/2,
                            item.getInt("dimw")*2,
                            item.getInt("dimh")*2,
                            1, new int[]{255,0,0,255}, new int[4]
                    );
            }
            if(item.get("type").equals("ITEM_TELEPORTER_BLUE")) {
                if(sSettings.vfxenableflares)
                    dFlares.drawFlare(g2,
                            item.getInt("coordx") - item.getInt("dimw")/2,
                            item.getInt("coordy") - item.getInt("dimh")/2,
                            item.getInt("dimw")*2,
                            item.getInt("dimh")*2,
                            1, new int[]{0,0,255,255}, new int[4]);
            }
        }
        else if(sSettings.show_mapmaker_ui){
            g2.setColor(gColors.getFontColorFromName("spawnpoint"));
            g2.fillRect(
                    item.getInt("coordx"),
                    item.getInt("coordy"),
                    item.getInt("dimw"),
                    item.getInt("dimh"));
        }
    }
}
