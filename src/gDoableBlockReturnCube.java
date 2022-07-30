import java.util.Arrays;

public class gDoableBlockReturnCube extends gDoableBlockReturn{
    public gBlock getBlock(String[] args) {
        System.out.println(Arrays.toString(args));
        gBlockCube block = new gBlockCube(
                Integer.parseInt(args[0]),
                Integer.parseInt(args[1]),
                Integer.parseInt(args[2]),
                Integer.parseInt(args[3]),
                Integer.parseInt(args[4]),
                Integer.parseInt(args[5])
        );
        return block;
    }
}
