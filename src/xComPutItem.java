public class xComPutItem extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length < 5)
            return "usage: putitem <ITEM_TITLE> <id> <x> <y>";
        String itemTitle = toks[1];
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
        item.put("occupied", "0");
        cServerLogic.scene.getThingMap("THING_ITEM").put(itemId, item);
        cServerLogic.scene.getThingMap(item.get("type")).put(itemId, item);
        return "put item";
    }
}
