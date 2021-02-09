import java.io.BufferedReader;
import java.io.FileReader;

public class xComExec extends xCom {
    public String doCommand(String fullcommand) {
        String s = fullcommand.split(" ")[1];
        xCon.instance().debug("Loading exec: " + s);
        try (BufferedReader br = new BufferedReader(new FileReader(s))) {
            String line;
            while ((line = br.readLine()) != null) {
                if(line.trim().length() > 0 && line.trim().charAt(0) != '#') {
                    xCon.ex(line);
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
