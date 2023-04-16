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
    }

    public void callScript(String[] args) {
        argSet.put("$0", id);
        int argCtr = 1;
        for(String arg : args) {
            argSet.put("$" + argCtr++, arg);
        }
        System.out.println(argSet.toString());
        for(String line : lines) {
            String[] lineArgCallTokens = line.trim().split(" ");
            for(int i = 0; i < lineArgCallTokens.length; i++) {
                if(lineArgCallTokens[i].startsWith("$")) {
                    String tokenKey = lineArgCallTokens[i];
                    if(argSet.contains(tokenKey))
                        lineArgCallTokens[i] = argSet.get(tokenKey);
                }
            }
            StringBuilder execStringBuilder = new StringBuilder();
            for(String lineArgtoken : lineArgCallTokens) {
                execStringBuilder.append(" ").append(lineArgtoken);
            }
            xCon.ex(execStringBuilder.substring(1));
        }
    }
}
