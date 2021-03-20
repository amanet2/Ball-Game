public class xComDeleteBlock extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        if(toks.length > 1) {
//            ArrayList<gBlock> blocks = eManager.currentMap.scene.blocks();
//            int tag = Integer.parseInt(toks[1]);
//            gBlock block = blocks.get(tag);
//            String code = gBlocks.getTitleForBlock(block);
//            blocks.remove(block);
//            eManager.currentMap.scene.getThingMap(code).remove(Integer.toString(tag));
//            return "removed block tag: " + tag;
        }
        return "usage: deleteblock <tag>";
    }
}
