import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class gArgSet {
    protected HashMap<String, gArg> args;
    protected ArrayList<String> filelines;

    protected gArgSet() {
        args = new HashMap<>();
        filelines = new ArrayList<>();
    }

    public boolean contains(String key) {
        return args.containsKey(key);
    }

    public void loadFromFile(String s) {
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

    public void loadFromLaunchArgs(String[] launchArgs) {
        for (int i = 0; i < launchArgs.length; i+=2) {
            if (launchArgs.length >= i+1) {
                put(launchArgs[i], launchArgs[i+1]);
            }
        }
    }

    public void saveToFile(String s) {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(s), StandardCharsets.UTF_8))) {
            for(String line : filelines) {
                String arg = line.split(" ")[0];
                if(contains(arg))
                    writer.write(String.format("%s %s", arg, get(arg)));
                else
                    writer.write(line);
                writer.write("\n");
            }
        } catch (IOException e) {
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
}
