import java.io.BufferedReader;
import java.io.FileReader;

public class xComExecClient extends xCom {
    public String doCommand(String fullcommand) {
        String[] args = fullcommand.split(" ");
        String s = args[1];
        xCon.instance().debug("Loading exec: " + s);
        if(args.length > 2) {
            //parse the $ vars for placing prefabs
            for(int i = 2; i < args.length; i++) {
                sVars.put(String.format("$%d", i-1), args[i]);
            }
        }
        if(gExecDoableFactory.instance().execDoableMap.containsKey(s)) {
            xCon.instance().debug("EXEC_CLIENT FROM MEMORY: " + s);
            for(String line : gExecDoableFactory.instance().execDoableMap.get(s).fileLines) {
                xCon.ex(line.replace("putblock", "cl_putblock"
                ).replace("putitem", "cl_putitem"));
            }
        }
        else {
            try (BufferedReader br = new BufferedReader(new FileReader(s))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if(line.trim().length() > 0 && line.trim().charAt(0) != '#')
                        xCon.ex(line.replace("putblock", "cl_putblock"
                        ).replace("putitem", "cl_putitem"));
                }
            }
            catch (Exception e) {
                eLogging.logException(e);
                e.printStackTrace();
            }
        }
        return String.format("%s finished", s);
    }
}
