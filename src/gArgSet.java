import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

public class gArgSet {
    protected HashMap<String, gArg> args;
    protected static ArrayList<String> filelines = new ArrayList<>();
//    protected static gArgSet instance;

    protected gArgSet() {
        args = new HashMap<>();
    }

    public boolean contains(String key) {
        return args.containsKey(key);
    }

    public void loadFromFile(String s) {
        System.out.println("FUCK YOU");
        try (BufferedReader br = new BufferedReader(new FileReader(s))) {
            xCon.instance().log("Loading Settings File Path: " + s);
            String line;
            while ((line = br.readLine()) != null) {
                filelines.add(line);
                String[] args = line.split(" ");
                String argname = args[0];
                if(argname.trim().replace(" ","").charAt(0) != '#') //filter out comments
                    put(argname, line.replaceFirst(argname + " ", ""));
            }
            xCon.instance().log(args.toString());
        }
        catch (Exception e) {
            eUtils.echoException(e);
            e.printStackTrace();
        }
    }

    protected void init() {

    }

    protected void putArg(gArg arg) {
        args.put(arg.key, arg);
        arg.onChange();
    }

    protected gArg getArg(String key) {
        return args.get(key);
    }

    public String get(String key) {
        if(args.containsKey(key))
            return args.get(key).value;
        return null;
    }

    public int getInt(String key) {
        if(args.containsKey(key))
            return Integer.parseInt(args.get(key).value);
        return -1;
    }

    public void put(String key, String val) {
        gArg arg = getArg(key);
        if(arg != null) {
            arg.value = val;
            arg.onChange();
        }
        else
            xCon.instance().debug("gArgSet.put: no arg for key: " + key);
    }

//    private static gArgSet instance() {
//        assert  instance != null : "Cannot create abstract argset";
//        return instance;
//    }
}
