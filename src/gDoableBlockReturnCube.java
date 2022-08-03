public class gDoableBlockReturnCube extends gDoableBlockReturn{
    public gBlock getBlock(String[] args) {
        return new gBlockCube(
                Integer.parseInt(args[0]),
                Integer.parseInt(args[1]),
                Integer.parseInt(args[2]),
                Integer.parseInt(args[3]),
                Integer.parseInt(args[4]),
                Integer.parseInt(args[5])
        );
    }
}
