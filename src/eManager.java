import java.io.File;
import java.util.*;

public class eManager {
	static int mapSelectionIndex = -1;
	static String[] mapsFileSelection;
	static String[] winSoundFileSelection;
	static String[] prefabFileSelection;
	static String[] configFileSelection;
	static String[] itemFilesSelection;
	static String[] scriptFilesSelection;

    public static void init() {
        configFileSelection = getFilesSelection("config");
        itemFilesSelection = getFilesSelection("items");
        prefabFileSelection = getFilesSelection("prefabs");
        scriptFilesSelection = getFilesSelection("scripts");
        mapsFileSelection = getFilesSelection("maps", ".map");
        winSoundFileSelection = getFilesSelection(eUtils.getPath("sounds/win"));
    }

	public static String[] getFilesSelection(String dirPath) {
	    String[] selectionArray = new String[]{};
        File fp = new File(dirPath);
        File[] fpContents = fp.listFiles();
        for(File ffp : fpContents) {
            if(ffp.isFile()) {
                selectionArray = Arrays.copyOf(selectionArray,selectionArray.length+1);
                selectionArray[selectionArray.length-1] = ffp.getName();
            }
        }
        return selectionArray;
    }

    public static String[] getFilesSelection(String dirPath, String extension) {
        String[] selectionArray = new String[]{};
        File fp = new File(dirPath);
        File[] fpContents = fp.listFiles();
        for(File ffp : fpContents) {
            if(ffp.isFile() && ffp.getName().split("\\.")[1].equalsIgnoreCase(
                    extension.replace(".",""))) {
                selectionArray = Arrays.copyOf(selectionArray,selectionArray.length+1);
                selectionArray[selectionArray.length-1] = ffp.getName();
            }
        }
        return selectionArray;
    }

    public static String createId() {
        int min = 11111111;
        int max = 99999999;
        int idInt = new Random().nextInt(max - min + 1) + min;
        return Integer.toString(idInt);
    }

    public static String createBotId() {
        int min = 11111;
        int max = 99999;
        int idInt = new Random().nextInt(max - min + 1) + min;
        return Integer.toString(idInt);
    }
}
