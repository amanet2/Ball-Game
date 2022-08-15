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
        mapsFileSelection = getFilesSelection("maps");
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

    public static int randomInt(int min, int max) {
        return new Random().nextInt(max - min + 1) + min;
    }

    public static String createId() {
        return Integer.toString(randomInt(11111111, 99999999));
    }

    public static String createBotId() {
        return Integer.toString(randomInt(11111, 99999));
    }
}
