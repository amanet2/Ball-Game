import java.awt.*;

public class gItemSpawnPoint extends gItem {
    public gItemSpawnPoint(int x, int y) {
        super("ITEM_SPAWNPOINT", x, y, 300, 300, null);
        put("occupied", "0");
    }

    public void activateItem(gPlayer player) {
        if(get("occupied").equals("0")) { //used to suppress spawnpoint noise in log
            super.activateItem(player);
            xCon.ex("exec items/spawnpoint");
        }
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
