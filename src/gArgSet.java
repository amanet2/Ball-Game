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
    private final ArrayList<String> filelines;

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
            xMain.shellLogic.console.ex("exec " + s);
            String line;
            while ((line = br.readLine()) != null) {
                filelines.add(line);
            }
        }
        catch (Exception e) {
            xMain.shellLogic.console.logException(e);
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
            boolean clientSave = s.equals(sSettings.CONFIG_FILE_LOCATION_CLIENT);
            for(String line : filelines) {
                String[] largs = line.split(" ");
                if(largs.length > 2 && largs[0].contains("setvar")) {
                    String tk = largs[1]; //var key
                    if(contains(tk)) {
                        String argLine = String.format("%s %s %s\n", clientSave ? "cl_setvar" : "setvar", tk, get(tk));
                        if(sSettings.clientDebug)
                            xMain.shellLogic.console.debug("SAVING FILE LINE W/ ARG" + argLine + " -> " + s);
                        writer.write(argLine);
                        continue;
                    }
                }
                if(sSettings.clientDebug)
                    xMain.shellLogic.console.debug("SAVING FILE LINE " + line + " -> " + s);
                writer.write(line);
                writer.write("\n");
            }
        } catch (Exception e) {
            xMain.shellLogic.console.logException(e);
        }
    }

    protected void init(String[] args) {

    }

    protected void putArg(gArg arg) {
        args.put(arg.key, arg);
        arg.onChange();
    }

    public String get(String key) {
        if(args.containsKey(key))
            return args.get(key).getValue();
        return "null";
    }

    public String[] parseScriptArgs(String full) {
        String[] args = full.trim().split(" ");
        for(int i = 0; i < args.length; i++) {
            if(args[i].startsWith("$") && contains(args[i].substring(1)))
                args[i] = get(args[i].substring(1));
        }
        return args;
    }

    public void put(String key, String val) {
        gArg arg = args.get(key);
        if(arg != null) {
            String old = arg.value;
            arg.value = val;
            if(!arg.value.equals(old))
                arg.onChange();
            arg.onUpdate();
        }
        else {
            xMain.shellLogic.console.debug("gArgSet.put: no arg for key: " + key);
            putArg(new gArg(key, val) {
                public void onChange() {

                }
            });
        }
    }
}
