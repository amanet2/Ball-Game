public class gDoableBlockReturnFloor extends gDoableBlockReturn{
    public gBlock getBlock(String[] args) {
        return new gBlockFloor(
                Integer.parseInt(args[0]),
                Integer.parseInt(args[1]),
                Integer.parseInt(args[2]),
                Integer.parseInt(args[3])
        );
    }
}
