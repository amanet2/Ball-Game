import java.util.Collection;

public class cClientLogic {
    public static gPlayer getUserPlayer() {
        return xMain.shellLogic.clientScene.getPlayerById(uiInterface.uuid);
    }

    public static Collection<String> getPlayerIds() {
        return xMain.shellLogic.clientScene.getThingMap("THING_PLAYER").keySet();
    }

    public static gPlayer getPlayerById(String id) {
        if(!xMain.shellLogic.clientScene.getThingMap("THING_PLAYER").containsKey(id))
            return null;
        return (gPlayer) xMain.shellLogic.clientScene.getThingMap("THING_PLAYER").get(id);
    }

    public static int getNewItemId() {
        int itemId = 0;
        for(String id : xMain.shellLogic.clientScene.getThingMap("THING_ITEM").keySet()) {
            if(itemId < Integer.parseInt(id))
                itemId = Integer.parseInt(id);
        }
        return itemId+1; //want to be the _next_ id
    }
}
