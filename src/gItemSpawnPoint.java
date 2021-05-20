import java.awt.*;

public class gItemSpawnPoint extends gItem {
    public gItemSpawnPoint(int x, int y) {
        super(x, y, 300, 300);
        put("type", "ITEM_SPAWNPOINT");
    }

    public boolean isOccupied() {
        for(String id : cServerLogic.scene.getThingMap("THING_PLAYER").keySet()) {
            gPlayer player = (gPlayer) cServerLogic.scene.getThingMap("THING_PLAYER").get(id);
            Shape bounds = new Rectangle(
                    getInt("coordx"), getInt("coordy"), getInt("dimw"), getInt("dimh"));
            return bounds.intersects(new Rectangle(
                    player.getInt("coordx"), player.getInt("coordy"), player.getInt("dimw"),
                    player.getInt("dimh")));
        }
        return false;
    }
}
