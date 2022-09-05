import java.text.NumberFormat;
import java.text.ParseException;

public class xComGetAddInt extends xCom {
    public String doCommand(String fullCommand) {
        //usage: getaddint $result $num1 $num2
        if(eUtils.argsLength(fullCommand) < 4)
            return "null";
        String[] args = eUtils.parseScriptArgsServer(fullCommand);
        String tk = args[1];
        Number n1 = null;
        Number n2 = null;
        try {
            n1 = NumberFormat.getInstance().parse(args[2]);
            n2 = NumberFormat.getInstance().parse(args[3]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        boolean n1d = n1 instanceof Double;
        boolean n1l = n1 instanceof Long;
        boolean n2d = n2 instanceof Double;
        boolean n2l = n2 instanceof Long;
        if(n1d || n2d)
            cServerVars.instance().put(tk, Integer.toString((int) ((double) n1 + (double) n2)));
        else if(n1l || n2l)
            cServerVars.instance().put(tk, Long.toString((long) n1 + (long) n2));
        else
            cServerVars.instance().put(tk, Integer.toString((int) n1 + (int) n2));
        return cServerVars.instance().get(tk);
    }
}
