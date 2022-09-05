import java.text.NumberFormat;
import java.text.ParseException;

public class xComTestResLteInt extends xCom {
    //usage: testreslte $res $val <string that will exec if res <= val>
    public String doCommand(String fullCommand) {
        if(eUtils.argsLength(fullCommand) < 3)
            return "0";
        String[] args = eUtils.parseScriptArgsServer(fullCommand);
        String tk = args[1];
        String tv = args[2];
        StringBuilder esb = new StringBuilder();
        for(int i = 3; i < args.length; i++) {
            esb.append(" ").append(args[i]);
        }
        String es = esb.substring(1);
        Number n1 = null;
        Number n2 = null;
        try {
            n1 = NumberFormat.getInstance().parse(tk);
            n2 = NumberFormat.getInstance().parse(tv);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        boolean n1d = n1 instanceof Double;
        boolean n1l = n1 instanceof Long;
        boolean n2d = n2 instanceof Double;
        boolean n2l = n2 instanceof Long;
        if(n1d && n2d) {
            if((double) n1 <= (double) n2)
                return success(es);
        }
        else if(n1l && n2d) {
            if(Long.parseLong(tk) <= Double.parseDouble(tv))
                return success(es);
        }
        else if(n1d && n2l) {
            if(Double.parseDouble(tk) <= Long.parseLong(tv))
                return success(es);
        }
        else if(n1l && n2l) {
            if((long) n1 <= (long) n2)
                return success(es);
        }
        //default
        if(Double.parseDouble(tk) <= Double.parseDouble(tv))
            return success(es);
        return "0";
    }

    private String success(String es) {
        xCon.ex(es);
        return "1";
    }
}
