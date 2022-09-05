import java.util.concurrent.ThreadLocalRandom;

public class xComGetRandom extends xCom {
    // usage: getrand $min $max
    public String doCommand(String fullCommand) {
        if(eUtils.argsLength(fullCommand) < 3)
            return "0";
        String[] args = eUtils.parseScriptArgsServer(fullCommand);
        int start = Integer.parseInt(args[1]);
        int end = Integer.parseInt(args[2]);
        return Integer.toString(ThreadLocalRandom.current().nextInt(start, end + 1));
    }
}
