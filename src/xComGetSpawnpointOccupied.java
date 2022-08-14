import java.awt.*;

public class xComGetSpawnpointOccupied extends xCom {
    public String doCommand(String fullCommand) {
        //usage: getspawnpointoccupied $id
        String[] args = fullCommand.split(" ");
        if(args.length < 2)
            return "0";
        String spid = args[1];
        gThing sp = cServerLogic.scene.getThingMap("ITEM_SPAWNPOINT").get(spid);
        if(sp == null)
            return "0";
        for(String id : cServerLogic.scene.getThingMap("THING_PLAYER").keySet()) {
            gPlayer player = (gPlayer) cServerLogic.scene.getThingMap("THING_PLAYER").get(id);
            Shape bounds = new Rectangle(
                    sp.getInt("coordx"), sp.getInt("coordy"), sp.getInt("dimw"), sp.getInt("dimh"));
            if(bounds.intersects(new Rectangle(player.getInt("coordx"), player.getInt("coordy"),
                    player.getInt("dimw"), player.getInt("dimh"))))
                return "1";
        }
        return "0";
    }
}
