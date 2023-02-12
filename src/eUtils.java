import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class eUtils {
    static double zoomLevel = 1.0;

    public static int argsLength(String full) {
        return full.trim().split(" ").length;
    }

    public static String[] parseScriptArgsAllSources(String full) {
        String[] args = full.trim().split(" ");
        for(int i = 0; i < args.length; i++) {
            if(!args[i].startsWith("$"))
                continue;
            if(cServerVars.instance().contains(args[i].substring(1)))
                args[i] = cServerVars.instance().get(args[i].substring(1));
            else if(cClientVars.instance().contains(args[i].substring(1)))
                args[i] = cClientVars.instance().get(args[i].substring(1));
            else if(sVars.get(args[i]) != null)
                args[i] = sVars.get(args[i]);
        }
        return args;
    }

    public static String[] parseScriptArgsServer(String full) {
        String[] args = full.trim().split(" ");
        for(int i = 0; i < args.length; i++) {
            if(!args[i].startsWith("$"))
                continue;
            if(cServerVars.instance().contains(args[i].substring(1)))
                args[i] = cServerVars.instance().get(args[i].substring(1));
            else if(sVars.get(args[i]) != null)
                args[i] = sVars.get(args[i]);
        }
        return args;
    }

    public static String[] parseScriptArgsClient(String full) {
        String[] args = full.trim().split(" ");
        for(int i = 0; i < args.length; i++) {
            if(!args[i].startsWith("$"))
                continue;
            if(cClientVars.instance().contains(args[i].substring(1)))
                args[i] = cClientVars.instance().get(args[i].substring(1));
            else if(sVars.get(args[i]) != null)
                args[i] = sVars.get(args[i]);
        }
        return args;
    }

    public static String getPath(String s) {
        return sSettings.datapath + "/" + s;
    }

    public static String getTimeString(Long l) {
        Date date = new Date(l);
        DateFormat formatter = new SimpleDateFormat("mm:ss");
        return formatter.format(date);
    }

	public static int scaleInt(int inputInt) {
		return (int) ((((double) inputInt / sSettings.gamescale) * (double) sSettings.height));
	}

	public static int unscaleInt(int inputInt) {
		return (int) ((((double) inputInt * sSettings.gamescale) / (double) sSettings.height));
	}

	public static int roundToNearest(int val, int snap) {
		return (Math.round(val/snap))*snap;
	}
}
