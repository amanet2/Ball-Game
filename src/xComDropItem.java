public class xComDropItem extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if (toks.length > 2) {
            String id = toks[1];
            String itemTitle = toks[2];
            gPlayer p = gScene.getPlayerById(id);
            xCon.ex(String.format("putitem %s %s %s", itemTitle, p.get("coordx"), p.get("coordy")));
            return "dropped item " + itemTitle;
        }
        return "usage: dropitem <dropper_id> <ITEM_TITLE>";
    }
}
