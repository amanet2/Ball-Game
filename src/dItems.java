import java.awt.*;

public class dItems {
    public static void drawItems(Graphics2D g2, gScene scene) {
        for(String itemId : scene.getThingMap("THING_ITEM").keySet()) {
            gItem item = (gItem) scene.getThingMap("THING_ITEM").get(itemId);
            if(item.sprite != null) {
                g2.drawImage(item.sprite,
                        eUtils.scaleInt(item.getInt("coordx") - cVars.getInt("camx")),
                        eUtils.scaleInt(item.getInt("coordy") - cVars.getInt("camy")),
                        null
                );
            }
            else if(sSettings.show_mapmaker_ui){
                g2.setColor(new Color(255, 150, 80, 150));
                g2.fillRect(eUtils.scaleInt(item.getInt("coordx") - cVars.getInt("camx")),
                        eUtils.scaleInt(item.getInt("coordy") - cVars.getInt("camy")),
                        eUtils.scaleInt(item.getInt("dimw")),
                        eUtils.scaleInt(item.getInt("dimh")));
            }
        }
        //flag for ctf
        for(String id : scene.getThingMap("THING_PLAYER").keySet()) {
            if(nClient.instance().serverArgsMap.containsKey("server")
                    && nClient.instance().serverArgsMap.get("server").containsKey("flagmasterid")
                    && nClient.instance().serverArgsMap.get("server").get("flagmasterid").equals(id)) {
                gPlayer player = (gPlayer) scene.getThingMap("THING_PLAYER").get(id);
                g2.drawImage(gItemFactory.instance().flagSprite,
                        eUtils.scaleInt(player.getInt("coordx") - cVars.getInt("camx")
                                - player.getInt("dimw")/2),
                        eUtils.scaleInt(player.getInt("coordy") - cVars.getInt("camy")
                                - player.getInt("dimh")),
                        null
                );
            }
        }
    }
}
