import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class gPrefab {
    String fileSource;
    ArrayList<String> prefabCommands;

    public gPrefab(String fileSource) {
        this.fileSource = fileSource;
        prefabCommands = new ArrayList<>();
    }

    public void loadFromFileSource(String fileSource) {
        this.fileSource = fileSource;
        try (BufferedReader br = new BufferedReader(new FileReader(fileSource))) {
            String line;
            while ((line = br.readLine()) != null) {
                if(line.trim().length() > 0 && line.trim().charAt(0) != '#') {
                    prefabCommands.add(line);
                }
            }
        }
        catch (Exception e) {
            eUtils.echoException(e);
            e.printStackTrace();
        }
    }

    public void putPrefab(int x, int y) {
        sVars.put("$1", Integer.toString(x));
        sVars.put("$2", Integer.toString(y));
        for(String line : prefabCommands) {
            xCon.ex(line);
        }
    }
}
