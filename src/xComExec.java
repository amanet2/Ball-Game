import java.io.BufferedReader;
import java.io.FileReader;

public class xComExec extends xCom {
    public String doCommand(String fullcommand) {
        String[] args = fullcommand.split(" ");
        String title = args[1];
        xCon.instance().debug("Loading exec: " + title);
        if(args.length > 2) {
            //parse the $ vars for placing prefabs
            for(int i = 2; i < args.length; i++) {
                sVars.put(String.format("$%d", i-1), args[i]);
            }
        }
        if(gExecDoableFactory.instance().execDoableMap.containsKey(title)) {
            System.out.println("EXEC FROM MEMORY: " + title);
            for(String line : gExecDoableFactory.instance().execDoableMap.get(title).fileLines) {
                //parse vars for exec calls within exec (changemap)
                xCon.ex(line);
            }
        }
        else {
            try (BufferedReader br = new BufferedReader(new FileReader(title))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if(line.trim().length() > 0 && line.trim().charAt(0) != '#')
                        xCon.ex(line);
                }
            }
            catch (Exception e) {
                eUtils.echoException(e);
                e.printStackTrace();
            }
        }
        return String.format("%s finished", title);
    }
}
