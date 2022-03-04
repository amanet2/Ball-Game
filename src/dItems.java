import java.awt.*;

public class dItems {
    public static void drawItem(Graphics2D g2, gItem item) {
        if(item.sprite != null) {
            g2.drawImage(item.sprite,
                    eUtils.scaleInt(item.getInt("coordx")),
                    eUtils.scaleInt(item.getInt("coordy")),
                    null
            );
            if(item.get("type").equals("ITEM_TELEPORTER_RED")) {
                if(sSettings.vfxenableflares)
                    dFlares.drawFlare(g2,
                            eUtils.scaleInt(item.getInt("coordx")
                                    - item.getInt("dimw")/2),
                            eUtils.scaleInt(item.getInt("coordy")
                                    - item.getInt("dimh")/2),
                            eUtils.scaleInt(item.getInt("dimw")*2),
                            eUtils.scaleInt(item.getInt("dimh")*2),
                            1, new int[]{255,0,0,255}, new int[4]
                    );
            }
            if(item.get("type").equals("ITEM_TELEPORTER_BLUE")) {
                if(sSettings.vfxenableflares)
                    dFlares.drawFlare(g2,
                            eUtils.scaleInt(item.getInt("coordx")
                                    - item.getInt("dimw")/2),
                            eUtils.scaleInt(item.getInt("coordy")
                                    - item.getInt("dimh")/2),
                            eUtils.scaleInt(item.getInt("dimw")*2),
                            eUtils.scaleInt(item.getInt("dimh")*2),
                            1, new int[]{0,0,255,255}, new int[4]);
            }
        }
        else if(sSettings.show_mapmaker_ui){
            g2.setColor(gColors.getFontColorFromName("spawnpoint"));
            g2.fillRect(eUtils.scaleInt(item.getInt("coordx")),
                    eUtils.scaleInt(item.getInt("coordy")),
                    eUtils.scaleInt(item.getInt("dimw")),
                    eUtils.scaleInt(item.getInt("dimh")));
        }
    }
}
