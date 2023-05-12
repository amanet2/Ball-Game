import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class gExecDoable {
    String fileSource;
    ArrayList<String> fileLines;

    public gExecDoable(String fileSource) {
        this.fileSource = fileSource;
        fileLines = new ArrayList<>();
        loadFromFileSource();
    }

    private void loadFromFileSource() {
        try (BufferedReader br = new BufferedReader(new FileReader(fileSource))) {
            String line;
            while ((line = br.readLine()) != null) {
                if(line.trim().length() > 0 && line.trim().charAt(0) != '#') {
                    fileLines.add(line);
                }
            }
        }
        catch (Exception e) {
            xMain.shellLogic.console.logException(e);
            e.printStackTrace();
        }
    }
}
