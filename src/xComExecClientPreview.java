import java.io.BufferedReader;
import java.io.FileReader;

public class xComExecClientPreview extends xCom {
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
        try (BufferedReader br = new BufferedReader(new FileReader(s))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] linetoks = line.split(" ");
                if(linetoks[0].equalsIgnoreCase("putblock")) {
                    xCon.ex(line.replace("putblock", "cl_putblockpreview"));
                }
            }
        }
        catch (Exception e) {
            eUtils.echoException(e);
            e.printStackTrace();
        }
        return String.format("%s finished", s);
    }
}
