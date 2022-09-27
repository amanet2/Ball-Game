import java.util.Arrays;

public class xComForEach extends xCom {
    //usage: foreach $var $start $end $incr <script to execute where $var is preloaded>
    public String doCommand(String fullCommand) {
        if(eUtils.argsLength(fullCommand) < 6)
            return "usage: foreach $var $start $end $incr <script where $var is num>";
        String[] args = eUtils.parseScriptArgsServer(fullCommand);
        String varname = args[1];
        int start = Integer.parseInt(args[2]);
        int end = Integer.parseInt(args[3]);
        int incr = Integer.parseInt(args[4]);
        for(int i = start; i <= end; i+=incr) {
            xCon.ex(String.format("setvar %s %s", varname, i));
            String[] cargs = eUtils.parseScriptArgsServer(fullCommand);
            String[] subarray = Arrays.stream(cargs, 5, cargs.length).toArray(String[]::new);
            String es = String.join(" ", subarray);
            xCon.ex(es);
            cServerVars.instance().args.remove(varname); //why is this needed here and not in foreachthing???
        }
        return "usage: foreach $var $start $end $incr <script where $var is num>";
    }
}
