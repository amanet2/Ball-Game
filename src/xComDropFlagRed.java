public class xComDropFlagRed extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if (toks.length > 1) {
            String id = toks[1];
            gPlayer p = gScene.getPlayerById(id);
            xCon.ex("putitem ITEM_FLAGRED " + p.get("coordx") + " " + p.get("coordy"));
        }

        return "dropped flagred";
    }
}
