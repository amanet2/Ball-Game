import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class gArgSet {
    protected HashMap<String, gArg> args;
    protected ArrayList<String> filelines;

    public String toString() {
        return args.toString();
    }

    protected gArgSet() {
        args = new HashMap<>();
        filelines = new ArrayList<>();
    }

    public boolean contains(String key) {
        return args.containsKey(key);
    }

    public void loadFromFile(String s) {
        try (BufferedReader br = new BufferedReader(new FileReader(s))) {
            String line;
            while ((line = br.readLine()) != null) {
                filelines.add(line);
            }
        }
        catch (Exception e) {
            eLogging.logException(e);
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
                if(!arg.equals("#") && contains(arg)) {
                    writer.write(String.format("%s %s", arg, get(arg)));
                }
                else
                    writer.write(line);
                writer.write("\n");
            }
        } catch (Exception e) {
            eLogging.logException(e);
            e.printStackTrace();
        }
    }

    protected void init(String[] args) {

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
        return "null";
    }

    public void put(String key, String val) {
        gArg arg = getArg(key);
        if(arg != null) {
            String old = arg.value;
            arg.value = val;
            if(!arg.value.equals(old))
                arg.onChange();
        }
        else {
            putArg(new gArg(key, val) {
                public void onChange() {

                }
            });
        }
            xCon.instance().debug("gArgSet.put: no arg for key: " + key);
    }
}
