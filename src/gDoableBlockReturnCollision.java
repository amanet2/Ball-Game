public class gDoableBlockReturnCollision extends gDoableBlockReturn{
    public gBlock getBlock(String[] args) {
        gBlockCollision block = new gBlockCollision(
                Integer.parseInt(args[0]),
                Integer.parseInt(args[1]),
                Integer.parseInt(args[2]),
                Integer.parseInt(args[3])
        );
        return block;
    }
}
