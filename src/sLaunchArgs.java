public class sLaunchArgs {

    public static void readLaunchArguments(String argname, String[] args) {
        if(!argname.equals(args[0])) {
            xCon.instance().log("Bad argument pair: " + argname+","+args[0]);
        }
        else {
            StringBuilder s = new StringBuilder("");
            for(int i = 1; i < args.length; i++) {
                s.append(args[i] + " ");
            }
            sVars.put(argname,s.toString().substring(0, s.toString().length()-1));
        }
    }

	public static void setLaunchArguments(String[] args) {
	    sVars.loadFromFile(sSettings.CONFIG_FILE_LOCATION);
		for (int i = 0; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("-editor")) {
                sSettings.show_mapmaker_ui = true;
                sVars.put("startpaused", "1");
            }
            else if (args.length >= i+1) {
                sVars.put(args[i], args[i+1]);
                i+=1;
            }
		}
	}
}
