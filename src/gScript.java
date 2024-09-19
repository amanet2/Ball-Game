import java.util.ArrayList;
import java.util.Arrays;

public class gScript {
    String id;
    ArrayList<String> lines;
    gArgSet argSet;

    public gScript(String filename, String content) {
        id = filename;
        lines = new ArrayList<>();
        argSet = new gArgSet();
        lines.addAll(Arrays.asList(content.split("\n")));
//        System.out.printf("CREATED SCRIPT: id=%s, contents=%s%n", id, content.replace("\n", "\\n"));
    }

    public String callScript(String[] args) {
        argSet.put("$0", id);
        int argCtr = 1;
        for(String arg : args) {
            argSet.put("$" + argCtr++, arg);
        }
        String result = "null";
        for(String line : lines) {
            String[] lineArgCallTokens = line.trim().split(" ");
            for(int i = 0; i < lineArgCallTokens.length; i++) {
                if(lineArgCallTokens[i].startsWith("$")) {
                    String tokenKey = lineArgCallTokens[i];
                    if(argSet.contains(tokenKey))
                        lineArgCallTokens[i] = argSet.get(tokenKey);
                    else if(xMain.shellLogic.serverVars.contains(tokenKey.substring(1)))
                        lineArgCallTokens[i] = xMain.shellLogic.serverVars.get(tokenKey.substring(1));
                }
            }
            StringBuilder execStringBuilder = new StringBuilder();
            for(String lineArgtoken : lineArgCallTokens) {
                execStringBuilder.append(" ").append(lineArgtoken);
            }
            result = xMain.shellLogic.console.ex(execStringBuilder.substring(1));
        }
        return result;
    }

    public void callScriptClientPreview(String[] args) {
        argSet.put("$0", id);
        int argCtr = 1;
        for(String arg : args) {
            argSet.put("$" + argCtr++, arg);
        }
        for(String line : lines) {
            String[] lineArgCallTokens = line.trim().split(" ");
            for(int i = 0; i < lineArgCallTokens.length; i++) {
                if(lineArgCallTokens[i].startsWith("$")) {
                    String tokenKey = lineArgCallTokens[i];
                    if(argSet.contains(tokenKey))
                        lineArgCallTokens[i] = argSet.get(tokenKey);
                    else if(xMain.shellLogic.clientVars.contains(tokenKey.substring(1)))
                        lineArgCallTokens[i] = xMain.shellLogic.clientVars.get(tokenKey.substring(1));
                }
            }
            StringBuilder execStringBuilder = new StringBuilder();
            for(String lineArgtoken : lineArgCallTokens) {
                execStringBuilder.append(" ").append(lineArgtoken);
            }
            xMain.shellLogic.console.ex(execStringBuilder.substring(1));
        }
    }

    public String toString() {
        return lines.toString();
    }
}
