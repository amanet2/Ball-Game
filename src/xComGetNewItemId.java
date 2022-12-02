public class xComGetNewItemId extends xCom {
    public String doCommand(String fullCommand) {
        int itemId = 0;
        for(String id : cServerLogic.scene.getThingMap("THING_ITEM").keySet()) {
            if(itemId < Integer.parseInt(id))
                itemId = Integer.parseInt(id);
        }
        return Integer.toString(itemId+1); //want to be the NEXT id
    }
}
