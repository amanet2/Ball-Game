public class gDoableBlockReturnCornerLR extends gDoableBlockReturn{
    public gBlock getBlock(String[] args) {
        gBlockCornerLR block = new gBlockCornerLR(
                Integer.parseInt(args[0]),
                Integer.parseInt(args[1]),
                Integer.parseInt(args[2]),
                Integer.parseInt(args[3]),
                Integer.parseInt(args[4]),
                Integer.parseInt(args[5]),
                args[6],
                args[7]
        );
        if(args.length > 8)
            block.put("frontwall", args[8]);
        return block;
    }
}
