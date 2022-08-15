import java.util.concurrent.ThreadLocalRandom;

public class xComGetRandom extends xCom {
    // usage: getrand $min $max
    public String doCommand(String fullCommand) {
        String[] args = fullCommand.split(" ");
        if(args.length < 3)
            return "0";
        for(int i = 1; i < args.length; i++) {
            if(args[i].startsWith("$")) {
                if(cServerVars.instance().contains(args[i].substring(1)))
                    args[i] = cServerVars.instance().get(args[i].substring(1));
                else if(sVars.get(args[i]) != null)
                    args[i] = sVars.get(args[i]);
            }
        }
        int start = Integer.parseInt(args[1]);
        int end = Integer.parseInt(args[2]);
        return Integer.toString(ThreadLocalRandom.current().nextInt(start, end + 1));
    }
}
