public class xComPutItemClient extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length < 5)
            return escape();
        gItemFactory factory = gItemFactory.instance();
        String itemTitle = toks[1];
        if(itemTitle.equals("ITEM_FLAG")) {
            String itemId = toks[2];
            int iw = Integer.parseInt(xCon.ex("setvar " + itemTitle+"_dimw"));
            int ih = Integer.parseInt(xCon.ex("setvar " + itemTitle+"_dimh"));
            String isp = xCon.ex("setvar " + itemTitle + "_sprite");
            String isc = xCon.ex("setvar " + itemTitle + "_script");
            gItem item = new gItem(itemTitle, Integer.parseInt(toks[3]), Integer.parseInt(toks[4]), iw, ih,
                    isp.equalsIgnoreCase("null") ? null : gTextures.getGScaledImage(eUtils.getPath(isp),
                            iw, ih));
            if(!isc.equalsIgnoreCase("null"))
                item.put("script", isc);
            item.put("id", itemId);
            cClientLogic.scene.getThingMap("THING_ITEM").put(itemId, item);
            cClientLogic.scene.getThingMap(item.get("type")).put(itemId, item);
            return "put item";
        }
        if(factory.itemLoadMap.containsKey(itemTitle)) {
            String itemId = toks[2];
            String putX = toks[3];
            String putY = toks[4];
            gDoableThingReturn itemReturn = factory.itemLoadMap.get(itemTitle);
            gThing newItem = itemReturn.getThing(new String[]{putX, putY}); //expects x y as args
            newItem.put("id", itemId);
            cClientLogic.scene.getThingMap("THING_ITEM").put(itemId, newItem);
            cClientLogic.scene.getThingMap(newItem.get("type")).put(itemId, newItem);
            return "put item";
        }
        return escape();
    }

    private String escape() {
        return "usage: cl_putitem <ITEM_TITLE> <id> <x> <y>";

    }
}
