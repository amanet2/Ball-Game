public class xComPutBlock extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 2) {
            gBlockFactory factory = gBlockFactory.instance();
            String blockString = toks[1];
            if(factory.blockLoadMap.containsKey(blockString)) {
                gDoableBlockReturn blockReturn = factory.blockLoadMap.get(blockString);
                String argString = fullCommand.replace(toks[0] + " ",
                        "").replace(toks[1] + " ", "");
                String[] args = argString.split(" ");
                blockReturn.storeBlock(blockReturn.getBlock(args), eManager.currentMap.scene);
            }
        }
        return "usage: putblock <BLOCK_TITLE> <args>";
    }
}
