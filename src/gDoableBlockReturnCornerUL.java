public class gDoableBlockReturnCornerUL extends gDoableBlockReturn{
    public gBlock getBlock(String[] args) {
        gBlockCornerUL block = new gBlockCornerUL(
                Integer.parseInt(args[0]),
                Integer.parseInt(args[1]),
                Integer.parseInt(args[2]),
                Integer.parseInt(args[3]),
                Integer.parseInt(args[4]),
                Integer.parseInt(args[5]),
                args[6],
                args[7]
        );
        return block;
    }
}
