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
                newItem.put("itemid", cVars.get("itemid"));
                if(sSettings.IS_CLIENT)
                    itemReturn.storeItem(newItem, cClientLogic.scene);
                if(sSettings.IS_SERVER)
                    itemReturn.storeItem(newItem, cServerLogic.scene);
            }
        }
        return "usage: putitem <ITEM_TITLE> <args>";
    }
}
