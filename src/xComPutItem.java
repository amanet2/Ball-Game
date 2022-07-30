public class xComPutItem extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length < 5)
            return escape();
        gItemFactory factory = gItemFactory.instance();
        String itemTitle = toks[1];
        if(factory.itemLoadMap.containsKey(itemTitle)) {
            String itemId = toks[2];
            String putX = toks[3];
            String putY = toks[4];
            gDoableItemReturn itemReturn = factory.itemLoadMap.get(itemTitle);
            gItem newItem = itemReturn.getItem(new String[]{putX, putY}); //expects x y as args
            newItem.put("id", itemId);
            cServerLogic.scene.getThingMap("THING_ITEM").put(itemId, newItem);
            cServerLogic.scene.getThingMap(newItem.get("type")).put(itemId, newItem);
            nServer.instance().addExcludingNetCmd("server", "cl_"+fullCommand);
            return "put item";
        }
        return escape();
    }

    private String escape() {
        return "usage: putitem <ITEM_TITLE> <id> <x> <y>";

    }
}
