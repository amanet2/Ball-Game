package game;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;

public class eManager {
	static int mapSelectionIndex = -1;
	static String[] mapsFileSelection;
	static String[] prefabFileSelection;
	static String[] configFileSelection;
	static String[] itemFilesSelection;
	static String[] scriptFilesSelection;
    static HashMap<String, File> audioFiles;

    public static void init() {
        configFileSelection = getFilesSelection("config");
        itemFilesSelection = getFilesSelection("items");
        prefabFileSelection = getFilesSelection("prefabs");
        scriptFilesSelection = getFilesSelection("scripts");
        mapsFileSelection = getFilesSelection("maps");
        audioFiles = new HashMap<>();
    }

    public static File getAudioFile(String path) {
        if(!audioFiles.containsKey(path))
            audioFiles.put(path, new File(path));
        return audioFiles.get(path);
    }

	private static String[] getFilesSelection(String dirPath) {
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

    public static String getPath(String s) {
        return sSettings.datapath + "/" + s;
    }
}
