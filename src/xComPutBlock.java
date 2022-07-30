public class xComPutBlock extends xCom {
    public String doCommand(String fullCommand) {
        String[] toks = fullCommand.split(" ");
        // putblock BLOCK_FLOOR id prefabid x y width height
        // putblock BLOCK_CUBE id prefabid x y width height top mid
        if (toks.length < 8)
            return escape();
        gBlockFactory factory = gBlockFactory.instance();
        String blockString = toks[1];
        String blockid = toks[2];
        String prefabid = toks[3];
        if (factory.blockLoadMap.containsKey(blockString)) {
            gDoableBlockReturn blockReturn = factory.blockLoadMap.get(blockString);
            String rawX = toks[4];
            String rawY = toks[5];
            String width = toks[6];
            String height = toks[7];
            //args are x y w h (t m)
            String[] args = new String[toks.length - 4];
            args[0] = rawX;
            args[1] = rawY;
            args[2] = width;
            args[3] = height;
            if (rawX.charAt(0) == '$') {
                int transformedX;
                String[] rxtoksadd = rawX.split("\\+");
                String[] rxtokssub = rawX.split("-");
                if (rxtoksadd.length > 1) {
                    int rxmod0 = sVars.getInt(rxtoksadd[0]);
                    int rxmod1 = Integer.parseInt(rxtoksadd[1]);
                    transformedX = rxmod0 + rxmod1;
                } else if (rxtokssub.length > 1) {
                    int rxmod0 = sVars.getInt(rxtokssub[0]);
                    int rxmod1 = Integer.parseInt(rxtokssub[1]);
                    transformedX = rxmod0 - rxmod1;
                } else {
                    transformedX = sVars.getInt(rawX);
                }
                args[0] = Integer.toString(transformedX);
            }

            if (rawY.charAt(0) == '$') {
                int transformedY;
                String[] rytoksadd = rawY.split("\\+");
                String[] rytokssub = rawY.split("-");
                if (rytoksadd.length > 1) {
                    int rymod0 = sVars.getInt(rytoksadd[0]);
                    int rymod1 = Integer.parseInt(rytoksadd[1]);
                    transformedY = rymod0 + rymod1;
                } else if (rytokssub.length > 1) {
                    int rymod0 = sVars.getInt(rytokssub[0]);
                    int rymod1 = Integer.parseInt(rytokssub[1]);
                    transformedY = rymod0 - rymod1;
                } else {
                    transformedY = sVars.getInt(rawY);
                }
                args[1] = Integer.toString(transformedY);
            }
            if (args.length > 4) {
                args[4] = toks[8];
                args[5] = toks[9];
            }
            gBlock newBlock = blockReturn.getBlock(args);
            newBlock.put("id", blockid);
            newBlock.put("prefabid", prefabid);
            cServerLogic.scene.getThingMap("THING_BLOCK").put(blockid, newBlock);
            cServerLogic.scene.getThingMap(newBlock.get("type")).put(blockid, newBlock);
        }
        return String.format("put block %s id%s pid%s", blockString, blockid, prefabid);
    }

    private String escape() {
        return "usage: putblock <BLOCK_TITLE> <id> <pid> <x> <y> <w> <h>. opt: <t> <m> ";
    }
}
