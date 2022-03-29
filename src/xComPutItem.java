public class xComPutItem extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 2) {
            gItemFactory factory = gItemFactory.instance();
            String itemTitle = toks[1];
            if(factory.itemLoadMap.containsKey(itemTitle)) {
                gDoableItemReturn itemReturn = factory.itemLoadMap.get(itemTitle);
                String argString = fullCommand.replace(toks[0] + " ",
                        "").replace(toks[1] + " ", "");
                String[] args = argString.split(" ");
                gItem newItem = itemReturn.getItem(args);
                newItem.putInt("itemid", cClientLogic.itemId);
                int itemId = cServerLogic.scene.itemIdCtr;
                newItem.putInt("id", itemId);
                newItem.putInt("itemid", itemId);
                cServerLogic.scene.getThingMap("THING_ITEM").put(Integer.toString(itemId), newItem);
                cServerLogic.scene.getThingMap(newItem.get("type")).put(Integer.toString(itemId), newItem);
                cServerLogic.scene.itemIdCtr += 1;
            }
        }
        return "usage: putitem <ITEM_TITLE> <args>";
    }
}
